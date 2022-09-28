package pom.com.vilcart.pom.login;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;

public class Login {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFCell cell;
	private WebDriver driver;
	private AngularWait aw;

	@CacheLookup
	@FindBy(css = "input[ng-reflect-name=email]")
	private WebElement email;

	@CacheLookup
	@FindBy(css = "input[ng-reflect-name=password]")
	private WebElement pass;

	@CacheLookup
	@FindBy(tagName = "button")
	private WebElement loginButton;

	public Login(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		PageFactory.initElements(driver, this);
	}

	public void login() throws IOException {

		aw.waitAllRequest();
		File src = new File("resources\\Login.xlsx");
		FileInputStream finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
		DataFormatter formatter = new DataFormatter();
		sheet = workbook.getSheetAt(0);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			// Import data for Email.
			cell = sheet.getRow(i).getCell(2);
			String value = formatter.formatCellValue(cell);
			email.clear();
			email.sendKeys(value);

			// Import data for password.
			cell = sheet.getRow(i).getCell(3);
			value = formatter.formatCellValue(cell);
			pass.clear();
			pass.sendKeys(value);

			loginButton.click();
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
