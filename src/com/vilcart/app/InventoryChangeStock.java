package com.vilcart.app;

import java.time.ZoneId;
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

import com.vilcart.util.AngularWait;

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
	private void fetchLiveData() {
		WebElement temp = driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", temp);
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]")).click();
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/div/button[1])[1]")).click();
		aw.waitAllRequest();
	}
	private void saveCache() {
		WebElement temp = driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]"));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", temp);
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/button)[1]")).click();
		driver.findElement(By.xpath("(//*[@id=\"cacheLabel\"]/div/div/div/button[2])[1]")).click();
		aw.waitAllRequest();
	}
	public void updateStock(String sku,int count) throws InterruptedException {
		Reporter.log("sku",true);
		WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
		menuInput.clear();
		menuInput.sendKeys("Inventory");
		menuInput.sendKeys(Keys.ENTER);
		WebElement menuInventory = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a/span"));
		menuInventory.click();

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
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btn.click();
		aw.waitAllRequest();
		Reporter.log("sku1",true);
		fetchLiveData();
		
		WebElement search = driver.findElement(By.xpath("//*[@id=\"demo-2\"]/input"));// *[@id="demo-2"]/input
		search.click();
		search.clear();
		search.sendKeys(sku);
		search.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		Reporter.log("sku2",true);
		List<WebElement> listTuples = driver.findElements(By.xpath("//*[@id=\"skuTuple\"]"));
		if(listTuples.size() == 0) {
			Reporter.log("No sku with name:\'"+sku+"\'",true);
			return;
		}
		for(int i=0; i<listTuples.size()&& i<1;i++) {
			String xpath = "//*[@id=\"skuTuple\"]["+(i+1)+"]/td[7]";
			WebElement varSku = driver.findElement(By.xpath(xpath));
			varSku.click();
			WebElement stockInput = driver.findElement(By.xpath("//*[@id=\"stock\"]"));
			js.executeScript("arguments[0].value = '"+Integer.toString(count)+"';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",stockInput);
			WebElement date = driver.findElement(By.xpath("//*[@id=\"datepicker\"]"));
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			js.executeScript("arguments[0].value = '"+dtf1.format(indiaDateTime)+"';arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",date);
			Reporter.log(""+dtf1.format(indiaDateTime),true);
			Reporter.log("date: "+date.getText(), true);
			Thread.sleep(20000);
			driver.findElement(By.xpath("//button[normalize-space(.)='Submit'][@class='swal2-confirm btn btn-primary']")).click();
			aw.waitAllRequest();
			Thread.sleep(20000);
		}
		saveCache();
		Reporter.log("sku3",true);
	}
}
