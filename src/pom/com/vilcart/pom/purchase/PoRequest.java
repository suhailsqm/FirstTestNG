/**
 * 
 */
package pom.com.vilcart.pom.purchase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author win10
 *
 */
public class PoRequest {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	WebElement searchInput;

	@FindBy(id = "poRequestTuple")
	List<WebElement> poRequestTuples;
	
	@FindBy(id = "approveBtn")
	WebElement approveBtn;

	public PoRequest(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(this.driver, this);
	}

	public void searchPoRequest(String poNumber) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(poNumber.substring(3));
		aw.waitAllRequest();
	}
/*
 * Only First Tuple is processed. It is expected that only the first tuple is required.
 * */
	public void processPoRequest(String poNumber) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(poRequestTuples.size()).withFailMessage("NO orders with PO Number " + poNumber).isGreaterThan(0);
		for (int i = 0; i < poRequestTuples.size(); i++) {
			if(poRequestTuples.get(i).findElement(By.xpath("//td[2]")).getText().equalsIgnoreCase(poNumber)) {
				poRequestTuples.get(i).findElement(By.xpath("//td[7]/div/button")).click();
				aw.waitAllRequest();
				break;
			}
		}
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", approveBtn);
		approveBtn.click();
	}
}
