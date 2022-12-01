package app.com.vilcart.app.placeorder;

import org.testng.annotations.Test;

import pom.com.vilcart.pom.customer.CustomerList;
import pom.com.vilcart.pom.customer.NewCustomer;
import pom.com.vilcart.pom.menu.Menu;
import pom.com.vilcart.pom.placeorder.PlaceOrder;
import pom.com.vilcart.pom.sku.Sku;
import pom.com.vilcart.pom.login.Login;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.BaseSuiteMethods;
import util.com.vilcart.util.CurrentMethod;
//import util.com.vilcart.util.Login;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.Keys;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class PlaceOrder_001 extends BaseSuiteMethods {
	private AngularWait aw;
	private Login loginObj;
	private NewCustomer newCustomer;
	private CustomerList customerList;
	private Menu menu;
	private Sku sku;
	private PlaceOrder placeOrder;
	private String phoneNumber;

	@DataProvider(name = "excelSkuData")
	public Object[][] getSkuData() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		sku = new Sku(driver, aw, ReadPropertiesFile.readPropertiesFile().getProperty("resources.PlaceOrder"));
		sku.getExcelData();
		return sku.data;
	}

	@DataProvider(name = "excelSkuNameData")
	public Object[][] getSkuNameData() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		if (!sku.dataCollected) {
			assertThat(false).withFailMessage("getSkuData should be called before getSkuNameData").isEqualTo(true);
		}
		return sku.skuNameArray;
	}

	@Test(priority = 1, dataProvider = "excelSkuData")
	public void createSku(String length, String... data) {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.createSku(length, data);
	}

	@Test(priority = 2, dependsOnMethods = { "createSku" }, dataProvider = "excelSkuNameData")
	public void skuInList(String skuName) {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.skuList(skuName);
	}

	@Test(priority = 3)
	public void createCustomerFlow() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		newCustomer = new NewCustomer(driver, aw,
				ReadPropertiesFile.readPropertiesFile().getProperty("resources.PlaceOrder"));
//		menu.goToNewCustomer();
		menu.menuSearch.clear();
		menu.menuSearch.sendKeys("Customer");
		menu.menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		menu.customerMenu.click();
		aw.waitAllRequest();
		menu.newCustomer.click();
		aw.waitAllRequest();
		phoneNumber = newCustomer.createCustomer();
	}

	@Test(priority = 4, dependsOnMethods = { "createCustomerFlow" })
	public void verifyCustomerListFlow() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
//		menu.goToCustomerList();
		menu.menuSearch.clear();
		menu.menuSearch.sendKeys("Customer");
		menu.menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		menu.customerList.click();
		aw.waitAllRequest();
		customerList.verifyCustomer(phoneNumber);
	}

	@Test(priority = 5, dependsOnMethods = { "verifyCustomerListFlow" })
	public void placeOrderFlow() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToPlaceOrder();
		placeOrder.placeOrderFromFile(phoneNumber);
	}

	@Test(priority = 6, dependsOnMethods = { "verifyCustomerListFlow" })
	public void deleteCustomerFlow() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
//		menu.goToCustomerList();
		menu.menuSearch.clear();
		menu.menuSearch.sendKeys("Customer");
		menu.menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		menu.customerMenu.click();
		aw.waitAllRequest();
		menu.customerList.click();
		aw.waitAllRequest();
		customerList.deleteCustomer(phoneNumber);
	}

	@Test(priority = 7, dependsOnMethods = { "skuInList" }, dataProvider = "excelSkuNameData")
	public void deleteSku(String skuName) {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.deleteFirstSku(skuName);
	}

	@BeforeClass
	public void beforeClass(ITestContext context) throws IOException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		Reporter.log("context driver: " + context.getAttribute("WebDriver") + "", true);
		driver.get(ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.deployed.url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);

		customerList = new CustomerList(driver, aw);
		loginObj = new Login(driver, aw);
		menu = new Menu(driver, aw);
		placeOrder = new PlaceOrder(driver, aw);
		loginObj.login();
	}

	@AfterClass
	public void afterClass() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		loginObj.logout();
	}

}
