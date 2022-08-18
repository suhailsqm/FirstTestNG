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

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
     menuRequestItem.click();
     
     WebElement itemSelectInput = driver.findElement(By.xpath("//*[@id=\"iconLeft1\"]"));
     itemSelectInput.sendKeys("test");
     aw.waitAllRequest();
     
     
     
  }
  @BeforeClass
  public void beforePurchaseFlow() {
	WebDriverManager.chromedriver().setup();
  	driver = new ChromeDriver();
  	//driver.get("http://localhost:4200");
  	driver.get("https://vilcart-buy.web.app");
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
