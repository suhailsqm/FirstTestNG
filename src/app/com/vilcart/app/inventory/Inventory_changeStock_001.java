package app.com.vilcart.app.inventory;

import org.testng.annotations.Test;

import pom.com.vilcart.pom.inventory.Inventory;
import pom.com.vilcart.pom.login.Login;
import pom.com.vilcart.pom.menu.Menu;
import pom.com.vilcart.pom.sku.Sku;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.BaseSuiteMethods;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class Inventory_changeStock_001 extends BaseSuiteMethods {
	private AngularWait aw;
	private Login loginObj;
	private Menu menu;
	private Sku sku;
	private String skuName;
	private Inventory inventory;
	private WebDriverWait wait;

	@Test(priority = 1)
	public void createSku() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		sku = new Sku(driver, aw);
		menu.goToSKU();
		skuName = sku.createSku();
	}

	@Test(priority = 2, dependsOnMethods = { "createSku" })
	public void skuInList() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.skuList(skuName);
	}

	@Test(priority = 3, dependsOnMethods = { "skuInList" })
	public void inventoryChangeStock() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToInventory();
		inventory.updateStock(skuName, 10);
	}

	@Test(priority = 4, dependsOnMethods = { "inventoryChangeStock" })
	public void inventoryVerifyChangeStock() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToInventory();
		if (10 == inventory.verifyOpeningStock(skuName)) {
			Reporter.log("Verified Opening Stock", true);
		} else {
			assertThat(false).withFailMessage("Opening stock not verified for sku with name:\'" + skuName + "\'.")
					.isEqualTo(true);
		}
		if (10 == inventory.verifyClosingBalance(skuName)) {
			Reporter.log("Verified Closing Stock", true);
		} else {
			assertThat(false).withFailMessage("Closing Balance not verified for sku with name:\'" + skuName + "\'.")
					.isEqualTo(true);
		}
	}

	@Test(priority = 5, dependsOnMethods = { "skuInList" })
	public void deleteSku() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.deleteFirstSku(skuName);
	}

	@BeforeClass
	public void beforeClass(ITestContext context) throws IOException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		ISuite suite = context.getSuite();
		driver = (WebDriver) suite.getAttribute("WebDriver");
		Reporter.log(suite.getAttribute("WebDriver") + "", true);
		driver.get(ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.deployed.url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		menu = new Menu(driver, aw);
		loginObj.login();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		inventory = new Inventory(driver, (JavascriptExecutor) driver, aw, wait);
	}

	@AfterClass
	public void afterClass() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		loginObj.logout();
	}

}
