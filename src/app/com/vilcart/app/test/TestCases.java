package app.com.vilcart.app.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import util.com.vilcart.util.AngularWait;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

@Listeners(app.com.vilcart.app.test.ListenerTest.class)

public class TestCases {
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	XSSFCell cell;
	WebDriver driver;
	AngularWait aw;

//Test to pass as to verify listeners.		
	@Test
	public void Login() throws IOException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		aw = new AngularWait(driver);
		driver.get("http://localhost:4200");
		// driver.get("https://vilcart-buy.web.app");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		File src = new File("resources\\Login.xlsx");
		FileInputStream finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
		DataFormatter formatter = new DataFormatter();
		sheet = workbook.getSheetAt(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
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
	}

//Forcefully failed this test as verify listener.		
	@Test
	public void TestToFail() {
		driver.quit();
		System.out.println("This method to test fail");
		Assert.assertTrue(false);
	}
}