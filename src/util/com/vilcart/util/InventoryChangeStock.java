package util.com.vilcart.util;

import java.time.ZoneId;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;

public class InventoryChangeStock {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;

	public InventoryChangeStock(WebDriver driver, JavascriptExecutor js, AngularWait aw, WebDriverWait wait) {
		this.driver = driver;
		this.js = js;
		this.aw = aw;
		this.wait = wait;
	}

	private void fetchLiveData() throws InterruptedException {
		WebElement temp = driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", temp);
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]")).click();
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/div/button[1])[1]")).click();
		aw.waitAllRequest();
	}

	private void saveCache() throws InterruptedException {
		WebElement temp = driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", temp);
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]")).click();
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/div/button[2])[1]")).click();
		aw.waitAllRequest();
	}

	public void updateStock(String sku, int count) throws InterruptedException {

		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Inventory");
		menuInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();

		WebElement menuInventory = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a/span"));
		menuInventory.click();
		aw.waitAllRequest();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		WebElement startDate = driver.findElement(By.xpath("//*[@id=\"startDate\"]"));// *[@id="startDate"]
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", startDate);
		WebElement endDate = driver.findElement(By.xpath("//*[@id=\"endDate\"]"));
		js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
				+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", endDate);
		WebElement btn = driver.findElement(By.xpath("//button[normalize-space()='Submit']"));
		btn.click();
		aw.waitAllRequest();
		fetchLiveData();
		
		Thread.sleep(3000);
		WebElement search = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
		//js.executeScript("arguments[0].click();arguments[0].value='" + sku
			//	+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }));arguments[0].dispatchEvent(new Event('keyup', { bubbles: true }));", search);
		search.clear();
		search.sendKeys(sku);
		WebElement searchBtn = driver.findElement(By.xpath("//*[@id=\"searchButton\"]"));
		searchBtn.click();
		aw.waitAllRequest();

		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"skuTuple\"]"));
		if (listTuples.size() == 0) {
			Reporter.log("No sku with name:\'" + sku + "\' in search", true);
			return;
		}
		boolean contains = false;
		for (int i = 0; i < listTuples.size(); i++) {
			String xp = "//*[@id=\"skuTuple\"][" + (i + 1) + "]/td[5]";
			WebElement name = driver.findElement(By.xpath(xp));
			Reporter.log(name.getAccessibleName(), true);
			Reporter.log(name.getText(), true);
			assertThat(name.getText().toLowerCase()).containsIgnoringCase(sku);
			if (name.getText().equalsIgnoreCase(sku)) {
				contains = true;
				String xpath = "//*[@id=\"skuTuple\"][" + (i + 1) + "]/td[7]";
				WebElement varSku = driver.findElement(By.xpath(xpath));
				varSku.click();
				WebElement stockInput = driver.findElement(By.xpath("//*[@id=\"stock\"]"));
				js.executeScript("arguments[0].value = '" + Integer.toString(count)
						+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", stockInput);

				Reporter.log("stockInput: " + stockInput.getText(), true);
				WebElement date = driver.findElement(By.xpath("//*[@id=\"datepicker\"]"));
				js.executeScript("arguments[0].value = '" + dtf.format(indiaDateTime)
						+ "';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", date);

				driver.findElement(
						By.xpath("//button[normalize-space(.)='Submit'][@class='swal2-confirm btn btn-primary']"))
						.click();
				aw.waitAllRequest();
				Thread.sleep(3000);
			}
		}
		if(contains == false) {
			System.out.println("SKU is not present with name:"+sku);
			return;
		}
		saveCache();
	}
}
