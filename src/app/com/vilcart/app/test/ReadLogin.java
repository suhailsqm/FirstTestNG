package app.com.vilcart.app.test;

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
import pom.com.vilcart.pom.login.*;
import org.openqa.selenium.interactions.Actions;
//import com.paulhammant.ngwebdriver.NgWebDriver;
//import com.paulhammant.ngwebdriver.ByAngular;
//import com.paulhammant.ngwebdriver.ByAngularModel;

import org.openqa.selenium.support.ui.WebDriverWait;

public class ReadLogin {

	static final Logger log = getLogger(lookup().lookupClass());

    WebDriver driver;
//    NgWebDriver ngWebDriver;
    //WebDriverWait wait;
    //XSSFWorkbook workbook;
    //XSSFSheet sheet;
    //XSSFCell cell;

    
    @BeforeTest
    public void TestSetup()
	{

    	WebDriverManager.firefoxdriver().setup();
    	driver = new FirefoxDriver();
//    	ngWebDriver = new NgWebDriver((JavascriptExecutor) driver);
    	driver.get("https://angular-tour-of-heroes-1a2a8.web.app/");
    	//driver.get("http://localhost:62827/");
    	driver.manage().window().maximize(); 
    	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
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
    	
//    	ngWebDriver.waitForAngularRequestsToFinish();
        //WebElement stateSelect = driver.findElement(ByAngular.withRootSelector("\"app-root\"").model("hero.name"));
        //WebElement stateSelect = driver.findElement(ByAngular.withRootSelector("\"app-heroes\"").model("hero.name"));
        //WebElement stateSelect = driver.findElement(ByAngularModel.name("hero.name"));
    	WebElement stateSelect = driver.findElement(By.id("name"));
    	stateSelect.clear();
    	stateSelect.click();
    	Thread.sleep(5000);
        //stateSelect.sendKeys("Testing Angular");
    	((JavascriptExecutor) driver).executeScript("arguments[0].value='testing angular interpolation';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", stateSelect);
        Thread.sleep(10000);
        driver.findElement(By.tagName("button")).click();
    	
    }
}
