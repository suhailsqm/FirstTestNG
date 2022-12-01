package app.com.vilcart.app.sku;



import pom.com.vilcart.pom.menu.Menu;
import pom.com.vilcart.pom.sku.Sku;
import pom.com.vilcart.pom.login.Login;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.BaseSuiteMethods;
import util.com.vilcart.util.CurrentMethod;
//import util.com.vilcart.util.Login;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class Sku_001 extends BaseSuiteMethods {
	private AngularWait aw;
	private Login loginObj;
	private Menu menu;
	private Sku sku;
	private String skuName;

	@Test
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
		Reporter.log(suite.getAttribute("WebDriver")+"", true);
		driver.get(ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.deployed.url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		menu = new Menu(driver, aw);
		loginObj.login();
	}

	@AfterClass
	public void afterClass() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		loginObj.logout();
	}

}
