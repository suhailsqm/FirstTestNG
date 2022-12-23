package pom.com.vilcart.pom.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.LineNumber;
import util.com.vilcart.util.TimeStamp;

public class Packing {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private WebDriverWait wait;
	private Order or;

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

	private By orderCount = By.xpath("//*[@id=\"orderCount\"]");

	@FindBy(xpath = "(//*[@id=\"packingTuple\"])[1]")
	private WebElement firstTuple;

	public Packing(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		PageFactory.initElements(driver, this);
		or = new Order(driver, aw);
	}

	public void getTuplesForCurrentDate() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		/*
		 * System.out.println(dtf.format(now)); // 2021/03/22 16:37:15
		 * System.out.println(now.getOffset()); // +08:00
		 */
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		/*
		 * System.out.println(dtf.format(indiaDateTime)); // 2021/03/22 17:37:15
		 * System.out.println(indiaDateTime.getOffset()); // +09:00
		 * System.out.println();
		 */
		// js.executeScript("arguments[0].setAttribute('ng-reflect-model','"+dtf.format(indiaDateTime)+"');arguments[0].dispatchEvent(new
		// Event('ngModelChange'))",startDate);
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", startDate);

		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", endDate);
		submit.click();
		aw.waitAllRequest();
	}

	public void searchInPacking(String keyword) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(keyword);
		searchBtn.click();
		aw.waitAllRequest();
	}

	public void evaluateOrderCount() {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		WebElement orderCountWE = null;
		try {
			orderCountWE = wait.withMessage("No Orders in packing")
					.until(ExpectedConditions.presenceOfElementLocated(orderCount));
		} catch (org.openqa.selenium.TimeoutException e) {
			Reporter.log(LineNumber.getLineNumber() + " " + "No Orders in Packing", true);
			assertTrue(false);
			return;
		} catch (Exception e) {
			System.out.println("Some problem occured!!");
			return;
		}
		String temp = orderCountWE.getText();
		String[] tempIndex = temp.split(":");
		int count = Integer.parseInt(tempIndex[1].trim());
		Reporter.log(LineNumber.getLineNumber() + " " + "order count: " + count, true);
		if (count == 0) {
			Reporter.log(LineNumber.getLineNumber() + " " + "No orders to dispatch in Packing", true);
			assertThat(count).withFailMessage("No orders in Packing").isGreaterThan(0);
			return;
		}

	}

	public void evaluateFirstTuple(String orderNumber) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		Reporter.log("orderNumber:" + orderNumber, true);
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		getTuplesForCurrentDate();
		searchInPacking(orderNumber.substring(7));
		WebElement orderCount = null;
		try {
			orderCount = wait.withMessage("No Orders in packing")
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"orderCount\"]")));
		} catch (org.openqa.selenium.TimeoutException e) {
			Reporter.log(LineNumber.getLineNumber() + " " + "No Orders in Packing", true);
			assertTrue(false);
			return;
		} catch (Exception e) {
			System.out.println("Some problem occured!!");
			Reporter.log(LineNumber.getLineNumber() + " ", true);
			assertTrue(false);
			return;
		}

		String temp = orderCount.getText();
		String[] tempIndex = temp.split(":");
		int count = Integer.parseInt(tempIndex[1].trim());
		Reporter.log(LineNumber.getLineNumber() + " " + "order count: " + count, true);
		if (count == 0) {
			Reporter.log(LineNumber.getLineNumber() + " " + "No orders to dispatch in Packing", true);
			assertTrue(false);
			return;
		}
		assertThat(count).withFailMessage("No orders in Packing").isGreaterThan(0);

		firstTuple.findElement(By.xpath("//td[9]/div/button[1]")).click();
		aw.waitAllRequest();
		or.evaluateOrderInPacking(orderNumber);
	}

}
