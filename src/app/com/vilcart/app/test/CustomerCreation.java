package app.com.vilcart.app.test;

import org.testng.annotations.Test;

//import com.paulhammant.ngwebdriver.NgWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.BeforeClass;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import util.com.vilcart.util.*;

import static java.lang.invoke.MethodHandles.lookup;
//import static org.slf4j.LoggerFactory.getLogger;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.poi.ss.usermodel.Cell.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.slf4j.Logger;
import org.openqa.selenium.interactions.Actions;
import util.com.vilcart.util.*;

public class CustomerCreation {

	private WebDriver driver;
	// NgWebDriver ngWebDriver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
	private Login loginObj;
	private long number;

	@Test(priority = 1)
	public void CustomerCreation() throws IOException, InterruptedException {

		loginObj.login();

		driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[2]/a/span")).click();

		driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[2]/ul/li[1]/a")).click();

		aw.waitAllRequest();

		WebElement state = driver.findElement(By.xpath("//*[@id=\"customerState\"]/div/div/div[3]/input"));// *[@id="customerState"]/div/div/div[3]/input
		js.executeScript(
				"arguments[0].value='KARNATAKA';arguments[0].click();arguments[0].dispatchEvent(new Event('change'))",
				state);
		state.getText();
		aw.waitAllRequest();
		WebElement district = driver.findElement(By.xpath("//*[@id=\"customerDistrict\"]/div/div/div[3]/input"));// *[@id="customerDistrict"]/div/div/div[3]/input
		js.executeScript(
				"arguments[0].value='MANDYA';arguments[0].click();arguments[0].dispatchEvent(new Event('change'))",
				district);

		aw.waitAllRequest();
		WebElement taluk = driver.findElement(By.xpath("//*[@id=\"customerTaluk\"]/div/div/div[3]/input"));// *[@id="customerTaluk"]/div/div/div[3]/input
		// js.executeScript("arguments[0].value='Maddur';arguments[0].click();arguments[0].dispatchEvent(new
		// Event('click'))", taluk);
		taluk.click();
		WebElement taluk1 = driver
				.findElement(By.xpath("//*[@id=\"customerTaluk\"]/ng-dropdown-panel/div/div[2]/div[2]/span"));
		aw.waitAllRequest();
		// js.executeScript("arguments[0].click()",taluk1);
		taluk1.click();
		Reporter.log(LineNumber.getLineNumber()+" "+"Maddur", true);
		aw.waitAllRequest();

		WebElement postal = driver.findElement(By.xpath("//*[@id=\"customerPostal\"]/div/div/div[3]/input"));// *[@id="customerPostal"]/div/div/div[3]/input
		// js.executeScript("arguments[0].value='Alur-maddur
		// B.O';arguments[0].click();arguments[0].dispatchEvent(new Event('click'))",
		// postal);
		postal.click();
		WebElement postal1 = driver
				.findElement(By.xpath("//*[@id=\"customerPostal\"]/ng-dropdown-panel/div/div[2]/div[2]/span"));
		aw.waitAllRequest();
		// js.executeScript("arguments[0].click()",taluk1);
		postal1.click();
		// Thread.sleep(2000);
		// js.executeScript("var injector = window.angular.element('body').injector();
		// var $http = injector.get('$http'); return ($http.pendingRequests.length ===
		// 0);");
		Reporter.log(LineNumber.getLineNumber()+" "+"Alur-maddur", true);
		aw.waitAllRequest();
		WebElement village = driver.findElement(By.xpath("//*[@id=\"customerVillage\"]/div/div/div[3]/input"));// *[@id="customerVillage"]/div/div/div[3]/input
		// js.executeScript("arguments[0].value='Alur';arguments[0].click();arguments[0].dispatchEvent(new
		// Event('click'))", village);
		village.click();
		WebElement village1 = driver
				.findElement(By.xpath("//*[@id=\"customerVillage\"]/ng-dropdown-panel/div/div[2]/div[2]/span"));
		aw.waitAllRequest();
		// js.executeScript("arguments[0].click()",taluk1);
		village1.click();
		Reporter.log(LineNumber.getLineNumber()+" "+"Alur", true);
		aw.waitAllRequest();
		WebElement shop = driver.findElement(By.xpath("//*[@id=\"customerShopType\"]/div/div/div[3]/input"));// *[@id="customerShopType"]/div/div/div[3]/input
		js.executeScript(
				"arguments[0].value='shop';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				shop);
		aw.waitAllRequest();
		WebElement shopName = driver.findElement(By.xpath("//*[@id=\"customerShopName\"]"));
		js.executeScript(
				"arguments[0].value='test shop';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				shopName);

		WebElement customerName = driver.findElement(By.xpath("//*[@id=\"customerName\"]"));// *[@id="customerName"]
		js.executeScript(
				"arguments[0].value='test customer';arguments[0].click;arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerName);
		customerName.sendKeys(Keys.ENTER);
		// Reporter.log(LineNumber.getLineNumber()+" "+""+, false)
		// Reporter.log(LineNumber.getLineNumber()+" "+""+customerName.getDomAttribute("selected"),true);
		Reporter.log(LineNumber.getLineNumber()+" " + customerName.isEnabled(), true);
		// Reporter.log(LineNumber.getLineNumber()+" "+customerName.isSelected(),true);

		WebElement customerLocalName = driver.findElement(By.xpath("//*[@id=\"customerLocalName\"]"));
		js.executeScript(
				"arguments[0].value='customer local name';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerLocalName);

		WebElement villageLocalName = driver.findElement(By.xpath("//*[@id=\"customerVillageLocalName\"]"));
		js.executeScript(
				"arguments[0].value='village local name';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				villageLocalName);

		WebElement phoneNumber = driver.findElement(By.xpath("//*[@id=\"customerPhoneNumber\"]"));
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		js.executeScript(
				"arguments[0].value='" + number
						+ "';arguments[0].click;arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				phoneNumber);
		phoneNumber.sendKeys(Keys.ENTER);
		this.number = number;
		Reporter.log(LineNumber.getLineNumber()+" "+number,true);

		WebElement phoneNumber2 = driver.findElement(By.xpath("//*[@id=\"customerPhoneNumber2\"]"));
		js.executeScript(
				"arguments[0].value='1032547698';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				phoneNumber2);

		WebElement customerAddress = driver.findElement(By.xpath("//*[@id=\"customerAddress\"]"));
		js.executeScript(
				"arguments[0].value='main road, alur';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerAddress);

		WebElement landmark = driver.findElement(By.xpath("//*[@id=\"customerLandMark\"]"));
		js.executeScript(
				"arguments[0].value='Near HDFC bank';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				landmark);

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", landmark);

		WebElement customerLeisure = driver.findElement(By.xpath("//*[@id=\"customerLeisure\"]/div/span[2]"));// *[@id="customerLeisure"]/div/div/div[3]/input
		// js.executeScript("arguments[0].click()", customerLeisure);
		customerLeisure.click();

		WebElement customerLeisure3 = driver.findElement(By.xpath("//*[@id=\"customerLeisure\"]/div/div/div[3]/input"));// *[@id="customerLeisure"]/div/div/div[3]/input
		js.executeScript("arguments[0].click();", customerLeisure3);
		aw.waitAllRequest();
		WebElement customerLeisure2 = driver
				.findElement(By.xpath("//*[@id=\"customerLeisure\"]/ng-dropdown-panel/div/div[2]/div[2]/span"));// *[@id="customerLeisure"]/div/div/div[3]/input
		js.executeScript("arguments[0].click()", customerLeisure2);
		aw.waitAllRequest();

		WebElement customerBreakTime = driver.findElement(By.xpath("//*[@id=\"customerBreakTime\"]"));
		js.executeScript(
				"arguments[0].value='afternoon';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerBreakTime);

		WebElement customerHasCooler = driver
				.findElement(By.xpath("//*[@id=\"customerHasCooler\"]/div/div/div[2]/input"));// *[@id="customerHasCooler"]/div/div/div[3]/input
		js.executeScript(
				"arguments[0].value='Yes';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerHasCooler);
		aw.waitAllRequest();
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", customerHasCooler);
		customerHasCooler.click();
		WebElement customerHasCooler1 = driver
				.findElement(By.xpath("//*[@id=\"customerHasCooler\"]/ng-dropdown-panel/div/div[2]/div[1]"));
		customerHasCooler1.click();
		aw.waitAllRequest();
		WebElement customerCoolerType = driver
				.findElement(By.xpath("//*[@id=\"customerCoolerType\"]/div/div/div[2]/input"));
		js.executeScript(
				"arguments[0].value='Commercial';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerCoolerType);

		WebElement customerQualification = driver.findElement(By.xpath("//*[@id=\"customerQualification\"]"));
		js.executeScript(
				"arguments[0].value='SSLC';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerQualification);

		WebElement customerGrade = driver.findElement(By.xpath("//*[@id=\"customerGrade\"]/div/div/div[2]/input"));// *[@id="customerGrade"]/div/div/div[2]/input
		js.executeScript(
				"arguments[0].value='Grade A';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerGrade);

		WebElement customerAvgSale = driver.findElement(By.xpath("//*[@id=\"customerAvgSale\"]"));
		js.executeScript(
				"arguments[0].value='2000';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerAvgSale);

		WebElement customerIsSmartPhoneUser = driver
				.findElement(By.xpath("//*[@id=\"customerIsSmartPhoneUser\"]/div/div/div[3]/input"));
		js.executeScript(
				"arguments[0].value='Yes';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerIsSmartPhoneUser);

		WebElement createCustomerButton = driver.findElement(By.xpath("//*[@id=\"createCustomerButton\"]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", createCustomerButton);
		aw.waitAllRequest();
		Thread.sleep(2000);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		Reporter.log(LineNumber.getLineNumber()+" "+"" + createCustomerButton.isEnabled(), true);
		Reporter.log(LineNumber.getLineNumber()+" " + createCustomerButton.isDisplayed(), true);
		wait.until(ExpectedConditions.elementToBeClickable(createCustomerButton));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", createCustomerButton);
		createCustomerButton.click();

		Thread.sleep(2000);

	}

	@Test(priority = 2)
	public void CustomerDeletion() {
		driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input")).sendKeys("Customer");
		//WebElement menu = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[2]/a/span"));
		aw.waitAllRequest();
		driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")).click();

		WebElement search = driver.findElement(By.xpath("//*[@id=\"searchBtn\"]"));
		Reporter.log(LineNumber.getLineNumber()+" "+number, true);
		search.sendKeys(""+this.number);
		
		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"customerTuple\"]"));

		for (int i = 0; i < listTuples.size() && i<1; i++) {
			WebElement temp = driver.findElement(By.xpath("(//*[@id=\"customerTuple\"])["+i+1+"]/td[3]/span"));//*[@id="customerTuple"]/td[3]/span
			Reporter.log(LineNumber.getLineNumber()+" "+""+temp.getText());
			WebElement temp1 = driver.findElement(By.xpath("(//*[@id=\"customerTuple\"])["+i+1+"]/td[7]"));
			Reporter.log(LineNumber.getLineNumber()+" "+""+temp1.getText());
//    	  WebElement tuple = listTuples.get(i);
			String xpath = "(//*[@id=\'customerTuple\'])[" + (i + 1) + "]/td[10]/div/button[2]/i";
			WebElement btn = driver.findElement(By.xpath(xpath));
			btn.click();
			driver.findElement(By.xpath("(//button[normalize-space()='Delete'])[1]")).click();
			driver.findElement(By.xpath("(//button[normalize-space()='OK'])[1]")).click();
		}

		
		
	}

	@BeforeClass
	public void TestSetup() throws IOException {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		// ngWebDriver = new NgWebDriver((JavascriptExecutor)
		// driver).withRootSelector("\"app-create-customers\"");;
		driver.get(ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.deployed.url"));
		// driver.get("https://vilcart-buy.web.app");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		Reporter.log(LineNumber.getLineNumber()+" "+driver.getTitle(), true);
		js = ((JavascriptExecutor) driver);
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
	}

	@AfterClass
	public void TestTeardown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}

}
