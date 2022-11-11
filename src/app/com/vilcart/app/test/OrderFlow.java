package app.com.vilcart.app.test;

import org.testng.annotations.Test;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.LineNumber;
import pom.com.vilcart.pom.login.Login;
import util.com.vilcart.util.TimeStamp;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeClass;

import static io.github.bonigarcia.wdm.WebDriverManager.isDockerAvailable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.chrome.ChromeDriverService;

public class OrderFlow {
	private WebDriver driver;
	private WebDriver driver1;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
	private Login loginObj;
	private InventoryChangeStock iv;
	private String orderNumber;
	private String invoiceNumber;
	private WebDriverManager wdm;

	private boolean docker = true;

	@Test(priority = 1)
	public void packing() throws IOException, InterruptedException {

		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		// iv.updateStock("17 test sku", 15);
		// iv.updateStock("test sku 508", 15);
		// iv.updateStock("208 test sku 2", 18);
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		WebElement menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();
		WebElement menuPacking = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a"));
		menuPacking.click();
		aw.waitAllRequest();
		menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", menuInput);
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();

		aw.waitAllRequest();

		WebElement startDate = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));// *[@id="startDate"]
		// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		System.out.println(dtf.format(now)); // 2021/03/22 16:37:15
		System.out.println(now.getOffset()); // +08:00
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		System.out.println(dtf.format(indiaDateTime)); // 2021/03/22 17:37:15
		System.out.println(indiaDateTime.getOffset()); // +09:00
		System.out.println();
		// js.executeScript("arguments[0].setAttribute('ng-reflect-model','"+dtf.format(indiaDateTime)+"');arguments[0].dispatchEvent(new
		// Event('ngModelChange'))",startDate);
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", startDate);
		WebElement endDate = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", endDate);
		driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
		aw.waitAllRequest();

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
			return;
		}
		String temp = orderCount.getText();
		String[] tempIndex = temp.split(":");
		int count = Integer.parseInt(tempIndex[1].trim());
		Reporter.log(LineNumber.getLineNumber() + " " + "order count: " + count, true);
		if (count == 0) {
			Reporter.log(LineNumber.getLineNumber() + " " + "No orders to dispatch in Packing", true);
			return;
		}
		assertThat(count).withFailMessage("No orders in Packing").isGreaterThan(0);

		WebElement temp1 = driver.findElement(By.xpath("(//*[@id=\"packingButton\"])[1]"));
		temp1.click();
		aw.waitAllRequest();
		Reporter.log(LineNumber.getLineNumber() + " " + "orderNumber1: "
				+ driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText(), true);
		// String orderNumber =
		// driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText().trim().split("()")[1];
		String orderNumber = driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText().trim()
				.replaceAll("^.|.$", "");
		Reporter.log(LineNumber.getLineNumber() + " " + "orderNumber: " + orderNumber, true);
		this.orderNumber = orderNumber;
		if (orderNumber.isEmpty()) {
			assertThat(orderNumber).withFailMessage("Order Number is empty").isNotEmpty();
			return;
		}

		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"orderTuple\"]"));

		assertThat(listTuples.size()).withFailMessage("No Tuples in Packing").isGreaterThan(0);
		for (int i = 0; i < listTuples.size(); i++) {
//    	  WebElement tuple = listTuples.get(i);
			String xpath = "//*[@id=\'orderTuple\'][" + (i + 1) + "]/td[12]/div/ui-switch/button";
			WebElement btn = driver.findElement(By.xpath(xpath));
			String value = btn.getAttribute("aria-checked");
			if (value.contains("false")) {
				String xpath1 = "//*[@id=\'orderTuple\'][" + (i + 1) + "]/td[12]/div/ui-switch";
				driver.findElement(By.xpath(xpath1)).click();
			}
			// btn.click();
//    	  js.executeScript("if(arguments[0].getAttribute('aria-checked')=='false') arguments[0].setAttribute('aria-checked','true');", tempIndex)  	  
		}

		int quantity = 0;
		String tempXpath = "//*[@id=\"originalQuantity\"]";
		List<WebElement> temp2 = driver.findElements(By.xpath(tempXpath));
		for (int i = 0; i < temp2.size(); i++) {
			WebElement temp3 = temp2.get(i);
			quantity += Integer.parseInt(temp3.getText());
		}
		assertThat(driver.findElement(By.xpath("//*[@id=\"totalQuantity\"]")).getText().split(":")[1].trim())
				.withFailMessage("Total Quantity doesn't tally").isEqualTo("" + quantity);
		int price = 0;
		List<WebElement> temp3Elements = driver.findElements(By.xpath("//*[@id=\"OriginalPrice\"]"));
		for (int i = 0; i < temp3Elements.size(); i++) {
			WebElement temp4 = temp3Elements.get(i);
			price += Integer.parseInt(temp4.getText());
		}
		assertThat(driver.findElement(By.xpath("//*[@id=\"totalPrice\"]")).getText().split("\\u20B9")[1].trim())
				.withFailMessage("Total Price not tallying.").isEqualTo("" + price);
		WebElement moveToInvoice = driver.findElement(By.xpath("//*[@id=\"moveToNext\"]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", moveToInvoice);
		moveToInvoice.click();
		aw.waitAllRequest();
	}

	@Test(priority = 2, dependsOnMethods = { "packing" })
	public void invoice() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		WebElement menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();
		WebElement menuPacking = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a"));
		menuPacking.click();
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();

		WebElement startDate = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));// *[@id="startDate"]
		// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

		// js.executeScript("arguments[0].setAttribute('ng-reflect-model','"+dtf.format(indiaDateTime)+"');arguments[0].dispatchEvent(new
		// Event('ngModelChange'))",startDate);
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", startDate);
		WebElement endDate = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", endDate);
		driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
		aw.waitAllRequest();

		WebElement search = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
		assertThat(orderNumber).withFailMessage("Order Number is Blank").isNotBlank();
		String temp = orderNumber.substring(7);
		// temp = "01983";
		search.sendKeys("" + temp);
		driver.findElement(By.xpath("//*[@id=\"searchButton\"]")).click();
		aw.waitAllRequest();

		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"invoiceTuple\"]"));
		assertThat(listTuples.size()).withFailMessage("No Tuples in invoice").isGreaterThan(0);
		for (int i = 0; i < listTuples.size(); i++) {
			String xpath = "(//*[@id=\"invoiceTuple\"])[" + (i + 1) + "]/td[3]";
			WebElement orderElement = driver.findElement(By.xpath(xpath));
			if (orderElement.getText().equalsIgnoreCase(orderNumber)) {
				assertThat(orderElement.getText()).withFailMessage("orderNumber doesnt match " + orderElement.getText())
						.isEqualToIgnoringCase(orderNumber);
				String xpath1 = "(//*[@id=\"actionBtn\"])[" + (i + 1) + "]";
				driver.findElement(By.xpath(xpath1)).click();
				break;
			}
		}
		aw.waitAllRequest();
		String handle = driver.getWindowHandle();
		WebElement moveToDispatch = driver.findElement(By.xpath("//*[@id=\"printAndDispatch\"]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", moveToDispatch);
		moveToDispatch.click();

		Reporter.log(LineNumber.getLineNumber() + " ", true);
		WebElement date = driver.findElement(By.xpath("//*[@id=\"datepicker\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", date);
		Select drpVehicle = new Select(driver.findElement(By.className("swal2-select")));
		drpVehicle.selectByVisibleText("KA 01 XE 6692");
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
		}

		// Thread.sleep(20000);

//		driver.switchTo().newWindow(WindowType.TAB);
//		// Opens LambdaTest homepage in the newly opened tab
//		driver.navigate().to("https://www.google.com/");

		Set<String> handles = driver.getWindowHandles();
		for (String actual : handles) {
			if (0 != actual.compareToIgnoreCase(handle)) {
				Reporter.log(LineNumber.getLineNumber() + "close " + actual, true);
				try {
					driver.switchTo().window(actual);
					Reporter.log(driver.getTitle(), true);
					driver.close();
//					driver.switchTo().window(actual).close();
					
				} catch (org.openqa.selenium.WebDriverException e) {
					Reporter.log("unknown error: failed to close window in 20 seconds Exception", true);
					js.executeScript("window.close();");
				}
			}
		}
		driver.switchTo().window(handle);
		aw.waitAllRequest();
	}

	@Test(priority = 3, dependsOnMethods = { "invoice" })
	public void dispatch() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		Reporter.log(LineNumber.getLineNumber() + " ", true);
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();

		WebElement menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();
		aw.waitAllRequest();

		WebElement menudispatch = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[3]/a"));
		menudispatch.click();
		aw.waitAllRequest();

		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);

		menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();
		aw.waitAllRequest();

		WebElement startDate = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));// *[@id="startDate"]
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", startDate);
		WebElement endDate = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", endDate);
		driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
		aw.waitAllRequest();

		assertThat(orderNumber).withFailMessage("Order Number is Blank").isNotBlank();
		String temp = orderNumber.substring(7);
		WebElement search = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
		search.sendKeys("" + temp);
		// search.sendKeys("01554");
		driver.findElement(By.xpath("//*[@id=\"searchButton\"]")).click();
		aw.waitAllRequest();
		Thread.sleep(3000);

		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"dispatchTuple\"]"));
		assertThat(listTuples.size()).withFailMessage("No Tuples in dispatch").isGreaterThan(0);
		assertThat(listTuples.size()).withFailMessage("criteria only one invoice for one order doesn't match")
				.isEqualTo(1);
		for (int i = 0; i < listTuples.size(); i++) {
			String xpath = "(//*[@id=\"dispatchTuple\"])[" + (i + 1) + "]/td[3]";
			WebElement orderElement = driver.findElement(By.xpath(xpath));
			String text = orderElement.getText();
			this.invoiceNumber = text;
//			Reporter.log("actual " + text + " " + i + " " + listTuples.size() + " " + orderNumber, true);
//			if (text.equalsIgnoreCase(orderNumber)) {
			Reporter.log("invoice Found " + text, true);
			String xpath1 = "//*[@id=\"dispatchTuple\"][" + (i + 1) + "]/td[9]/ng-select/div/div/div[2]/input";
			WebElement vehicleNumber = driver.findElement(By.xpath(xpath1));
			vehicleNumber.click();
			String xpath2 = "//*[@id=\"selectVehicle\"]/ng-dropdown-panel/div/div[2]/div";
			List<WebElement> listVehicles = driver.findElements(By.xpath(xpath2));
			assertThat(listVehicles.size()).withFailMessage("No vehicles in dispatch dropdown").isGreaterThan(0);
			for (int j = 0; j < listVehicles.size(); j++) {
				String xpath3 = "//*[@id=\"selectVehicle\"]/ng-dropdown-panel/div/div[2]/div[" + (j + 1) + "]/span";
				Reporter.log(driver.findElement(By.xpath(xpath3)).getText(), true);
				if (driver.findElement(By.xpath(xpath3)).getText().equalsIgnoreCase("KA 02 EA 3344")) {
					driver.findElement(By.xpath(xpath3)).click();
					break;
				}
				if (j == (listVehicles.size() - 1)) {
					Reporter.log("No option for vehicle, KA 02 EA 3344", true);
				}
			}
			// vehicleNumber.sendKeys("KA 02 EA 3333");
			driver.findElement(By.xpath("//button[normalize-space()='Update']")).click();
			driver.findElement(By.xpath("//button[normalize-space()='OK']")).click();
			aw.waitAllRequest();
			String xpath3 = "//*[@id=\"dispatchTuple\"][" + (i + 1) + "]/td[10]/div/button";
			driver.findElement(By.xpath(xpath3)).click();
			aw.waitAllRequest();
			break;
//			}
//			if (i == listTuples.size() - 1) {
//				assertThat(text).withFailMessage(
//						"Order Number " + text + "doesn't tally for " + orderNumber)
//						.isEqualToIgnoringCase(orderNumber);
//			}
		}

	}

	@Test(priority = 4, dependsOnMethods = { "dispatch" })
	// @Test
	public void delivery() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		
		  WebElement menuOrders; /*=
		  driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		  menuOrders.click();*/
		 
		WebElement menuDelivery = driver.findElement(By.xpath(" //*[@id=\"main-menu-navigation\"]/li/ul/li[4]/a"));
		menuDelivery.click();
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();

		WebElement startDate = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));// *[@id="startDate"]

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", startDate);
		WebElement endDate = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", endDate);
		driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
		aw.waitAllRequest();

		assertThat(orderNumber).withFailMessage("Order Number is Blank").isNotBlank();
		String temp = orderNumber.substring(7);
		WebElement search = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
		search.sendKeys(temp);
//		search.sendKeys("01558");
		driver.findElement(By.xpath("//*[@id=\"searchButton\"]")).click();
		aw.waitAllRequest();

		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"deliveryTuple\"]"));
		assertThat(listTuples.size()).withFailMessage("No Tuples in delivery").isGreaterThan(0).descriptionText();
		for (int i = 0; i < listTuples.size(); i++) {
			String xpath = "(//*[@id=\"deliveryTuple\"])[" + (i + 1) + "]/td[4]";
			WebElement orderElement = driver.findElement(By.xpath(xpath));
			if (orderElement.getText().equalsIgnoreCase(invoiceNumber)) {
				assertThat(orderElement.getText())
						.withFailMessage("invoice Number doesn't tally" + orderElement.getText())
						.isEqualToIgnoringCase(invoiceNumber);
				String xpath1 = "//*[@id=\"deliveryTuple\"]/td[10]/div/button";
				try {
					WebElement action = driver.findElement(By.xpath(xpath1));
					action.click();
					aw.waitAllRequest();
					break;
				} catch (org.openqa.selenium.StaleElementReferenceException ex) {
					WebElement action = driver.findElement(By.xpath(xpath1));
					action.click();
					aw.waitAllRequest();
					break;
				}
			}
		}
	}

	@Test(priority = 5, dependsOnMethods = { "delivery" })
	public void complete() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);

		WebElement menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();

		WebElement menuComplete = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[5]/a"));
		menuComplete.click();
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));

		WebElement startDate = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));// *[@id="startDate"]
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", startDate);
		WebElement endDate = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input'))", endDate);
		driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
		aw.waitAllRequest();

		WebElement search = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
		search.sendKeys("" + orderNumber);
		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"completeTuple\"]"));
		assertThat(listTuples.size()).withFailMessage("No tuples in complete").isGreaterThan(0);
		for (int i = 0; i < listTuples.size(); i++) {
			String xpath = "(//*[@id=\"completeTuple\"])[" + (i + 1) + "]/td[4]";
			WebElement orderElement = driver.findElement(By.xpath(xpath));
			if (orderElement.getText().equalsIgnoreCase(orderNumber)) {
				assertThat(orderElement.getText()).withFailMessage("orderNumber doesn't tally" + orderElement.getText())
						.isEqualToIgnoringCase(orderNumber);
			}
		}
	}

	@BeforeClass
	public void beforeOrderFlow() throws IOException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
//		WebDriverManager.firefoxdriver().setup();

//		ChromeOptions options = new ChromeOptions();
		// ChromeDriver is just AWFUL because every version or two it breaks unless you
		// pass cryptic arguments
		// AGRESSIVE: options.setPageLoadStrategy(PageLoadStrategy.NONE); //
		// https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
//		options.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
//		options.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
//		options.addArguments("--headless"); // only if you are ACTUALLY running headless
//		options.addArguments("--no-sandbox"); // https://stackoverflow.com/a/50725918/1689770
//		options.addArguments("--disable-dev-shm-usage"); // https://stackoverflow.com/a/50725918/1689770
//		options.addArguments("--disable-browser-side-navigation"); // https://stackoverflow.com/a/49123152/1689770
//		options.addArguments("--disable-gpu"); // https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc
//		driver = new ChromeDriver(options);

		// This option was deprecated, see
		// https://sqa.stackexchange.com/questions/32444/how-to-disable-infobar-from-chrome
		// options.addArguments("--disable-infobars");
		// //https://stackoverflow.com/a/43840128/1689770

		// options.addArguments("--window-size=1366,768");
//		options.addArguments("--no-sandbox");
//		options.addArguments("--disable-gpu");
//		options.addArguments("--enable-javascript");
//		options.addArguments("disable-infobars");
//		options.addArguments("--disable-infobars");
//		options.addArguments("--single-process");
//		options.addArguments("--disable-extensions");
//		options.addArguments("--disable-dev-shm-usage");
//		options.addArguments("--headless");
//		options.addArguments("enable-automation");
//		options.addArguments("--disable-browser-side-navigation");

//		options.addArguments("enable-automation");
//		options.addArguments("--headless");
		// options.addArguments("--window-size=1920,1080");
//		options.addArguments("--no-sandbox");
//		options.addArguments("--disable-extensions");
//		options.addArguments("--dns-prefetch-disable");
//		options.addArguments("--disable-gpu");
//		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
//		System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY,"true");
//		driver = new ChromeDriver(options);
//		driver = new ChromeDriver();
//		driver = new FirefoxDriver();

		if (docker) {
			WebDriverManager.chromedriver().setup();
			driver1 = new ChromeDriver();
			wdm = WebDriverManager.chromedriver().browserInDocker().enableVnc();
			assumeThat(isDockerAvailable()).isTrue();
			driver = wdm.create();
			// Verify URL for remote session
			URL noVncUrl = wdm.getDockerNoVncUrl();
			assertThat(noVncUrl).isNotNull();
			Reporter.log(noVncUrl + "", true);
			driver1.get(noVncUrl + "");
		} else {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		driver.get("http://192.168.1.48:4200");
		// driver.get("https://vilcart-buy.web.app");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		Reporter.log(LineNumber.getLineNumber() + " " + driver.getTitle(), true);
		js = ((JavascriptExecutor) driver);
		wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		iv = new InventoryChangeStock(driver, js, aw, wait);
		loginObj.login();
	}

	@AfterClass
	public void afterOrderFlow() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		driver.quit();
		if (docker) {
			driver1.quit();
			wdm.quit();
		}

	}
}
