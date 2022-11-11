package pom.com.vilcart.pom.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

public class Inventory {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;

	@FindBy(id = "searchInput")
	private WebElement searchInput;

	@FindBy(id = "searchButton")
	private WebElement searchButton;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(xpath = "(//*[@id=\"cacheLabel\"]/div/div/button)[1]")
	private WebElement cacheDropDown;

	@FindBy(xpath = "(//*[@id=\"cacheLabel\"]/div/div/div/button[1])[1]")
	private WebElement fetchLiveData;

	@FindBy(xpath = "(//*[@id=\"cacheLabel\"]/div/div/div/button[2])[1]")
	private WebElement saveCache;

	@FindBy(xpath = "//*[@id=\"skuTuple\"]")
	List<WebElement> skuTuples;

	@FindBy(xpath = "//*[@id=\"stock\"]")
	private WebElement stockInput;

	@FindBy(xpath = "//*[@id=\"datepicker\"]")
	private WebElement datepicker;

	@FindBy(xpath = "//button[normalize-space(.)='Submit'][@class='swal2-confirm btn btn-primary']")
	private WebElement submitStock;

	public Inventory(WebDriver driver, JavascriptExecutor js, AngularWait aw, WebDriverWait wait) {
		this.driver = driver;
		this.js = js;
		this.aw = aw;
		this.wait = wait;
		PageFactory.initElements(driver, this);
	}

	private void fetchLiveDataInInventory() throws InterruptedException {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", cacheDropDown);
		cacheDropDown.click();
		fetchLiveData.click();
		aw.waitAllRequest();
	}

	private void saveCacheInInventory() throws InterruptedException {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", cacheDropDown);
		cacheDropDown.click();
		saveCache.click();
		aw.waitAllRequest();
	}

	public void searchInInventory(String sku) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(sku);
		searchButton.click();
		aw.waitAllRequest();
	}

	private void getTuplesForCurrentDate() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				startDate);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				endDate);
		submit.click();
		aw.waitAllRequest();
	}

	private String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}

	public void updateStock(String sku, int count) throws InterruptedException {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		getTuplesForCurrentDate();

		fetchLiveDataInInventory();

		Thread.sleep(3000);
		// js.executeScript("arguments[0].click();arguments[0].value='" + sku
		// + "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', {
		// bubbles: true }));arguments[0].dispatchEvent(new Event('keyup', { bubbles:
		// true }));", search);
		searchInInventory(sku);

		if (skuTuples.size() == 0) {
			assertThat(false).withFailMessage("No sku with name:\'" + sku + "\' in search").isEqualTo(true);
		}

		boolean contains = false;
		for (int i = 0; i < skuTuples.size(); i++) {
			String xp = "//td[5]";
			WebElement name = skuTuples.get(i).findElement(By.xpath(xp));
			Reporter.log(name.getAccessibleName(), true);
			Reporter.log(name.getText(), true);
			assertThat(name.getText().toLowerCase()).containsIgnoringCase(sku);
			if (name.getText().equalsIgnoreCase(sku)) {
				contains = true;
				String xpath = "//td[7]";
				WebElement varSku = skuTuples.get(i).findElement(By.xpath(xpath));
				varSku.click();

				js.executeScript("arguments[0].value = '" + Integer.toString(count)
						+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", stockInput);
				Reporter.log("stockInput: " + stockInput.getText(), true);

				js.executeScript("arguments[0].value = '" + getDate()
						+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", datepicker);

				submitStock.click();
				aw.waitAllRequest();
				Thread.sleep(3000);
			}
		}
		if (contains == false) {
			assertThat(false).withFailMessage("SKU is not present with name:" + sku).isEqualTo(true);
		}
		saveCacheInInventory();
	}

	public int getInventoryGoodsInCount(String skuName) {
		searchInInventory(skuName);

		if (skuTuples.size() == 0) {
			assertThat(false).withFailMessage("No sku with name:\'" + skuName + "\' in search").isEqualTo(true);
		}
		boolean contains = false;
		for (int i = 0; i < skuTuples.size(); i++) {
			String xp = ".//td[5]";
			WebElement name = skuTuples.get(i).findElement(By.xpath(xp));
			Reporter.log(name.getAccessibleName(), true);
			Reporter.log(name.getText(), true);
			assertThat(name.getText().toLowerCase()).containsIgnoringCase(skuName);
			if (name.getText().equalsIgnoreCase(skuName)) {
				contains = true;
				String xpath = ".//td[15]";
				WebElement GoodsInCount = skuTuples.get(i).findElement(By.xpath(xpath));
				return Integer.parseInt(GoodsInCount.getText().trim());
			}
		}
		if (contains == false) {
			assertThat(false).withFailMessage("SKU is not present with name:" + skuName).isEqualTo(true);
		}
		// It doesn't come here. assert will fail before reaching here.
		return 0;
	}

	public int getInventoryGoodsOutCount(String skuName) {
		searchInInventory(skuName);

		if (skuTuples.size() == 0) {
			assertThat(false).withFailMessage("No sku with name:\'" + skuName + "\' in search").isEqualTo(true);
		}
		boolean contains = false;
		for (int i = 0; i < skuTuples.size(); i++) {
			String xp = "//td[5]";
			WebElement name = skuTuples.get(i).findElement(By.xpath(xp));
			Reporter.log(name.getAccessibleName(), true);
			Reporter.log(name.getText(), true);
			assertThat(name.getText().toLowerCase()).containsIgnoringCase(skuName);
			if (name.getText().equalsIgnoreCase(skuName)) {
				contains = true;
				String xpath = ".//td[14]";
				WebElement GoodsInCount = skuTuples.get(i).findElement(By.xpath(xpath));
				return Integer.parseInt(GoodsInCount.getText().trim());
			}
		}
		if (contains == false) {
			assertThat(false).withFailMessage("SKU is not present with name:" + skuName).isEqualTo(true);
		}
		// It doesn't come here. assert will fail before reaching here.
		return 0;
	}
}
