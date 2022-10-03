package app.com.vilcart.app.sku;

import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.NgWebDriver;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.Login;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
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
    private WebDriver driver;
    private JavascriptExecutor js;
    private AngularWait aw;
    private WebDriverWait wait;
	private Login loginObj;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFCell cell;

  @Test
  public void createSKU() throws IOException, InterruptedException {
	  File src=new File("resources\\SKUDummy.xlsx");
	  FileInputStream finput = new FileInputStream(src);
	  workbook = new XSSFWorkbook(finput);
      DataFormatter formatter = new DataFormatter();
      sheet= workbook.getSheetAt(0);
      int rowKey = 0;
      
      loginObj.login();
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
      Row row = sheet.createRow(rowKey++);
      Cell cell = row.createCell(0);
      cell.setCellValue("HSN Key");
      cell = row.createCell(1);
      cell.setCellValue(1245);
      
      WebElement name = driver.findElement(By.xpath("//*[@id=\"skuName\"]"));
      name.sendKeys("test sku 508");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("SKU Name");
      cell = row.createCell(1);
      cell.setCellValue("test sku 508");
      
      WebElement localName = driver.findElement(By.xpath("//*[@id=\"localName\"]"));
      localName.sendKeys("test SKU local");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("SKU Local Name");
      cell = row.createCell(1);
      cell.setCellValue("test SKU Local");
      
      WebElement desc = driver.findElement(By.xpath("//*[@id=\"description\"]"));
      desc.sendKeys("This is test SKU");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("Description");
      cell = row.createCell(1);
      cell.setCellValue("This is test SKU");
      
      WebElement category = driver.findElement(By.xpath("//*[@id=\"category\"]/div/div/div[3]/input"));//*[@id="category"]/div/div/div[3]/input
      category.click();
      WebElement category1 = driver.findElement(By.xpath("//*[@id=\"category\"]/ng-dropdown-panel/div/div[2]/div[3]/span"));
      category1.click();
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("Category");
      cell = row.createCell(1);
      WebElement category2 = driver.findElement(By.xpath("//*[@id=\"category\"]/div/div/div[2]/span[2]"));
      cell.setCellValue(category2.getText());
      
      aw.waitAllRequest();
      WebElement subCategory = driver.findElement(By.xpath("//*[@id=\"subCategory\"]/div/div/div[2]/input"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();",subCategory);
      subCategory.click();
      WebElement subCategory1 = driver.findElement(By.xpath("//*[@id=\"subCategory\"]/ng-dropdown-panel/div/div[2]/div[1]/span"));  
      subCategory1.click();
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("sub-Category");
      cell = row.createCell(1);
      WebElement subCategory2 = driver.findElement(By.xpath("//*[@id=\"subCategory\"]/div/div/div[2]/span[2]"));
      cell.setCellValue(subCategory2.getText());
      
      WebElement gstRate = driver.findElement(By.xpath("//*[@id=\"gstRate\"]"));
      gstRate.sendKeys("5");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("GST Rate");
      cell = row.createCell(1);
      cell.setCellValue(gstRate.getAttribute("value"));
      
      WebElement cessAmount = driver.findElement(By.xpath("//*[@id=\"cessAmount\"]"));
      cessAmount.sendKeys("5");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("CESS Rate");
      cell = row.createCell(1);
      cell.setCellValue(cessAmount.getAttribute("value"));
      
      WebElement amountType = driver.findElement(By.xpath("//*[@id=\"amountType\"]/div/div/div[3]/input"));
      amountType.click();
      WebElement subAmountType = driver.findElement(By.xpath("//*[@id=\"amountType\"]/ng-dropdown-panel/div/div[2]/div[2]/span"));
      subAmountType.click();
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("Amount Type");
      cell = row.createCell(1);
      WebElement amountType2 = driver.findElement(By.xpath("//*[@id=\"amountType\"]/div/div/div[2]/span[2]"));
      cell.setCellValue(amountType2.getText());
      
      WebElement brandName = driver.findElement(By.xpath("//*[@id=\"brandName\"]/div/div/div[2]/input"));
      js.executeScript("arguments[0].value='Parle Agro';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", brandName);
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("Brand Name");
      cell = row.createCell(1);
      cell.setCellValue(brandName.getAttribute("value"));
      
		WebElement variationButton = driver.findElement(By.xpath("//*[@id=\"variationButton\"]"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();", variationButton);
      variationButton.click();
    		  
      WebElement nameVariation = driver.findElement(By.xpath("//*[@id=\"variationName\"]"));
      nameVariation.sendKeys("test SKU automation");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("variation Name");
      cell = row.createCell(1);
      cell.setCellValue(nameVariation.getAttribute("value"));
      
      WebElement price = driver.findElement(By.xpath("//*[@id=\"price\"]"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();", price);
      price.clear();
      price.sendKeys("120");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("Price");
      cell = row.createCell(1);
      cell.setCellValue(price.getAttribute("value"));
      
      //WebElement unit = driver.findElement(By.xpath("//*[@id=\"unitText\"]"));
      //unit.sendKeys("1");
      
      WebElement value = driver.findElement(By.xpath("//*[@id=\"valueText\"]"));
      value.clear();
      value.sendKeys("2");
      row = sheet.createRow(rowKey++);
      cell = row.createCell(0);
      cell.setCellValue("value");
      cell = row.createCell(1);
      Reporter.log(value.getAttribute("value"), true);
      cell.setCellValue(value.getAttribute("value"));
      
      FileOutputStream fos = new FileOutputStream("resources\\SKUDummy.xlsx");
      workbook.write(fos);
      fos.close();
      finput.close();
      
      WebElement saveButton = driver.findElement(By.xpath("//*[@id=\"saveButton\"]"));
      js.executeScript("arguments[0].scrollIntoViewIfNeeded();", saveButton);
      saveButton.click();
      
      
  }
  @BeforeTest
  public void beforeCreateSKU() {
	  	WebDriverManager.chromedriver().setup();
	  	driver = new ChromeDriver();
	  	//ngWebDriver = new NgWebDriver((JavascriptExecutor) driver).withRootSelector("\"app-create-customers\"");;
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

  @AfterTest
  public void afterCreateSKU() throws InterruptedException {
	  	Thread.sleep(3000);
	  	driver.quit();
  }

}
