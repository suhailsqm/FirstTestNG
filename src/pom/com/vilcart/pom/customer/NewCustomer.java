/**
 * 
 */
package pom.com.vilcart.pom.customer;

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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import static org.assertj.core.api.Assertions.assertThat;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.LineNumber;
import util.com.vilcart.util.TimeStamp;

/**
 * @author win10
 *
 */
public class NewCustomer {

	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
	private long phoneNumber;

	private File file;
	private FileInputStream finput;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;

	@FindBy(id = "customerState")
	private WebElement customerState;

	@FindBy(id = "customerDistrict")
	private WebElement customerDistrict;

	@FindBy(id = "customerTaluk")
	private WebElement customerTaluk;

	@FindBy(id = "customerPostal")
	private WebElement customerPostal;

	@FindBy(id = "customerVillage")
	private WebElement customerVillage;

	@FindBy(id = "customerShopType")
	private WebElement customerShopType;

	@FindBy(id = "customerShopName")
	private WebElement customerShopName;

	@FindBy(id = "customerName")
	private WebElement customerName;

	@FindBy(id = "customerLocalName")
	private WebElement customerLocalName;

	@FindBy(id = "customerVillageLocalName")
	private WebElement customerVillageLocalName;

	@FindBy(id = "customerPhoneNumber")
	private WebElement customerPhoneNumber;

	@FindBy(id = "customerPhoneNumber2")
	private WebElement customerPhoneNumber2;

	@FindBy(id = "customerAddress")
	private WebElement customerAddress;

	@FindBy(id = "customerLandMark")
	private WebElement customerLandMark;

	@FindBy(id = "customerLeisure")
	private WebElement customerLeisure;

	@FindBy(id = "customerBreakTime")
	private WebElement customerBreakTime;

	@FindBy(id = "customerHasCooler")
	private WebElement customerHasCooler;

	@FindBy(id = "customerCoolerType")
	private WebElement customerCoolerType;

	@FindBy(id = "customerQualification")
	private WebElement customerQualification;

	@FindBy(xpath = "//*[@id=\\\"customerGrade\\\"]/div/div/div[2]/input")
	private WebElement customerGrade;

	@FindBy(id = "customerAvgSale")
	private WebElement customerAvgSale;

	@FindBy(xpath = "//*[@id=\\\"customerIsSmartPhoneUser\\\"]/div/div/div[3]/input")
	private WebElement customerIsSmartPhoneUser;

	@FindBy(id = "createCustomerButton")
	private WebElement createCustomerButton;

	public NewCustomer(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		file = new File("resources\\Customer.xlsx");
		try {
			finput = new FileInputStream(file);
			formatter = new DataFormatter();
			workbook = new XSSFWorkbook(finput);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeFileInputStream() {
		try {
			finput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String fetchFeild(String feild) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			cell1 = sheet.getRow(i).getCell(0);
			String value = formatter.formatCellValue(cell1);
			if (feild.contentEquals(value)) {
				cell2 = sheet.getRow(i).getCell(1);
				String value2 = formatter.formatCellValue(cell2);
				return value2;
			}
		}
		assertThat(false).withFailMessage("No specific '" + feild + "' in workbook resource.").isEqualTo(true);
		return null;
	}

	public void createCustomer() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		WebElement stateInput = customerState.findElement(By.xpath("//div/div/div[3]/input"));// *[@id="customerState"]/div/div/div[3]/input
		js.executeScript("arguments[0].value='" + fetchFeild("customerState")
				+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('change'))", stateInput);
		aw.waitAllRequest();

		WebElement district = customerDistrict.findElement(By.xpath("//div/div/div[3]/input"));// *[@id="customerDistrict"]/div/div/div[3]/input
		js.executeScript("arguments[0].value='" + fetchFeild("customerDistrict")
				+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('change'))", district);
		aw.waitAllRequest();

		WebElement taluk = customerTaluk.findElement(By.xpath("//div/div/div[3]/input"));
		taluk.click();
		aw.waitAllRequest();
		List<WebElement> taluk1 = customerTaluk.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < taluk1.size(); i++) {
			if (taluk1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(fetchFeild("customerTaluk"))) {
				taluk1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == taluk1.size()) {
				assertThat(false)
						.withFailMessage("No specific '" + fetchFeild("customerTaluk") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		WebElement postal = customerPostal.findElement(By.xpath("//div/div/div[3]/input"));
		postal.click();
		aw.waitAllRequest();
		List<WebElement> postal1 = customerPostal.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < postal1.size(); i++) {
			if (postal1.get(i).findElement(By.xpath("//span")).getText()
					.equalsIgnoreCase(fetchFeild("customerPostal"))) {
				postal1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == postal1.size()) {
				assertThat(false)
						.withFailMessage("No specific '" + fetchFeild("customerPostal") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		WebElement village = customerVillage.findElement(By.xpath("//div/div/div[3]/input"));
		village.click();
		aw.waitAllRequest();
		List<WebElement> village1 = customerVillage.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < village1.size(); i++) {
			if (village1.get(i).findElement(By.xpath("//span")).getText()
					.equalsIgnoreCase(fetchFeild("customerVillage"))) {
				village1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == village1.size()) {
				assertThat(false)
						.withFailMessage("No specific '" + fetchFeild("customerVillage") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		WebElement shop = customerShopType.findElement(By.xpath("//div/div/div[3]/input"));
		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerShopType")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				shop);
		aw.waitAllRequest();

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerShopName")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerShopName);

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerName")
						+ "';arguments[0].click;arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerName);
		customerName.sendKeys(Keys.ENTER);

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerLocalName")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerLocalName);

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerVillageLocalName")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerVillageLocalName);

		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		js.executeScript(
				"arguments[0].value='" + number
						+ "';arguments[0].click;arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerPhoneNumber);
		customerPhoneNumber.sendKeys(Keys.ENTER);
		this.phoneNumber = number;
		Reporter.log(LineNumber.getLineNumber() + " " + number, true);

		js.executeScript(
				"arguments[0].value='1032547698';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerPhoneNumber2);

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerAddress")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerAddress);

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerLandMark")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerLandMark);

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", customerLeisure);

		if (fetchFeild("customerLeisure").contentEquals("Break Time")) {
			WebElement customerLeisure1 = customerLeisure.findElement(By.xpath("//div/span[2]"));
			customerLeisure1.click();

			WebElement customerLeisure3 = customerLeisure.findElement(By.xpath("//div/div/div[3]/input"));
			js.executeScript("arguments[0].click();", customerLeisure3);
			aw.waitAllRequest();

			WebElement customerLeisure2 = customerLeisure
					.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div[2]/span"));
			js.executeScript("arguments[0].click()", customerLeisure2);
			aw.waitAllRequest();

			js.executeScript("arguments[0].value='" + fetchFeild("customerBreakTime")
					+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
					customerBreakTime);
		} else if (fetchFeild("customerLeisure").contentEquals("No Break")) {
			WebElement customerLeisure1 = customerLeisure.findElement(By.xpath("//div/span[2]"));
			customerLeisure1.click();

			WebElement customerLeisure3 = customerLeisure.findElement(By.xpath("//div/div/div[3]/input"));
			js.executeScript("arguments[0].click();", customerLeisure3);
			aw.waitAllRequest();

			WebElement customerLeisure2 = customerLeisure
					.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div[1]/span"));
			js.executeScript("arguments[0].click()", customerLeisure2);
			aw.waitAllRequest();
		} else {
			Reporter.log("Invalid Option for customer Lesiure, It should be either 'Break Time' or 'No Break'", true);
		}

		if (fetchFeild("customerHasCooler").contentEquals("Yes")) {
			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", customerHasCooler);
			customerHasCooler.click();
			WebElement customerHasCooler1 = customerHasCooler
					.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div[1]"));
			customerHasCooler1.click();
			aw.waitAllRequest();
			WebElement customerCoolerType1 = customerCoolerType.findElement(By.xpath("//div/div/div[2]/input"));
			js.executeScript("arguments[0].value='" + fetchFeild("customerCoolerType")
					+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
					customerCoolerType1);
		} else if (fetchFeild("customerHasCooler").contentEquals("No")) {
			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", customerHasCooler);
			customerHasCooler.click();
			WebElement customerHasCooler1 = customerHasCooler
					.findElement(By.xpath("//ng-dropdown-panel/div/div[2]/div[2]"));
			customerHasCooler1.click();
			aw.waitAllRequest();
		} else {
			Reporter.log("Invalid Option for customerHasCooler, It should be either 'Yes' or 'No'", true);
		}

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerQualification")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerQualification);

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerGrade")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerGrade);

		WebElement customerAvgSale = driver.findElement(By.xpath("//*[@id=\"customerAvgSale\"]"));
		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerAvgSale")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerAvgSale);

		js.executeScript(
				"arguments[0].value='" + fetchFeild("customerIsSmartPhoneUser")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				customerIsSmartPhoneUser);

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", createCustomerButton);
		aw.waitAllRequest();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		Reporter.log(LineNumber.getLineNumber() + " " + "" + createCustomerButton.isEnabled(), true);
		Reporter.log(LineNumber.getLineNumber() + " " + createCustomerButton.isDisplayed(), true);
		wait.until(ExpectedConditions.elementToBeClickable(createCustomerButton));
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", createCustomerButton);
		createCustomerButton.click();
		aw.waitAllRequest();

		closeFileInputStream();
	}
}
