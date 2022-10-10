package pom.com.vilcart.pom.adminlist;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import static org.assertj.core.api.Assertions.assertThat;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

public class AdminList {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement searchButton;

	@FindBy(id = "adminTuple")
	private List<WebElement> adminTuples;
	
	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;
	
	@FindBy(xpath = "(//button[normalize-space()='OK'])[1]")
	private WebElement ok;

	public AdminList(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
	}

	public void searchInAdminList(String keyword) {
		searchButton.clear();
		searchButton.sendKeys(keyword);
		searchButton.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	public void verifyAdminPresent(String email) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInAdminList(email);
		for (int i = 0; i < adminTuples.size(); i++) {
			if (adminTuples.get(i).findElement(By.xpath("//td[3]")).getText().equalsIgnoreCase(email)) {
				break;
			}
			if (adminTuples.size() - 1 == i) {
				assertThat(false).withFailMessage("No Tuple with email " + email).isEqualTo(true);
			}
		}
	}

	public void deleteAdmin(String email) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInAdminList(email);
		for (int i = 0; i < adminTuples.size(); i++) {
			if (adminTuples.get(i).findElement(By.xpath("//td[3]")).getText().equalsIgnoreCase(email)) {
				adminTuples.get(i).findElement(By.xpath("//td[6]/div/button")).click();
				submit.click();
				ok.click();
			}
			if (adminTuples.size() - 1 == i) {
				assertThat(false).withFailMessage("No Tuple with email " + email).isEqualTo(true);
			}
		}
	}
	
	public void createAdmin() {
		//TODO
	}
}
