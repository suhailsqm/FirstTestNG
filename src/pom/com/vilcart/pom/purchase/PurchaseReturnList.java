/**
 * 
 */
package pom.com.vilcart.pom.purchase;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
public class PurchaseReturnList {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private CreatePurchaseReturn cpr;

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement searchInput;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(xpath = "//*[@id=\"purchaseReturn\"]")
	private WebElement createPurchaseReturn;

	@FindBy(id = "purchaseReturnTuples")
	private List<WebElement> purchaseReturnTuples;

	public PurchaseReturnList(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		this.cpr = new CreatePurchaseReturn(driver, aw);
		PageFactory.initElements(driver, this);
	}

	public void searchInPurchaseList(String poNumber) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(poNumber.substring(3));
		searchInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	private void getTuplesForCurrentDate() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				startDate);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				endDate);
		submit.click();
		aw.waitAllRequest();
	}

	private String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}

	public void purchaseReturnList(String poNumber, String vendorName) {
		createPurchaseReturn.click();
		aw.waitAllRequest();
		cpr.selectpurchaseToReturn(poNumber, vendorName);

		getTuplesForCurrentDate();
		searchInPurchaseList(poNumber);

		for (int i = 0; i < purchaseReturnTuples.size(); i++) {
			if (purchaseReturnTuples.get(i).findElement(By.xpath("\\td[3]")).getText().trim()
					.equalsIgnoreCase(poNumber)) {
				assertThat(true).withFailMessage("po Number matches in purchase Return List").isEqualTo(true);
				break;
			}
			if (i == purchaseReturnTuples.size()) {
				assertThat(true).withFailMessage("po Number doesn't in purchase Return List " + poNumber)
						.isEqualTo(false);
			}
		}
	}
}
