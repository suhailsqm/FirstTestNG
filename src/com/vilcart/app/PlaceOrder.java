package com.vilcart.app;

import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.NgWebDriver;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;

public class PlaceOrder {
	
    WebDriver driver;
    NgWebDriver ngWebDriver;
    JavascriptExecutor js;
    AngularWait aw;
    WebDriverWait wait;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFCell cell;
    
  @Test
  public void placeOrder() throws IOException {
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
          
          
          WebElement placeOrder = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[11]/a/span"));//*[@id="main-menu-navigation"]/li[11]/a/span
          js.executeScript("arguments[0].scrollIntoViewIfNeeded();", placeOrder);
          placeOrder.click();
          
          WebElement searchCustomer = driver.findElement(By.xpath("//*[@id=\"iconLeft1\"]"));
          searchCustomer.sendKeys("test");
          aw.waitAllRequest();
          WebElement testCustomer = driver.findElement(By.xpath("//*[@id=\"currCustomer\"]/h2"));
          testCustomer.click();
          WebElement searchItem = driver.findElement(By.xpath("//*[@id=\"itemName\"]"));
          searchItem.sendKeys("test");
          List<WebElement> addToCartButton = driver.findElements(By.id("addToCartList"));
          List<WebElement> itemNameList = driver.findElements(By.id("itemNameList"));
          for(int i1=0;i1<addToCartButton.size()&&i1<2;i1++) {
        	  //Reporter.log(addToCartButton.get(i1).toString(), true);
        	  Reporter.log("\n"+itemNameList.get(i1).getText(), true);
        	  addToCartButton.get(i1).click();
          }
          WebElement placeOrderButton = driver.findElement(By.xpath("//*[@id=\"placeOrderButton\"]"));
          placeOrderButton.click();
          WebElement remarksInput = driver.findElement(By.id("swal2-input"));
          remarksInput.sendKeys("placing order");
          //WebElement buyAllButton = driver.findElement(By.xpath("/html/body/div/div/div[6]/button[3]"));
          WebElement buyAllButton = driver.findElement(By.className("swal2-confirm"));
          buyAllButton.click();
      }
      finput.close();
	  
  }
  @BeforeClass
  public void beforePlaceOrder() {
	  
	  	WebDriverManager.chromedriver().setup();
	  	driver = new ChromeDriver();
	  	ngWebDriver = new NgWebDriver((JavascriptExecutor) driver).withRootSelector("\"app-create-customers\"");;
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
  public void afterPlaceOrder() throws InterruptedException {
	  	Thread.sleep(3000);
	  	driver.quit();
  }

}
