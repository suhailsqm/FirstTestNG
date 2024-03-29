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
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

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

	@FindBy(xpath = "//*[@id=\"navbar-mobile\"]/ul[3]/li/a/span[2]/img")
	private WebElement logoutbar;

	@FindBy(xpath = "//*[@id=\"navbar-mobile\"]/ul[3]/li/div/a[2]")
	private WebElement logoutButton;

	public Login(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		PageFactory.initElements(driver, this);
	}

	public void login() throws IOException {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		aw.waitAllRequest();
		File src = new File(ReadPropertiesFile.readPropertiesFile().getProperty("resources.login"));
		FileInputStream finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
		DataFormatter formatter = new DataFormatter();
		sheet = workbook.getSheetAt(0);
//		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
		// Import data for Email.
		cell = sheet.getRow(1).getCell(2);
		String value = formatter.formatCellValue(cell);
		email.clear();
		email.sendKeys(value);

		// Import data for password.
		cell = sheet.getRow(1).getCell(3);
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
//		}
		finput.close();
	}

	public void logout() throws InterruptedException {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		logoutbar.click();
		aw.waitAllRequest();
		logoutButton.click();
		aw.waitAllRequest();
	}

}
