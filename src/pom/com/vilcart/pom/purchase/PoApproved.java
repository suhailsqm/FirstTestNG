/**
 * 
 */
package pom.com.vilcart.pom.purchase;

import static org.assertj.core.api.Assertions.assertThat;

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

/**
 * @author win10
 *
 */
public class PoApproved {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	@FindBy(id = "searchInput")
	WebElement searchInput;

	@FindBy(id = "searchBtn")
	WebElement searchBtn;

	@FindBy(id = "poApproveTuple")
	List<WebElement> poApproveTuples;

	@FindBy(id = "poApproveSubItem")
	List<WebElement> poApproveSubItems;

	@FindBy(id = "grnButton")
	WebElement grnButton;

	@FindBy(id = "cancelBtn")
	WebElement cancelBtn;

	public PoApproved(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(this.driver, this);
	}

	public void searchPoApprove(String poNumber) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(poNumber.substring(3));
		searchBtn.click();
		aw.waitAllRequest();
	}

	/*
	 * Only First Tuple is processed. It is expected that only the first tuple is
	 * required.
	 */
	public void processPoApproved(String poNumber) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(poApproveTuples.size()).withFailMessage("NO orders with PO Number " + poNumber).isGreaterThan(0);
		for (int i = 0; i < poApproveTuples.size(); i++) {
			if (poApproveTuples.get(i).findElement(By.xpath("//td[2]")).getText().equalsIgnoreCase(poNumber)) {
				WebElement temp = poApproveTuples.get(i).findElement(By.xpath("//td[9]/div/button"));
				js.executeScript("arguments[0].scrollIntoViewIfNeeded();", temp);
				temp.click();
				aw.waitAllRequest();
				break;
			}
		}

		assertThat(poApproveSubItems.size()).withFailMessage("No Tuples in Po Approved/ Category/ GRN")
				.isGreaterThan(0);
		for (int i = 0; i < poApproveSubItems.size(); i++) {
			WebElement tuple = poApproveSubItems.get(i);
			WebElement grnRowBtn = tuple.findElement(By.xpath("//td[9]/div/ui-switch/button"));
			String value = grnRowBtn.getAttribute("aria-checked");
			if (value.contains("false")) {
				WebElement test = tuple.findElement(By.xpath("//td[9]/div/ui-switch"));
				test.click();
			}
		}
		aw.waitAllRequest();
		
		grnButton.click();
		
	}
}
