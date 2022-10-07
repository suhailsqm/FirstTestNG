/**
 * 
 */
package pom.com.vilcart.pom.purchase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class PurchaseList {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private SelectVendor sv;

	@FindBy(id = "searchInput")
	private WebElement searchInput;

	@FindBy(id = "searchBtn")
	private WebElement searchBtn;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(xpath = "//*[@id=\"purchaseListTuple\"]")
	private List<WebElement> purchaseListTuples;

	public PurchaseList(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		sv = new SelectVendor(driver, aw);
	}

	public void searchInPurchaseList(String poNumber) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(poNumber.substring(3));
		searchBtn.click();
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

	public void processPurchaseList(String poNumber, String vendorName, String invoiceNumberArg) {
		getTuplesForCurrentDate();
		searchInPurchaseList(poNumber);

		assertThat(purchaseListTuples.size()).withFailMessage("no tuples in purchase List for po Number " + poNumber)
				.isGreaterThan(0);
		for (int i = 0; i < purchaseListTuples.size(); i++) {
			if (purchaseListTuples.get(i).findElement(By.xpath("//td[4]")).getText().equalsIgnoreCase(poNumber)) {
				WebElement temp = purchaseListTuples.get(i).findElement(By.xpath("//td[11]/div/button[1]"));
				js.executeScript("arguments[0].scrollIntoViewIfNeeded();", temp);
				temp.click();
				aw.waitAllRequest();
				break;
			}
			if (i == purchaseListTuples.size() - 1) {
				assertThat(false).withFailMessage("No tuples in purchaseList with the po Number "+poNumber).isEqualTo(true);
			}
		}
		sv.selectVendorInPurchaseList(vendorName, invoiceNumberArg);
		
	}
}
