package app.com.vilcart.app.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pom.com.vilcart.pom.customer.CustomerList;
import pom.com.vilcart.pom.customer.NewCustomer;
import pom.com.vilcart.pom.goods.GoodsReceive;
import pom.com.vilcart.pom.inventory.Inventory;
import pom.com.vilcart.pom.login.Login;
import pom.com.vilcart.pom.menu.Menu;
import pom.com.vilcart.pom.order.Complete;
import pom.com.vilcart.pom.order.Delivery;
import pom.com.vilcart.pom.order.Dispatch;
import pom.com.vilcart.pom.order.Invoice;
import pom.com.vilcart.pom.order.Packing;
import pom.com.vilcart.pom.placeorder.PlaceOrder;
import pom.com.vilcart.pom.sku.Sku;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.BaseSuiteMethods;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

public class Order_001 extends BaseSuiteMethods {
	private AngularWait aw;
	private Login loginObj;
	private Menu menu;
	private Sku sku;
	private NewCustomer newCustomer;
	private CustomerList customerList;
	private PlaceOrder placeOrder;
	private String phoneNumber;
	private String orderNumber;
	private String invoiceNumber;

	@DataProvider(name = "excelSkuData")
	public Object[][] getSkuData() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		sku = new Sku(driver, aw, ReadPropertiesFile.readPropertiesFile().getProperty("resources.Order"));
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
//		placeOrder.placeOrderFromFile("2708220399");
		placeOrder.placeOrderFromFile(phoneNumber);
		orderNumber = placeOrder.orderNumber;
	}

	@Test(priority = 6, dependsOnMethods = { "placeOrderFlow" })
	public void packing() throws IOException, InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToPacking();
		Packing packing = new Packing(driver, aw);
		packing.evaluateFirstTuple(orderNumber);
	}

	@Test(priority = 7, dependsOnMethods = { "packing" })
	public void invoice() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.menuSearch.clear();
		menu.menuSearch.sendKeys("Orders");
		menu.menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		menu.ordersMenu.click();
		aw.waitAllRequest();
		menu.invoice.click();
		Invoice invoice = new Invoice(driver, aw);
		invoice.evaluateOrderInInvoice(orderNumber);
	}

	@Test(priority = 8, dependsOnMethods = { "invoice" })
	public void dispatch() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.menuSearch.clear();
		menu.menuSearch.sendKeys("Orders");
		menu.menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		menu.ordersMenu.click();
		aw.waitAllRequest();
		menu.dispatch.click();
		Dispatch dispatch = new Dispatch(driver, aw);
		invoiceNumber = dispatch.evaluateOrderInDispatch(orderNumber, "KA 02 EA 3344");
	}

	@Test(priority = 9, dependsOnMethods = { "dispatch" })
	public void delivery() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.menuSearch.clear();
		menu.menuSearch.sendKeys("Orders");
		menu.menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
//		menu.ordersMenu.click();
//		aw.waitAllRequest();
		menu.delivery.click();
		aw.waitAllRequest();
		Delivery delivery = new Delivery(driver, aw);
		delivery.evaluateOrderInDelivery(invoiceNumber);
	}

	@Test(priority = 10, dependsOnMethods = { "delivery" })
	public void complete() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.menuSearch.clear();
		menu.menuSearch.sendKeys("Orders");
		menu.menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
//		menu.ordersMenu.click();
//		aw.waitAllRequest();
		menu.complete.click();
		aw.waitAllRequest();
		Complete complete = new Complete(driver, aw);
		complete.evaluateOrderInComplete(orderNumber);
	}

	@Test(priority = 11, dependsOnMethods = { "verifyCustomerListFlow" })
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

	@Test(priority = 12, dependsOnMethods = { "skuInList" }, dataProvider = "excelSkuNameData")
	public void deleteSku(String skuName) {
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
//		wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		menu = new Menu(driver, aw);
		customerList = new CustomerList(driver, aw);
		placeOrder = new PlaceOrder(driver, aw, ReadPropertiesFile.readPropertiesFile().getProperty("resources.Order"));
		loginObj.login();
	}

	@AfterClass
	public void afterClass() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		loginObj.logout();
	}

}
