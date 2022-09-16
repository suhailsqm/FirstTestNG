package com.vilcart.app;

import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.vilcart.util.AngularWait;
import com.vilcart.util.Login;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeClass;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import static org.assertj.core.api.Assertions.assertThat;
import org.openqa.selenium.support.ui.Select;

public class OrderFlow {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
	private Login loginObj;
	private InventoryChangeStock iv;
	private String orderNumber;

	@Test (priority=1)
	public void Packing() throws IOException, InterruptedException {

		 //iv.updateStock("17 test sku", 15);
		 iv.updateStock("test sku 508", 15);
		 /*
		// iv.updateStock("208 test sku 2", 18);
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		WebElement menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();
		WebElement menuPacking = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a"));
		menuPacking.click();

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
			Reporter.log("No Orders in Packing", true);
			return;
		} catch (Exception e) {
			System.out.println("Some problem occured!!");
			return;
		}
		String temp = orderCount.getText();
		String[] tempIndex = temp.split(":");
		int count = Integer.parseInt(tempIndex[1].trim());
		Reporter.log("order count: " + count, true);
		if (count == 0) {
			Reporter.log("No orders to dispatch in Packing");
			return;
		}
		assertThat(count).isGreaterThan(0);

		WebElement temp1 = driver.findElement(By.xpath("(//*[@id=\"packingButton\"])[1]"));
		temp1.click();
		aw.waitAllRequest();
		Reporter.log("orderNumber1: " + driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText(), true);
		// String orderNumber =
		// driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText().trim().split("()")[1];
		String orderNumber = driver.findElement(By.xpath("//*[@id=\"orderNumber\"]")).getText().trim()
				.replaceAll("^.|.$", "");
		Reporter.log("orderNumber: " + orderNumber, true);
		this.orderNumber = orderNumber;
        if(orderNumber.isEmpty()) return;
		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"orderTuple\"]"));

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
				.isEqualTo("" + quantity);

		int price = 0;
		List<WebElement> temp3Elements = driver.findElements(By.xpath("//*[@id=\"OriginalPrice\"]"));
		for (int i = 0; i < temp3Elements.size(); i++) {
			WebElement temp4 = temp3Elements.get(i);
			price += Integer.parseInt(temp4.getText());
		}
		assertThat(driver.findElement(By.xpath("//*[@id=\"totalPrice\"]")).getText().split("\\u20B9")[1].trim())
				.isEqualTo("" + price);

		WebElement moveToInvoice = driver.findElement(By.xpath("//*[@id=\"moveToNext\"]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", moveToInvoice);
		moveToInvoice.click();
		*/
	}

	@Test (priority=2)
	public void invoice() {
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		WebElement menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();
		WebElement menuPacking = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a"));
		menuPacking.click();

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

		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"invoiceTuple\"]"));
		for (int i = 0; i < listTuples.size(); i++) {
			String xpath = "(//*[@id=\"invoiceTuple\"])[" + (i + 1) + "]/td[3]";
			WebElement orderElement = driver.findElement(By.xpath(xpath));
			if (orderElement.getText().equalsIgnoreCase(orderNumber)) {
				assertThat(orderElement.getText()).isEqualToIgnoringCase(orderNumber);
				String xpath1 = "(//*[@id=\"actionBtn\"])[" + (i + 1) + "]";
				driver.findElement(By.xpath(xpath1)).click();
			}
		}

		WebElement moveToDispatch = driver.findElement(By.xpath("//*[@id=\"printAndDispatch\"]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", moveToDispatch);
		moveToDispatch.click();

		WebElement date = driver.findElement(By.xpath("//*[@id=\"datepicker\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", date);

		Select drpVehicle = new Select(driver.findElement(By.className("swal2-select")));
		drpVehicle.selectByVisibleText("KA 01 XE 6692");
		
		driver.findElement(By.cssSelector("button[class='swal2-confirm swal2-styled']")).click();
			
	}
	
	@Test (priority=3)
	public void dispatch() {
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Orders");
		menuInput.sendKeys(Keys.ENTER);
		WebElement menuOrders = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a"));
		menuOrders.click();
		WebElement menuPacking = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[3]/a"));
		menuPacking.click();
		
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
		
		
	}
	
	@BeforeClass
	public void beforeOrderFlow() throws IOException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("http://localhost:4200");
		// driver.get("https://vilcart-buy.web.app");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		Reporter.log(driver.getTitle(), true);
		js = ((JavascriptExecutor) driver);
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		iv = new InventoryChangeStock(driver, js, aw, wait);
		loginObj.login();
	}

	@AfterClass
	public void afterOrderFlow() throws InterruptedException {
		Thread.sleep(10000);
		driver.quit();
	}

}
