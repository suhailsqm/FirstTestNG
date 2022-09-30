package pom.com.vilcart.pom.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

public class Invoice {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	@FindBy(xpath = "//*[@id=\"searchInput\"]")
	private WebElement searchInput;

	@FindBy(xpath = "//*[@id=\"searchButton\"]")
	private WebElement searchBtn;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(xpath = "//*[@id=\"invoiceTuple\"]")
	private List<WebElement> invoiceTuples;

	@FindBy(xpath = "//*[@id=\"actionBtn\"]")
	private List<WebElement> actionBtn;

	public Invoice(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
	}

	private String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}

	public void getTuplesForCurrentDate() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
//		ZonedDateTime now = ZonedDateTime.now();
		/*
		 * System.out.println(dtf.format(now)); // 2021/03/22 16:37:15
		 * System.out.println(now.getOffset()); // +08:00
		 */
//		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		/*
		 * System.out.println(dtf.format(indiaDateTime)); // 2021/03/22 17:37:15
		 * System.out.println(indiaDateTime.getOffset()); // +09:00
		 * System.out.println();
		 */
		// js.executeScript("arguments[0].setAttribute('ng-reflect-model','"+dtf.format(indiaDateTime)+"');arguments[0].dispatchEvent(new
		// Event('ngModelChange'))",startDate);
		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				startDate);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				endDate);
		submit.click();
		aw.waitAllRequest();
	}

	public void searchInInvoice(String keyword) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(keyword);
		searchBtn.click();
		aw.waitAllRequest();
	}

	public void evaluateOrderInInvoice(String orderNumber) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(invoiceTuples.size()).withFailMessage("No Tuples in invoice").isGreaterThan(0);
		for (int i = 0; i < invoiceTuples.size(); i++) {
			WebElement orderElement = invoiceTuples.get(i).findElement(By.xpath("//td[3]"));
			if (orderElement.getText().equalsIgnoreCase(orderNumber)) {
				assertThat(orderElement.getText()).withFailMessage("orderNumber doesnt match " + orderElement.getText())
						.isEqualToIgnoringCase(orderNumber);
				actionBtn.get(i).click();
				aw.waitAllRequest();
				break;
			}
		}
	}
}
