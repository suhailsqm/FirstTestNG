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

public class Delivery {
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

	@FindBy(xpath = "//*[@id=\"deliveryTuple\"]")
	private List<WebElement> deliveryTuples;

	public Delivery(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
	}

	public void searchInDelivery(String keyword) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(keyword);
		searchBtn.click();
		aw.waitAllRequest();
	}

	public void getTuplesForCurrentDate() {
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

	public void evaluateOrderInDelivery(String orderNumber) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		assertThat(deliveryTuples.size()).withFailMessage("No Tuples in delivery").isGreaterThan(0).descriptionText();
		for (int i = 0; i < deliveryTuples.size(); i++) {
			WebElement orderElement = deliveryTuples.get(i).findElement(By.xpath("//td[4]"));
			if (orderElement.getText().equalsIgnoreCase(orderNumber)) {
				assertThat(orderElement.getText())
						.withFailMessage("order Number doesn't tally" + orderElement.getText())
						.isEqualToIgnoringCase(orderNumber);
				try {
					WebElement action = deliveryTuples.get(i).findElement(By.xpath("//td[10]/div/button"));
					action.click();
				} catch (org.openqa.selenium.StaleElementReferenceException ex) {
					WebElement action = deliveryTuples.get(i).findElement(By.xpath("//td[10]/div/button"));
					action.click();
					aw.waitAllRequest();
				}
				break;
			}
		}
	}
}
