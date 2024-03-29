package app.com.vilcart.app.goods;

import org.testng.annotations.Test;

import pom.com.vilcart.pom.goods.GoodsReceive;
import pom.com.vilcart.pom.inventory.Inventory;
import pom.com.vilcart.pom.login.Login;
import pom.com.vilcart.pom.menu.Menu;
import pom.com.vilcart.pom.sku.Sku;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.BaseSuiteMethods;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

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

public class GoodsReceive_001 extends BaseSuiteMethods {
	private AngularWait aw;
	private Login loginObj;
	private Menu menu;
	private Sku sku;
//	private String skuName;
	private GoodsReceive goodsReceive;
	private Inventory inventory;
	private WebDriverWait wait;

	@DataProvider(name = "excelSkuData")
	public Object[][] getSkuData() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		sku = new Sku(driver, aw, ReadPropertiesFile.readPropertiesFile().getProperty("resources.goodsreceive"));
		sku.getExcelData();
		return sku.data;
	}

	@DataProvider(name = "excelSkuNameData")
	public Object[][] getSkuNameData() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		try {
			if (!sku.dataCollected) {
				assertThat(false).withFailMessage("getSkuData should be called before getSkuNameData").isEqualTo(true);
			}
		} catch (java.lang.NullPointerException exception) {
			sku = new Sku(driver, aw, ReadPropertiesFile.readPropertiesFile().getProperty("resources.goodsreceive"));
			sku.getExcelData();
			if (!sku.dataCollected) {
				assertThat(false).withFailMessage("getSkuData should be called before getSkuNameData").isEqualTo(true);
			}
		}
		return sku.skuNameArray;
	}

	@DataProvider(name = "fetchDataGoodsReceive")
	public Object[][] fetchDataGoodsReceive() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		goodsReceive.grc.fetchData();
		if (!sku.dataCollected) {
			assertThat(false).withFailMessage("getSkuData should be called before getSkuNameData").isEqualTo(true);
		} else {
			goodsReceive.verifyInventoryAfterGoodsReceive(sku.skuVarValueData);
		}
		assertThat(goodsReceive.grc.itemsCount).withFailMessage("No Items for GoodsReceive").isGreaterThan(0);
		assertThat(goodsReceive.grc.skuCount).withFailMessage("No SKU's for GoodsReceive").isGreaterThan(0);
		return goodsReceive.grc.updateStockInventory;
	}

	@DataProvider(name = "verifyInventoryDataGoodsReceive")
	public Object[][] verifyInventoryDataGoodsReceive() {
		return goodsReceive.maps;
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

	@Test(priority = 3, dependsOnMethods = { /* "skuInList" */ }, dataProvider = "fetchDataGoodsReceive")
	public void updateInventory(String skuName, String count) throws NumberFormatException, InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToInventory();
		inventory.updateStock(skuName, Integer.parseInt(count));
	}

	@Test(priority = 4, dependsOnMethods = { "updateInventory",/* "skuInList" */ })
	public void GoodsReceive() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToGoodsReceive();
		goodsReceive.receiveGoods("", "", "");
		menu.goToGoodsReceive();
		goodsReceive.verifyReceiveGoods();
	}

	@Test(priority = 5, dependsOnMethods = { "GoodsReceive" }, dataProvider = "verifyInventoryDataGoodsReceive")
	public void checkGoodsReceiveInventory(String skuName, String count, String initVal)
			throws Exception, InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToInventory();
		double curVal = inventory.getInventoryGoodsOutCount(skuName);
		if (Double.parseDouble(count) == curVal - Double.parseDouble(initVal)) {
			Reporter.log("Verified GoodsOut Stock", true);
		} else {
			assertThat(false)
					.withFailMessage("GoodsOut stock not verified for sku with name:'" + skuName + "' and count '"
							+ count + "' initial value '" + initVal + "' current value '" + curVal + "'.")
					.isEqualTo(true);
		}
	}

	@Test(priority = 6, dependsOnMethods = { /* "skuInList" */ }, dataProvider = "excelSkuNameData")
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
		wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		menu = new Menu(driver, aw);
		inventory = new Inventory(driver, (JavascriptExecutor) driver, aw, wait);
		loginObj.login();
		goodsReceive = new GoodsReceive(driver, aw,
				ReadPropertiesFile.readPropertiesFile().getProperty("resources.goodsreceive"), inventory, menu);
	}

	@AfterClass
	public void afterClass() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		loginObj.logout();
	}

}
