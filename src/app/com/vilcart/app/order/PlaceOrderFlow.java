package app.com.vilcart.app.order;

import org.testng.annotations.Test;

import pom.com.vilcart.pom.login.Login;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class PlaceOrderFlow {

	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
	private Login loginObj;

	@Test
	public void placeOrder() throws IOException {
		Reporter.log(CurrentMethod.methodName()+" "+TimeStamp.CurTimeStamp(), true);

		loginObj.login();

		WebElement placeOrder = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[11]/a/span"));// *[@id="main-menu-navigation"]/li[11]/a/span
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", placeOrder);
		placeOrder.click();

		WebElement searchCustomer = driver.findElement(By.xpath("//*[@id=\"iconLeft1\"]"));
		searchCustomer.sendKeys("test");
		aw.waitAllRequest();
		WebElement testCustomer = driver.findElement(By.xpath("//*[@id=\"currCustomer\"]/h2"));
		testCustomer.click();
		WebElement searchItem = driver.findElement(By.xpath("//*[@id=\"itemName\"]"));
		searchItem.sendKeys("208 test sku");

		List<WebElement> liList = driver.findElements(By.id("liList"));// *[@id="liList"]
		for (int i1 = 0; i1 < liList.size() && i1 < 2; i1++) {
			WebElement liListVar = liList.get(i1);
			String xpath = "//*[@id=\'liList\'][" + (i1 + 1) + "]//following-sibling::span";
			// List<WebElement> temp =
			// driver.findElements(By.xpath("//*[@id=\"liList\"][1]//following-sibling::span"));
			List<WebElement> temp = driver.findElements(By.xpath(xpath));
			List<WebElement> addToCartButton = driver.findElements(By.id("addToCartList"));
			for (int i2 = 0; i2 < temp.size(); i2++) {
				temp.get(i2).getText();
				Reporter.log(temp.get(i2).getText(), true);
				temp.get(i2).click();
				addToCartButton.get(i1).click();
			}
			List<WebElement> itemNameList = driver.findElements(By.id("itemNameList"));
			Reporter.log("" + itemNameList.get(i1).getText(), true);

		}
		WebElement placeOrderButton = driver.findElement(By.xpath("//*[@id=\"placeOrderButton\"]"));
		placeOrderButton.click();
		WebElement remarksInput = driver.findElement(By.id("swal2-input"));
		remarksInput.sendKeys("placing order");
		// WebElement buyAllButton =
		// driver.findElement(By.xpath("/html/body/div/div/div[6]/button[3]"));
		WebElement buyAllButton = driver.findElement(By.className("swal2-confirm"));
		buyAllButton.click();

	}

	@BeforeClass
	public void beforePlaceOrder() {
		Reporter.log(CurrentMethod.methodName()+" "+TimeStamp.CurTimeStamp(), true);
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
	}

	@AfterClass
	public void afterPlaceOrder() throws InterruptedException {
		Reporter.log(CurrentMethod.methodName()+" "+TimeStamp.CurTimeStamp(), true);
		Thread.sleep(3000);
		driver.quit();
	}

}