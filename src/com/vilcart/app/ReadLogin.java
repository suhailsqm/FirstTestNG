package com.vilcart.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.poi.ss.usermodel.Cell.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.vilcart.pom.*;
import org.openqa.selenium.interactions.Actions;
import com.paulhammant.ngwebdriver.NgWebDriver;
import com.paulhammant.ngwebdriver.ByAngular;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReadLogin {

	static final Logger log = getLogger(lookup().lookupClass());

    WebDriver driver;
    NgWebDriver ngWebDriver;
    WebDriverWait wait;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFCell cell;

    
    @BeforeTest
    public void TestSetup()
	{
    	//WebDriverManager.chromedriver().setup();
    	//driver = new ChromeDriver();
    	 WebDriverManager.firefoxdriver().setup();
    	 driver = new FirefoxDriver();

    	//ChromeOptions options = new ChromeOptions();
    	//options.setBinary("C:\\Program Files\\Google\\Chrome Beta\\Application\\chrome.exe");
    	//System.setProperty("webdriver.chrome.driver","C:\\Users\\win10\\Downloads\\chromedriver_win32_104\\chromedriver.exe");
    	//driver = new ChromeDriver(options);
    	ngWebDriver = new NgWebDriver((JavascriptExecutor) driver);
       // Enter url.
       driver.get("https://vilcart-buy.web.app/");
       driver.manage().window().maximize();
        
       //wait = new WebDriverWait(driver,30);
       driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
       assertThat(driver.getTitle()).containsIgnoringCase("Login - VILCART");
       Reporter.log(driver.getTitle(), true);
     }
    @AfterTest
    public void TestTeardown() throws InterruptedException 
    {
    	Thread.sleep(3000);
    	driver.quit();
    }
    @Test
    public void ReadData() throws FileNotFoundException, IOException, NoSuchElementException, InterruptedException, JavascriptException {
    	// Import excel sheet.
        //File src=new File("C:\\Users\\win10\\eclipse-workspace\\FirstTestNG\\TestData.xlsx");
    	File src=new File("TestData.xlsx");
        // Load the file.
        FileInputStream finput = new FileInputStream(src);
         
        // Load he workbook.
         workbook = new XSSFWorkbook(finput);
         DataFormatter formatter = new DataFormatter();
        // Load the sheet in which data is stored.
        sheet= workbook.getSheetAt(0);
        for(int i=1; i<=sheet.getLastRowNum(); i++)
        {
            // Import data for Email.
            cell = sheet.getRow(i).getCell(2);
            //cell.setCellType(Cell.CELL_TYPE_STRING);
            String value = formatter.formatCellValue(cell);
            driver.findElement(By.cssSelector("input[ng-reflect-name=email]")).sendKeys(value);
             
            // Import data for password.
            cell = sheet.getRow(i).getCell(3);
            //cell.setCellType(Cell.CELL_TYPE_STRING);
            value = formatter.formatCellValue(cell);
            driver.findElement(By.cssSelector("input[ng-reflect-name=password]")).sendKeys(value);
            
            driver.findElement(By.tagName("button")).click();
            
            //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            
            Wait<WebDriver> wait= new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofSeconds(5))
                    .ignoring(NoSuchElementException.class);
            
            //log.info("Home page title: \n"+ driver.getTitle());
            //log.debug("Home page title: \n"+ driver.getTitle());
            Thread.sleep(3000);
            ngWebDriver.waitForAngularRequestsToFinish();
            Reporter.log(driver.getTitle(), true);
            assertThat(driver.getTitle()).containsIgnoringCase("Home - VILCART");
            
            driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[2]/a/span")).click();
            
            driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[2]/ul/li[1]/a")).click();
            
            //WebElement state = driver.findElement(By.xpath("/html/body/app-main/app-private-layout/app-create-customers/div/div/div[2]/section/div/div/m-card/div/div[2]/div/div/div[2]/div[1]/div/ng-select/div/div/div[3]/input"));
            Actions action = new Actions(driver);
            //state.click();
            //action.moveToElement(state).click().build().perform();
            //ngWebDriver.waitForAngularRequestsToFinish();
            
            Reporter.log("Before model state",true);
            //Thread.sleep(10000);
            
            NgWebDriver ngwd = new NgWebDriver((JavascriptExecutor)driver).withRootSelector("\"app-create-customers\"");
            ByAngular.Factory baf = ngwd.makeByAngularFactory();
            WebElement stateSelect = driver.findElement(baf.model("state"));
            
            //WebElement stateSelect = driver.findElement(ByAngular.withRootSelector("\"app-create-customers\"").model("state"));
            stateSelect.click();


            Thread.sleep(2000);
        }
        finput.close();
    }
}
