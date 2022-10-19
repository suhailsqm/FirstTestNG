/**
 * 
 */
package pom.com.vilcart.pom.vehicle;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

/**
 * @author win10
 *
 */
public class NewVehicle {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	private File file;
	private FileInputStream finput;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;

	@FindBy(id = "selectedDC")
	private WebElement selectedDC;

	@FindBy(id = "state")
	private WebElement state;

	@FindBy(id = "driverName")
	private WebElement driverName;

	@FindBy(id = "contactInfo")
	private WebElement contactInfo;

	@FindBy(id = "address")
	private WebElement address;

	@FindBy(xpath = "//button[normalize-space()='Next']")
	private List<WebElement> next;

	@FindBy(xpath = "//button[normalize-space()='Previous']")
	private List<WebElement> previous;

	@FindBy(id = "vehicleNumber")
	private WebElement vehicleNumber;

	@FindBy(id = "vehicleType")
	private WebElement vehicleType;

	@FindBy(id = "vehicleBrand")
	private WebElement vehicleBrand;

	@FindBy(id = "vehicleDlupto")
	private WebElement vehicleDlupto;

	@FindBy(id = "height")
	private WebElement height;

	@FindBy(id = "length")
	private WebElement length;

	@FindBy(id = "width")
	private WebElement width;

	@FindBy(id = "licenseUrl")
	private WebElement licenseUrl;

	@FindBy(id = "vehicleRegdate")
	private WebElement vehicleRegdate;

	@FindBy(id = "vehicleIncharge")
	private WebElement vehicleIncharge;

	@FindBy(id = "vehicleLeaseUpto")
	private WebElement vehicleLeaseUpto;

	@FindBy(id = "vehicleCapacity")
	private WebElement vehicleCapacity;

	@FindBy(id = "vehicleFcUpto")
	private WebElement vehicleFcUpto;

	@FindBy(id = "vehicleRCnumber")
	private WebElement vehicleRCnumber;

	@FindBy(id = "vehicleEmissionUpto")
	private WebElement vehicleEmissionUpto;

	@FindBy(id = "vehicleRCUrl")
	private WebElement vehicleRCUrl;

	@FindBy(id = "isLoan")
	private WebElement isLoan;

	@FindBy(id = "loanAmount")
	private WebElement loanAmount;

	@FindBy(id = "loanTenure")
	private WebElement loanTenure;

	@FindBy(id = "lender")
	private WebElement lender;

	@FindBy(id = "emiDate")
	private WebElement emiDate;

	@FindBy(id = "emiAmount")
	private WebElement emiAmount;

	@FindBy(id = "leaseagreementUrl")
	private WebElement leaseagreementUrl;

	@FindBy(id = "bankName")
	private WebElement bankName;

	@FindBy(id = "accountNumber")
	private WebElement accountNumber;

	@FindBy(id = "IFSCCode")
	private WebElement IFSCCode;

	@FindBy(id = "chequeUrl")
	private WebElement chequeUrl;

	@FindBy(id = "vehicleRentPerDay")
	private WebElement vehicleRentPerDay;

	@FindBy(id = "emmisionUrl")
	private WebElement emmisionUrl;

	@FindBy(id = "vehicleRatePerKm")
	private WebElement vehicleRatePerKm;

	@FindBy(id = "fitnessUrl")
	private WebElement fitnessUrl;

	@FindBy(id = "insuranceUpto")
	private WebElement insuranceUpto;

	@FindBy(id = "insuranceUrl")
	private WebElement insuranceUrl;

	@FindBy(id = "exceptionalUrl")
	private WebElement exceptionalUrl;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	public NewVehicle(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		file = new File("resources\\NewVehicle.xlsx");
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

	public void createNewVehicle1StPage() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		WebElement temp = selectedDC.findElement(By.xpath("//div/div/div[2]/input"));
		temp.click();
		aw.waitAllRequest();
		List<WebElement> temp1 = selectedDC.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < temp1.size(); i++) {
			if (temp1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(fetchFeild("selectedDC"))) {
				temp1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == temp1.size() - 1) {
				assertThat(false)
						.withFailMessage("No specific '" + fetchFeild("selectedDC") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		temp = state.findElement(By.xpath("//div/div/div[2]/input"));
		temp.click();
		aw.waitAllRequest();
		temp1 = state.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < temp1.size(); i++) {
			if (temp1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(fetchFeild("selectedDC"))) {
				temp1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == temp1.size() - 1) {
				assertThat(false)
						.withFailMessage("No specific '" + fetchFeild("selectedDC") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		driverName.clear();
		driverName.sendKeys(fetchFeild("driverName"));

		contactInfo.clear();
		contactInfo.sendKeys(fetchFeild("selectedDC"));

		address.clear();
		address.sendKeys(fetchFeild("address"));

		next.get(0).click();
	}

	public void createNewVehicle2ndPage() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		vehicleNumber.clear();
		vehicleNumber.sendKeys(fetchFeild("vehicleNumber"));

		WebElement temp = vehicleType.findElement(By.xpath("//div/div/div[2]/input"));
		temp.click();
		aw.waitAllRequest();
		List<WebElement> temp1 = vehicleType.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < temp1.size(); i++) {
			if (temp1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(fetchFeild("vehicleType"))) {
				temp1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == temp1.size() - 1) {
				assertThat(false)
						.withFailMessage("No specific '" + fetchFeild("vehicleType") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		temp = vehicleBrand.findElement(By.xpath("//div/div/div[2]/input"));
		temp.click();
		aw.waitAllRequest();
		temp1 = vehicleBrand.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < temp1.size(); i++) {
			if (temp1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(fetchFeild("vehicleBrand"))) {
				temp1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == temp1.size() - 1) {
				assertThat(false)
						.withFailMessage("No specific '" + fetchFeild("vehicleBrand") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		js.executeScript("arguments[0].value = '" + fetchFeild("vehicleDlupto")
				+ "';arguments[0].dispatchEvent(new Event('input'))", vehicleDlupto);

		height.clear();
		height.sendKeys(fetchFeild("height"));

		length.clear();
		length.sendKeys(fetchFeild("length"));

		width.clear();
		width.sendKeys(fetchFeild("width"));

		licenseUrl.clear();
		licenseUrl.sendKeys(fetchFeild("licenseUrl"));

		js.executeScript("arguments[0].value = '" + fetchFeild("vehicleRegdate")
				+ "';arguments[0].dispatchEvent(new Event('input'))", vehicleRegdate);

		vehicleIncharge.clear();
		vehicleIncharge.sendKeys(fetchFeild("vehicleIncharge"));

		js.executeScript("arguments[0].value = '" + fetchFeild("vehicleLeaseUpto")
				+ "';arguments[0].dispatchEvent(new Event('input'))", vehicleLeaseUpto);

		vehicleCapacity.clear();
		vehicleCapacity.sendKeys(fetchFeild("vehicleCapacity"));

		js.executeScript("arguments[0].value = '" + fetchFeild("vehicleFcUpto")
				+ "';arguments[0].dispatchEvent(new Event('input'))", vehicleFcUpto);

		vehicleRCnumber.clear();
		vehicleRCnumber.sendKeys(fetchFeild("vehicleRCnumber"));

		js.executeScript("arguments[0].value = '" + fetchFeild("vehicleEmissionUpto")
				+ "';arguments[0].dispatchEvent(new Event('input'))", vehicleEmissionUpto);

		vehicleRCUrl.clear();
		vehicleRCUrl.sendKeys(fetchFeild("vehicleRCUrl"));

		next.get(1).click();
	}

	public void createNewVehicle3rdPage() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		WebElement temp = isLoan.findElement(By.xpath("//div/div/div[2]/input"));
		temp.click();
		aw.waitAllRequest();
		List<WebElement> temp1 = isLoan.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < temp1.size(); i++) {
			if (temp1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(fetchFeild("isLoan"))) {
				temp1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == temp1.size() - 1) {
				assertThat(false).withFailMessage("No specific '" + fetchFeild("isLoan") + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		if (fetchFeild("isLoan").equalsIgnoreCase("yes")) {
			loanAmount.clear();
			loanAmount.sendKeys(fetchFeild("loanAmount"));

			loanTenure.clear();
			loanTenure.sendKeys(fetchFeild("loanTenure"));

			lender.clear();
			lender.sendKeys(fetchFeild("lender"));

			js.executeScript("arguments[0].value = '" + fetchFeild("emiDate")
					+ "';arguments[0].dispatchEvent(new Event('input'))", emiDate);

			emiAmount.clear();
			emiAmount.sendKeys(fetchFeild("emiAmount"));

			leaseagreementUrl.clear();
			leaseagreementUrl.sendKeys(fetchFeild("leaseagreementUrl"));
		}

		next.get(2).click();
	}

	public void createNewVehicle4thPage() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		bankName.clear();
		bankName.sendKeys(fetchFeild("bankName"));

		accountNumber.clear();
		accountNumber.sendKeys(fetchFeild("accountNumber"));

		IFSCCode.clear();
		IFSCCode.sendKeys(fetchFeild("IFSCCode"));

		chequeUrl.clear();
		chequeUrl.sendKeys(fetchFeild("chequeUrl"));

		next.get(3).click();
	}

	public void createNewVehicle5thPage() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		vehicleRentPerDay.clear();
		vehicleRentPerDay.sendKeys(fetchFeild("vehicleRentPerDay"));

		emmisionUrl.clear();
		emmisionUrl.sendKeys(fetchFeild("emmisionUrl"));

		vehicleRatePerKm.clear();
		vehicleRatePerKm.sendKeys(fetchFeild("vehicleRatePerKm"));

		fitnessUrl.clear();
		fitnessUrl.sendKeys(fetchFeild("fitnessUrl"));

		next.get(4).click();
	}

	public void createNewVehicle6thPage() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		insuranceUpto.clear();
		insuranceUpto.sendKeys(fetchFeild("insuranceUpto"));

		insuranceUrl.clear();
		insuranceUrl.sendKeys(fetchFeild("insuranceUrl"));

		exceptionalUrl.clear();
		exceptionalUrl.sendKeys(fetchFeild("exceptionalUrl"));

		submit.click();
		closeFileInputStream();
	}
}
