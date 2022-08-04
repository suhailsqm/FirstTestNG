package com.vilcart.app;

import org.testng.annotations.Test;

import com.paulhammant.ngwebdriver.NgWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeClass;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.openqa.selenium.interactions.Actions;

public class CustomerCreation {
	
    WebDriver driver;
    NgWebDriver ngWebDriver;
    JavascriptExecutor js;
    WebDriverWait wait;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFCell cell;
    
    public void waitForAngular() {
    	final String ForAngular = "var rootSelector = arguments[0];"+
    			"var callback = arguments[1];"+
    			"if (window.angular && !(window.angular.version && window.angular.version.major > 1)) {"+
    			     "var hooks = getNg1Hooks(rootSelector);"+
    			   "if (hooks.$$testability) {"+

    			       "hooks.$$testability.whenStable(callback);"+

    			   "} else if (hooks.$injector) {"+

    			       "hooks.$injector.get('$browser')."+

    			       "notifyWhenNoOutstandingRequests(callback);"+

    			   "} else if (!!rootSelector) {"+

    			       "throw new Error('Could not automatically find injector on page: ' +"+

    			           "window.location.toString() + '. Consider setting rootElement');"+

    			   "} else {"+

    			   "throw new Error('root element (' + rootSelector + ') has no injector.' +"+

    			       "' this may mean it is not inside ng-app.');"+

    			   "}"+

    			"} else if (rootSelector && window.getAngularTestability) {"+

    			   "var el = document.querySelector(rootSelector);"+

    			   "window.getAngularTestability(el).whenStable(callback);"+

    			"} else if (window.getAllAngularTestabilities) {"+

    			   "var testabilities = window.getAllAngularTestabilities();"+

    			   "var count = testabilities.length;"+

    			   "var decrement = function() {"+

    			       "count--;"+

    			       "if (count === 0) {"+

    			           "callback();"+

    			       "}"+

    			   "};"+

    			   "testabilities.forEach(function(testability) {"+

    			       "testability.whenStable(decrement);"+

    			   "});"+

    			"} else if (!window.angular) {"+

    			   "throw new Error('window.angular is undefined. This could be either ' +"+

    			       "'because this is a non-angular page or because your test involves ' +"+

    			       "'client-side navigation, which can interfere with Protractor\'s ' +"+

    			       "'bootstrapping. See http://git.io/v4gXM for details');"+

    			"} else if (window.angular.version >= 2) {"+

    			   "throw new Error('You appear to be using angular, but window.' +"+

    			       "'getAngularTestability was never set. This may be due to bad ' +"+

    			       "'obfuscation.');"+

    			"} else {"+

    			   "throw new Error('Cannot get testability API for unknown angular ' +"+

    			       "'version \"\"' + window.angular.version + '\"\"');"+

    			"}"+
    			
"function getNg1Hooks(selector, injectorPlease) {"+

   "function tryEl(el) {"+

       "try {"+

           "if (!injectorPlease && angular.getTestability) {"+

               "var $$testability = angular.getTestability(el);"+

               "if ($$testability) {"+

                   "return {$$testability: $$testability};"+

               "}"+

           "} else {"+

               "var $injector = angular.element(el).injector();"+

               "if ($injector) {"+

                   "return {$injector: $injector};"+

               "}"+

           "}"+

       "} catch(err) {}"+

   "}"+

   "function trySelector(selector) {"+

       "var els = document.querySelectorAll(selector);"+

       "for (var i = 0; i < els.length; i++) {"+

           "var elHooks = tryEl(els[i]);"+

           "if (elHooks) {"+

               "return elHooks;"+

           "}"+

       "}"+

   "}"+

   "if (selector) {"+

       "return trySelector(selector);"+

   "} else if (window.__TESTABILITY__NG1_APP_ROOT_INJECTOR__) {"+

       "var $injector = window.__TESTABILITY__NG1_APP_ROOT_INJECTOR__;"+

       "var $$testability = null;"+

       "try {"+

           "$$testability = $injector.get('$$testability');"+

       "} catch (e) {}"+

       "return {$injector: $injector, $$testability: $$testability};"+

   "} else {"+

       "return tryEl(document.body) ||"+

           "trySelector('[ng-app]') || trySelector('[ng\\:app]') ||"+

           "trySelector('[ng-controller]') || trySelector('[ng\\:controller]');"+

   "}"+

"};";		
    	
    	
    	
    	((JavascriptExecutor) driver).executeAsyncScript(ForAngular,"\"app-main\"");
    }
    
    public void untilAngularFinishHttpCalls() {
        final String javaScriptToLoadAngular =
                "var injector = window.angular.element('body').injector();" + 
                "var $http = injector.get('$http');" + 
                "return ($http.pendingRequests.length === 0)";

        ExpectedCondition<Boolean> pendingHttpCallsCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript(javaScriptToLoadAngular).equals(true);
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // timeout = 20 secs
        wait.until(pendingHttpCallsCondition);
    }
    
  @Test
  public void testCreation() throws IOException, InterruptedException {
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
          String value = formatter.formatCellValue(cell);
          driver.findElement(By.cssSelector("input[ng-reflect-name=email]")).sendKeys(value);
           
          // Import data for password.
          cell = sheet.getRow(i).getCell(3);
          value = formatter.formatCellValue(cell);
          driver.findElement(By.cssSelector("input[ng-reflect-name=password]")).sendKeys(value);
          
          driver.findElement(By.tagName("button")).click();
          
          driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
          
          /*Wait<WebDriver> wait= new FluentWait<WebDriver>(driver)
                  .withTimeout(Duration.ofSeconds(30))
                  .pollingEvery(Duration.ofSeconds(5))
                  .ignoring(NoSuchElementException.class);*/
          
          driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
          driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
          Thread.sleep(5000);
          WebElement body = driver.findElement(By.xpath("/html/body"));
          //js.executeScript("var injector = window.angular.element(document).find('body').injector(); var $http = injector.get('$http'); return ($http.pendingRequests.length === 0);",body);
          ngWebDriver.waitForAngularRequestsToFinish();
          Reporter.log(driver.getTitle(), true);
          //untilAngularFinishHttpCalls();
          //waitForAngular();
          String angularReady = "return window.angular.element(document).injector().get('$http').pendingRequests.length === 0";
          boolean isAngularReady = (boolean)js.executeScript(angularReady);
          
          assertThat(driver.getTitle()).containsIgnoringCase("Home - VILCART");
          
          
          driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[2]/a/span")).click();
          
          driver.findElement(By.xpath("//*[@id=\"main-menu-navigation\"]/li[2]/ul/li[1]/a")).click();
          
          
          
          ngWebDriver.waitForAngularRequestsToFinish();
          WebElement state = driver.findElement(By.xpath("//*[@id=\"customerState\"]"));
          js.executeScript("arguments[0].value='KARNATAKA';arguments[0].click()", state);
         
          WebElement district = driver.findElement(By.xpath("//*[@id=\"customerDistrict\"]"));
          js.executeScript("var injector = window.angular.element(arguments[0]).injector(); var $http = injector.get('$http'); return ($http.pendingRequests.length === 0);",district);
          js.executeScript("arguments[0].value='MANDYA';arguments[0].click()", district);
          
          
          Thread.sleep(2000);
          js.executeScript("var injector = window.angular.element('body').injector(); var $http = injector.get('$http'); return ($http.pendingRequests.length === 0);");
          WebElement taluk = driver.findElement(By.xpath("//*[@id=\"customerTaluk\"]"));
          js.executeScript("arguments[0].value='Maddur';arguments[0].click()", taluk);
          Thread.sleep(2000);
         // js.executeScript("var injector = window.angular.element('body').injector(); var $http = injector.get('$http'); return ($http.pendingRequests.length === 0);");
          WebElement postal = driver.findElement(By.xpath("//*[@id=\"customerPostal\"]"));////*[@id="customerPostal"]/div/div/div[3]/input
          js.executeScript("arguments[0].value='Alur-maddur B.O';arguments[0].click()", postal);
          Thread.sleep(2000);
         // js.executeScript("var injector = window.angular.element('body').injector(); var $http = injector.get('$http'); return ($http.pendingRequests.length === 0);");
          WebElement village = driver.findElement(By.xpath("//*[@id=\"customerVillage\"]"));
          js.executeScript("arguments[0].value='Alur';arguments[0].click()", village);
          Thread.sleep(2000);
         // js.executeScript("var injector = window.angular.element('body').injector(); var $http = injector.get('$http'); return ($http.pendingRequests.length === 0);");
         
         // Actions action = new Actions(driver);
          //state.click();
          //action.moveToElement(state).click().build().perform();
          //action.scrollToElement(state).doubleClick();
          //ngWebDriver.waitForAngularRequestsToFinish();
          
          //Reporter.log("Before model state",true);
          //Thread.sleep(1000);
          //action.moveToElement(state).sendKeys(Keys.ARROW_UP);
          //Thread.sleep(1000);
          //action.sendKeys(Keys.ARROW_UP);
          //Thread.sleep(1000);
          //action.sendKeys(Keys.ARROW_UP);
          //action.sendKeys(Keys.ENTER);
          //NgWebDriver ngwd = new NgWebDriver((JavascriptExecutor)driver).withRootSelector("\"app-create-customers\"");
          //NgWebDriver ngwd = new NgWebDriver((JavascriptExecutor)driver).withRootSelector("\"app-main\"");
          //NgWebDriver ngwd = new NgWebDriver((JavascriptExecutor)driver).withRootSelector("\"app-main\"");
          //ByAngular.Factory baf = ngwd.makeByAngularFactory();
          //WebElement stateSelect = driver.findElement(baf.model("state"));
        //*[@id="name"]

         // WebElement we = driver.findElement(By.xpath("//*[@id=\"name\"]"));
          //we.clear();
          //we.sendKeys("Testing angular");
          
          
        
         
          //Reporter.log(driver.findElement(By.xpath("/html/body/app-root/app-heroes/div[2]")).getText(), true);
          //stateSelect.click();


          Thread.sleep(2000);
      }
      finput.close();
	  
  }
  @BeforeClass
  public void TestSetup() {
	  
  	WebDriverManager.chromedriver().setup();
  	driver = new ChromeDriver();
  	ngWebDriver = new NgWebDriver((JavascriptExecutor) driver).withRootSelector("\"app-create-customers\"");;
  	driver.get("http://localhost:4200");
  	driver.manage().window().maximize(); 
  	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
  	Reporter.log(driver.getTitle(), true);
  	js=((JavascriptExecutor) driver);
  	
  }

  @AfterClass
  public void TestTeardown() throws InterruptedException {
  	Thread.sleep(3000);
  	driver.quit();
  }

}
