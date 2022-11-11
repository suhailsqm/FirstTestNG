package app.com.vilcart.app.goods;

import org.testng.annotations.Test;

import pom.com.vilcart.pom.goods.GoodsTransfer;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class GoodsTransfer_001 extends BaseSuiteMethods {
	private AngularWait aw;
	private Login loginObj;
	private Menu menu;
	private Sku sku;
//	private String skuName;
	private GoodsTransfer goodsTransfer;
	private Inventory inventory;
	private WebDriverWait wait;

	@DataProvider(name = "excelSkuData")
	public Object[][] getSkuData() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		sku = new Sku(driver, aw, ReadPropertiesFile.readPropertiesFile().getProperty("resources.sku"));
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
	public void createSku(String... data) {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.createSku(data);

	}

	@Test(priority = 2, dependsOnMethods = { "createSku" }, dataProvider = "excelSkuNameData")
	public void skuInList(String skuName) {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.skuList(skuName);
	}

	@Test(priority = 3, dependsOnMethods = { "skuInList" })
	public void GoodsTransfer() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToGoodsTransfer();
		goodsTransfer.transferGoods("", "");
	}

	@Test(priority = 4, dependsOnMethods = { "GoodsTransfer" })
	public void checkGoodsTransferInventory() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToInventory();
		;
//		inventory.getInventoryGoodsOutCount(skuName);
	}

	@Test(priority = 5, dependsOnMethods = { "skuInList" }, dataProvider = "excelSkuNameData")
	public void deleteSku(String skuName) {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToSKU();
		sku.deleteFirstSku(skuName);
	}

	@BeforeClass
	public void beforeClass(ITestContext context) throws IOException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		driver = (WebDriver) context.getAttribute("WebDriver");
		driver.get(ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.deployed.url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		menu = new Menu(driver, aw);
		inventory = new Inventory(driver, (JavascriptExecutor) driver, aw, wait);
		loginObj.login();
	}

	@AfterClass
	public void afterClass() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		loginObj.logout();
	}

}
