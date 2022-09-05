package com.vilcart.app;

import org.testng.annotations.Test;

import com.vilcart.util.AngularWait;
import com.vilcart.util.Login;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeClass;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class PurchaseFlow {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
    private Login loginObj;
    
  @Test
  public void purchaseFlow() throws IOException {

	 loginObj.login();
      
     WebElement menuPurchase = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[8]/a/span"));
     js.executeScript("arguments[0].scrollIntoViewIfNeeded();", menuPurchase);
     menuPurchase.click();
     WebElement menuRequestItem = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[8]/ul/li[5]/a"));
     js.executeScript("arguments[0].scrollIntoViewIfNeeded();", menuRequestItem);
     menuRequestItem.click();
     aw.waitAllRequest();
     
     WebElement itemSelectInput = driver.findElement(By.xpath("//*[@id=\"iconLeft1\"]"));
     //itemSelectInput.sendKeys("test");
     //itemSelectInput.sendKeys(Keys.ENTER);
     js.executeScript("arguments[0].value='208';arguments[0].click();arguments[0].dispatchEvent(new Event('input',{ bubbles : true}));arguments[0].dispatchEvent(new Event('keyup', { bubbles: true }))", itemSelectInput);
     aw.waitAllRequest();
     
     List<WebElement> tup = driver.findElements(By.xpath("//*[@id=\"tupleRow\"]"));
     int numberOfTuples = 0;
     if(tup.size()<2) {
    	 numberOfTuples = 1;
     } 
     else {
    	 numberOfTuples = 2;
     }
     for(int i=0;i<numberOfTuples;i++) {
    	 String xpath = "//*[@id=\"tupleRow\"]["+(i+1)+"]/td/div/div[1]/div[2]/div/button";
    	 WebElement list = driver.findElement(By.xpath(xpath));
    	 Reporter.log("1 "+list.getText(),true);
    	 list.click();
    	 String xpath1 = "//*[@id=\"tupleRow\"]["+(i+1)+"]/td/div/div[1]/div[2]/div/div/button";
    	 List<WebElement> listSelectDropDown = driver.findElements(By.xpath(xpath1));
    	 int numOfVariations =listSelectDropDown.size();
    	 Reporter.log(xpath1+" "+numOfVariations, true);
    	 for(int i1=0;i1<listSelectDropDown.size();i1++) {
        	 WebElement varSelectDropDown = listSelectDropDown.get(i1);
        	 Reporter.log(varSelectDropDown.getText(),true);
        	 varSelectDropDown.click();
        	 String xpath2 = "//input[@id=\"countNumber\"]";//["+(i+1)+"]";
             List<WebElement> inputCount = driver.findElements(By.xpath(xpath2));
             Reporter.log(xpath2+" "+inputCount.size(), true);
             WebElement varInputCount = inputCount.get(i);
             varInputCount.clear();
             varInputCount.sendKeys("20");
             String xpath3 = "//*[@id=\"addToCartButton\"]";//["+(i+1)+"]";
             List<WebElement> addToCart = driver.findElements(By.xpath(xpath3));
             Reporter.log(xpath3+" "+addToCart.size(), true);
             WebElement varAddToCart = addToCart.get(i);
             varAddToCart.click();
             
             if(i1 != numOfVariations-1) {
            	 driver.findElement(By.xpath(xpath)).click();
             }
             
         }
     }

     
     WebElement continueButton = driver.findElement(By.xpath("//button[text()='Continue']"));
     js.executeScript("arguments[0].scrollIntoViewIfNeeded();", continueButton);
     try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     continueButton.click();
     WebElement orderAllButton = driver.findElement(By.xpath("//button[text()='Order All']"));
     orderAllButton.click();
     aw.waitAllRequest();
     
     /*
     WebElement scrollBar = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[3]/div"));//*[@id="main-menu-content"]/div[3]/div
     String sj = "arguments[0].setAttribute('style','top: 124px; height: 172px');";
     js.executeScript(sj, scrollBar);
     */
     WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
     menuInput.sendKeys("Purchase");
     menuInput.sendKeys(Keys.ENTER);
     WebElement menuPurchase1 = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a/span"));//*[@id="main-menu-navigation"]/li/a/span
     js.executeScript("arguments[0].click();", menuPurchase1);
     aw.waitAllRequest();
     
     WebElement menuPORequest = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/ul/li[4]/a"));
     js.executeScript("arguments[0].click();", menuPORequest);
     aw.waitAllRequest();
     
     
  }
  @BeforeClass
  public void beforePurchaseFlow() {
	WebDriverManager.chromedriver().setup();
  	driver = new ChromeDriver();
  	driver.get("http://localhost:4200");
  	//driver.get("https://vilcart-buy.web.app");
  	driver.manage().window().maximize(); 
  	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
  	Reporter.log(driver.getTitle(), true);
  	js=((JavascriptExecutor) driver);
  	wait = new WebDriverWait(driver, Duration.ofSeconds(20));
  	aw = new AngularWait(driver);
  	loginObj = new Login(driver,aw);
  }

  @AfterClass
  public void afterPurchaseFlow() throws InterruptedException {
	  	Thread.sleep(3000);
	  	driver.quit();
  }

}
