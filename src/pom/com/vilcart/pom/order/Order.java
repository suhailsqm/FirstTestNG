package pom.com.vilcart.pom.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.LineNumber;
import util.com.vilcart.util.TimeStamp;

public class Order {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;

	@FindBy(xpath = "//*[@id=\"orderNumber\"]")
	private WebElement orderNumberElement;

	@FindBy(xpath = "//*[@id=\"orderTuple\"]")
	private List<WebElement> listTuples;

	@FindBy(xpath = "//*[@id=\"originalQuantity\"]")
	private List<WebElement> originalQuantity;

	@FindBy(xpath = "//*[@id=\"totalQuantity\"]")
	private WebElement totalQuantity;

	@FindBy(xpath = "//*[@id=\"OriginalPrice\"]")
	private List<WebElement> OriginalPrice;

	@FindBy(xpath = "//*[@id=\"totalPrice\"]")
	private WebElement totalPrice;

	@FindBy(xpath = "//*[@id=\"moveToNext\"]")
	private WebElement moveToInvoice;

	@FindBy(xpath = "//*[@id=\"quantity\"]")
	private List<WebElement> quantity;

	@FindBy(xpath = "//*[@id=\"subTotal\"]")
	private List<WebElement> subTotal;

	@FindBy(xpath = "//*[@id=\"printAndDispatch\"]")
	private WebElement moveToDispatch;

	@FindBy(className = "swal2-select")
	private WebElement selectVehicle;

	@FindBy(css = "button[class='swal2-confirm swal2-styled']")
	private WebElement submitBtn;

	@FindBy(xpath = "//*[@id=\"datepicker\"]")
	private WebElement date;

	public Order(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		PageFactory.initElements(driver, this);
	}

	public String getOrderNumber() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		String orderNumber = orderNumberElement.getText().trim().replaceAll("^.|.$", "");
		Reporter.log("orderNumber: " + orderNumber, true);

		if (orderNumber.isEmpty()) {
			assertThat(orderNumber).withFailMessage("Order Number is empty").isNotEmpty();
			return null;
		}
		return orderNumber;
	}

	public void evaluateOrderInPacking(String orderNumberArg) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		Reporter.log(LineNumber.getLineNumber() + " " + "orderNumber1: "
				+ driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText(), true);
		// String orderNumber =
		// driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText().trim().split("()")[1];
		String orderNumber = driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText().trim()
				.replaceAll("^.|.$", "");
		Reporter.log(LineNumber.getLineNumber() + " " + "orderNumber: " + orderNumber, true);
		if (orderNumber.isEmpty()) {
			assertThat(orderNumber).withFailMessage("Order Number is empty").isNotEmpty();
			return;
		}
		assertThat(orderNumberArg).withFailMessage(orderNumberArg + " Doesnt match " + orderNumber)
				.isEqualTo(orderNumber);

		assertThat(listTuples.size()).withFailMessage("No Tuples in Packing").isGreaterThan(0);
		for (int i = 0; i < listTuples.size(); i++) {
			WebElement tuple = listTuples.get(i);
			WebElement orderRowBtn = tuple.findElement(By.xpath(".//td[12]/div/ui-switch/button"));
			String value = orderRowBtn.getAttribute("aria-checked");
			if (value.contains("false")) {
				WebElement test = tuple.findElement(By.xpath(".//td[12]/div/ui-switch"));
				test.click();
			}
		}

//		int quantityHolder = 0;
//		for (int i = 0; i < originalQuantity.size(); i++) {
//			WebElement temp = originalQuantity.get(i);
//			quantityHolder += Integer.parseInt(temp.getText());
//		}
//		assertThat(totalQuantity.getText().split(":")[1].trim()).withFailMessage("Total Quantity doesn't tally "+)
//				.isEqualTo("" + quantityHolder);
		try {
			assertThat(totalQuantity.getText().split(":")[1].trim()).withFailMessage("Total Quantity doesn't tally ")
					.isEqualTo("" + listTuples.size());
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			Reporter.log(totalQuantity.getText() + " can't be split using :", true);
			Reporter.log("" + listTuples.size(), true);
		}

		int price = 0;
		for (int i = 0; i < OriginalPrice.size(); i++) {
			WebElement temp4 = OriginalPrice.get(i);
			price += Integer.parseInt(temp4.getText().replace(",", ""))
					* Integer.parseInt(originalQuantity.get(i).getText());
		}
		try {
			assertThat(totalPrice.getText().split("\\u20B9")[1].trim()).withFailMessage("Total Price not tallying.")
					.isEqualTo("" + price);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			Reporter.log(totalPrice.getText() + " can't be split using \\u20B9", true);
			Reporter.log("" + price, true);
		}
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", moveToInvoice);
		moveToInvoice.click();
		aw.waitAllRequest();
	}

	public void evaluateOrderInInvoice(String vehicle) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(listTuples.size()).withFailMessage("No Tuples in Invoice").isGreaterThan(0);

//		int quantityHolder = 0;
//		for (int i = 0; i < quantity.size(); i++) {
//			WebElement temp = quantity.get(i);
//			quantityHolder += Integer.parseInt(temp.getText());
//		}
		assertThat(totalQuantity.getText().split(":")[1].trim()).withFailMessage("Total Quantity doesn't tally")
				.isEqualTo("" + listTuples.size());

		int price = 0;
		for (int i = 0; i < subTotal.size(); i++) {
			WebElement temp4 = subTotal.get(i);
			price += Integer.parseInt(temp4.getText().replace(",", ""));
		}
		assertThat(totalPrice.getText().split("\\u20B9")[1].trim()).withFailMessage("Total Price not tallying.")
				.isEqualTo("" + price);

		String handle = driver.getWindowHandle();
		Reporter.log("Window not supposed to close is " + handle, true);
		WebElement moveToDispatch = driver.findElement(By.xpath("//*[@id=\"printAndDispatch\"]"));
		Reporter.log("Window not supposed to close is " + handle, true);
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", moveToDispatch);
		moveToDispatch.click();
		Reporter.log("Window not supposed to close is " + handle, true);

		Reporter.log(LineNumber.getLineNumber() + " ", true);
		WebElement date = driver.findElement(By.xpath("//*[@id=\"datepicker\"]"));
		js.executeScript("arguments[0].value = '" + getDate()
				+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", date);
		Select drpVehicle = new Select(driver.findElement(By.className("swal2-select")));
		drpVehicle.selectByVisibleText(vehicle);
		Reporter.log(LineNumber.getLineNumber() + " ", true);
		driver.findElement(By.cssSelector("button[class='swal2-confirm swal2-styled']")).click();
		/* dont use the below line it's not working */
//		aw.waitAllRequest();
		Reporter.log(LineNumber.getLineNumber() + " ", true);

		Reporter.log(LineNumber.getLineNumber() + " " + handle, true);

		try {
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
			WebElement cancel = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[@id=\"sidebar\"]//print-preview-button-strip//div/cr-button[2]")));
			cancel.click();
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		} catch (org.openqa.selenium.TimeoutException e) {
			Reporter.log("timeOut Exception", true);
			js.executeScript("window.stop();");
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		}

		// Thread.sleep(20000);

//		driver.switchTo().newWindow(WindowType.TAB);
//		// Opens LambdaTest homepage in the newly opened tab
//		driver.navigate().to("https://www.google.com/");
		try {
			Set<String> handles = driver.getWindowHandles();

			for (String actual : handles) {
				Reporter.log(LineNumber.getLineNumber() + "list " + actual, true);
			}

			for (String actual : handles) {
				if (0 != actual.compareToIgnoreCase(handle)) {
					Reporter.log(LineNumber.getLineNumber() + "close " + actual, true);
					try {
						driver.switchTo().window(actual);
						Reporter.log(LineNumber.getLineNumber() + "close " + actual, true);
						Reporter.log("title is " + driver.getTitle() + " url is " + driver.getCurrentUrl(), true);
						driver.close();
						Reporter.log(LineNumber.getLineNumber() + "close " + actual, true);
//					driver.switchTo().window(actual).close();

					} catch (org.openqa.selenium.WebDriverException e) {
						Reporter.log(e.getRawMessage(), true);
						Reporter.log("unknown error: failed to close window in 20 seconds Exception", true);
						js.executeScript("window.close();");
					}
				}
			}
			driver.switchTo().window(handle);
			aw.waitAllRequest();
		} catch (org.openqa.selenium.NoSuchWindowException e) {
			driver.switchTo().window(handle);
			aw.waitAllRequest();
		}
	}

	private String getDate() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}
}
