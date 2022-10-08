package pom.com.vilcart.pom.goods;

import static org.assertj.core.api.Assertions.assertThat;

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

public class GoodsReceive {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private GoodsReceiveCreation grc;

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement searchInput;

	@FindBy(id = "createGoodsReceive")
	private WebElement createGoodsReceive;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(id = "receiveListTuple")
	private List<WebElement> receiveListTuples;

	@FindBy(id = "orderListTuple")
	private List<WebElement> orderListTuples;

	public GoodsReceive(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		grc = new GoodsReceiveCreation(driver, aw);
	}

	public void searchInGoodsreceive(String key) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(key);
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

	public void receiveGoods(String dc, String vehicle, String challanNoArg) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		createGoodsReceive.click();
		grc.createGoodsReceive(dc, vehicle, challanNoArg);
		getTuplesForCurrentDate();
		searchInGoodsreceive(vehicle);
		// ToDo verifying with vehicle number should be a challan no which has to pop up
		// in
		// create Goods receive.
		for (int i = 0; i < receiveListTuples.size(); i++) {
			if (receiveListTuples.get(i).findElement(By.xpath("//td[4]")).getText().equalsIgnoreCase(vehicle)) {
				receiveListTuples.get(i).findElement(By.xpath("//td[11]/div/button[1]")).click();
				aw.waitAllRequest();
				// ToDo check if all order Items are present in the Goods receive.
				break;
			}
			if (i == receiveListTuples.size() - 1) {
				assertThat(false).withFailMessage("No tuple with vehicle Number " + vehicle).isEqualTo(true);
			}
		}
	}
}
