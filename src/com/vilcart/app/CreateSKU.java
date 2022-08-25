package com.vilcart.app;

import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.vilcart.util.AngularWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeTest;

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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;

public class CreateSKU {
    WebDriver driver;
    NgWebDriver ngWebDriver;
    JavascriptExecutor js;
    AngularWait aw;
    WebDriverWait wait;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFCell cell;
  @Test
  public void createSKU() throws IOException {
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
      
      WebElement menuInput = driver.findElement(By.xpath("//*[@id=\"main-menu-content\"]/div[1]/input"));
      menuInput.sendKeys("SKU");
      menuInput.sendKeys(Keys.ENTER);
      WebElement clickSKU = driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li/a/span"));
      clickSKU.click();
      WebElement clickAddSKU =  driver.findElement(By.xpath("//button[normalize-space(.)='Add SKU']"));
      clickAddSKU.click();
      aw.waitAllRequest();
      
      WebElement hsnCode = driver.findElement(By.xpath("//*[@id=\"itemHSNCode\"]"));
      hsnCode.sendKeys("1245");
      
      WebElement name = driver.findElement(By.xpath("//*[@id=\"skuName\"]"));
      name.sendKeys("test SKU");
      
      WebElement localName = driver.findElement(By.xpath("//*[@id=\"localName\"]"));
      localName.sendKeys("test SKU local");
      
      WebElement desc = driver.findElement(By.xpath("//*[@id=\"description\"]"));
      desc.sendKeys("This is test SKU");
      
      WebElement category = driver.findElement(By.xpath("//*[@id=\"category\"]/div/div/div[2]/input"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();", category);
      category.click();
      WebElement category1 = driver.findElement(By.xpath("//*[@id=\"category\"]/ng-dropdown-panel/div/div[2]/div[3]/span"));
      category1.click();
      
      aw.waitAllRequest();
      WebElement subCategory = driver.findElement(By.xpath("//*[@id=\"subCategory\"]/div/div/div[2]/input"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();",subCategory);
      subCategory.click();
      WebElement subCategory1 = driver.findElement(By.xpath("//*[@id=\"subCategory\"]/ng-dropdown-panel/div/div[2]/div[1]/span"));  
      subCategory1.click();
      
      WebElement gstRate = driver.findElement(By.xpath("//*[@id=\"gstRate\"]"));
      gstRate.sendKeys("5");
      
      WebElement cessAmount = driver.findElement(By.xpath("//*[@id=\"cessAmount\"]"));
      cessAmount.sendKeys("5");
      
      WebElement amountType = driver.findElement(By.xpath("//*[@id=\"amountType\"]/div/div/div[3]/input"));
      amountType.click();
      WebElement subAmountType = driver.findElement(By.xpath("//*[@id=\"amountType\"]/ng-dropdown-panel/div/div[2]/div[2]/span"));
      subAmountType.click();
      
      WebElement brandName = driver.findElement(By.xpath("//*[@id=\"brandName\"]/div/div/div[2]/input"));
      js.executeScript("arguments[0].value='Parle Agro';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", brandName);
      
      WebElement variationButton = driver.findElement(By.xpath("//*[@id=\"variationButton\"]"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();", variationButton);
      variationButton.click();
    		  
      WebElement nameVariation = driver.findElement(By.xpath("//*[@id=\"textbox2\"]"));
      nameVariation.sendKeys("test SKU automation");
      
      WebElement price = driver.findElement(By.xpath("//*[@id=\"price\"]"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();", price);
      price.sendKeys("120");
      
      //WebElement unit = driver.findElement(By.xpath("//*[@id=\"unitText\"]"));
      //unit.sendKeys("1");
      
      WebElement value = driver.findElement(By.xpath("//*[@id=\"valueText\"]"));
      value.sendKeys("1");
      
      WebElement saveButton = driver.findElement(By.xpath("//*[@id=\"saveButton\"]"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();", saveButton);
      saveButton.click();
      
  }
  @BeforeTest
  public void beforeCreateSKU() {
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

  @AfterTest
  public void afterCreateSKU() throws InterruptedException {
	  	Thread.sleep(3000);
	  	driver.quit();
  }

}
