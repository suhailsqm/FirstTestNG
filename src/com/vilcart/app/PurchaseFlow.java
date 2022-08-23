package com.vilcart.app;

import org.testng.annotations.Test;

import com.vilcart.util.AngularWait;

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
    WebDriver driver;
    JavascriptExecutor js;
    AngularWait aw;
    WebDriverWait wait;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFCell cell;
    
  @Test
  public void purchaseFlow() throws IOException {
	  File src=new File("TestData.xlsx");
	  FileInputStream finput = new FileInputStream(src);
	  workbook = new XSSFWorkbook(finput);
      DataFormatter formatter = new DataFormatter();
      sheet= workbook.getSheetAt(0);
      for(int i=1; i<=sheet.getLastRowNum(); i++)
      {
          // Import data for Email.
          cell = sheet.getRow(i).getCell(2);
          String value = formatter.formatCellValue(cell);
          driver.findElement(By.cssSelector("input[ng-reflect-name=email]")).sendKeys(value);
           
          // Import data for password.
          cell = sheet.getRow(i).getCell(3);
          value = formatter.formatCellValue(cell);
          driver.findElement(By.cssSelector("input[ng-reflect-name=password]")).sendKeys(value);
          
          driver.findElement(By.tagName("button")).click();
          driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
          driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
          driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));          
          Reporter.log(driver.getTitle(), true);
          
          aw.waitAllRequest();
          assertThat(driver.getTitle()).containsIgnoringCase("Home - VILCART");
      }
      finput.close();
      
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
     js.executeScript("arguments[0].value='test';arguments[0].click();arguments[0].dispatchEvent(new Event('input',{ bubbles : true}));arguments[0].dispatchEvent(new Event('keyup', { bubbles: true }))", itemSelectInput);
     aw.waitAllRequest();

     List<WebElement> listSelectDropDown = driver.findElements(By.xpath("//*[@id=\"dropdownBasic1\"]"));//*[@id="dropdownBasic1"]
     for(int i1=0;i1<listSelectDropDown.size()&&i1<1;i1++) {
    	 WebElement varSelectDropDown = listSelectDropDown.get(i1);
    	 Reporter.log(varSelectDropDown.getText(),true);
    	 varSelectDropDown.click();
    	 List<WebElement> listDropDown = driver.findElements(By.xpath("//*[@id=\"listDropdownBasic1\"]//child::button"));
    	 for(int j1=0;j1<listDropDown.size()&&j1<1;j1++) {
    		WebElement varListDropDown = listDropDown.get(j1);
    		Reporter.log(varListDropDown.getText(),true);
    		varListDropDown.click();
    	 }	
     }
     List<WebElement> inputCount = driver.findElements(By.xpath("//*[@id=\"countNumber\"]"));
     WebElement varInputCount = inputCount.get(0);
     varInputCount.sendKeys("2");
     
     List<WebElement> addToCart = driver.findElements(By.xpath("//*[@id=\"addToCartButton\"]"));
     WebElement varAddToCart = addToCart.get(0);
     varAddToCart.click();
     
     WebElement continueButton = driver.findElement(By.xpath("//button[text()='Continue']"));
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
  }

  @AfterClass
  public void afterPurchaseFlow() throws InterruptedException {
	  	Thread.sleep(3000);
	  	driver.quit();
  }

}
