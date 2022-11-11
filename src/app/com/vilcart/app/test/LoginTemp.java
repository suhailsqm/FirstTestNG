package app.com.vilcart.app.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

public class LoginTemp {

	XSSFWorkbook workbook;
	XSSFSheet sheet;
	XSSFCell cell;
	WebDriver driver;
	AngularWait aw;

	public LoginTemp(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
	}

	public void login() throws IOException {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		aw.waitAllRequest();
		File src = new File("resources\\Login.xlsx");
		FileInputStream finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
		DataFormatter formatter = new DataFormatter();
		sheet = workbook.getSheetAt(0);
		Reporter.log("-> "+sheet.getLastRowNum(),true);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Reporter.log(""+i+" "+sheet.getLastRowNum(),true);
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
}
