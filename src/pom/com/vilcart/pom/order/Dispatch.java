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

public class Dispatch {
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

	@FindBy(xpath = "//*[@id=\"dispatchTuple\"]")
	private List<WebElement> dispatchTuples;

	@FindBy(xpath = "//*[@id=\"selectVehicle\"]")
	private List<WebElement> selectVehicleInTuples;

	@FindBy(xpath = "//button[normalize-space()='Update']")
	private WebElement update;

	@FindBy(xpath = "//button[normalize-space()='OK']")
	private WebElement ok;

	public Dispatch(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
	}

	public void searchInDispatch(String keyword) {
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

	public void evaluateOrderInDispatch(String orderNumber, String vehicleNumber) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(dispatchTuples.size()).withFailMessage("No Tuples in dispatch").isGreaterThan(0);
		for (int i = 0; i < dispatchTuples.size(); i++) {
			String xpath = "//td[3]";
			WebElement orderElement = dispatchTuples.get(i).findElement(By.xpath(xpath));
			String text = orderElement.getText();
			assertThat(text).withFailMessage("Order Number doesn't tally" + text).isEqualToIgnoringCase(orderNumber);
			if (text.equalsIgnoreCase(orderNumber)) {
				String xpath1 = "//td[9]/ng-select/div/div/div[2]/input";
				WebElement vehicleNumberWE = dispatchTuples.get(i).findElement(By.xpath(xpath1));
				vehicleNumberWE.click();
				String xpath2 = "//ng-dropdown-panel/div/div[2]/div";
				List<WebElement> listVehicles = selectVehicleInTuples.get(i).findElements(By.xpath(xpath2));
				assertThat(listVehicles.size()).withFailMessage("No vehicles in dispatch dropdown").isGreaterThan(0);
				for (int j = 0; j < listVehicles.size(); j++) {
					String xpath3 = "//ng-dropdown-panel/div/div[2]/div[" + (j + 1) + "]/span";
					Reporter.log(selectVehicleInTuples.get(i).findElement(By.xpath(xpath3)).getText(), true);
					if (selectVehicleInTuples.get(i).findElement(By.xpath(xpath3)).getText()
							.equalsIgnoreCase(vehicleNumber)) {
						selectVehicleInTuples.get(i).findElement(By.xpath(xpath3)).click();
						break;
					}
					if (j == (listVehicles.size() - 1)) {
						Reporter.log("No option for vehicle Number in dispatch, " + vehicleNumber, true);
						assertThat(false)
								.withFailMessage(
										"No Vehicle option in dispatch for this vehicle Number" + vehicleNumber)
								.isEqualTo(true);
					}
				}
				// vehicleNumber.sendKeys("KA 02 EA 3333");
				update.click();
				ok.click();
				String xpath3 = "//td[10]/div/button";
				dispatchTuples.get(i).findElement(By.xpath(xpath3)).click();
				aw.waitAllRequest();
				break;
			}
		}
	}

	private String getDate() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}
}
