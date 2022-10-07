/**
 * 
 */
package pom.com.vilcart.pom.purchase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

/**
 * @author win10
 *
 */
public class SelectVendor {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private File file;
	private FileInputStream finput;
//	private FileOutputStream fos;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;
	private String[][] data;

	@FindBy(id = "searchInput")
	private WebElement searchInput;

	@FindBy(xpath = "//*[@id=\"vendorListTuple\"]")
	private List<WebElement> vendorListTuples;

	@FindBy(id = "grnDate")
	private WebElement grnDate;

	@FindBy(id = "invoiceDate")
	private WebElement invoiceDate;

	@FindBy(id = "invoiceNumber")
	private WebElement invoiceNumber;

	@FindBy(id = "backBtn")
	private WebElement backBtn;

	@FindBy(id = "editVendorBtn")
	private WebElement editVendorBtn;

	@FindBy(id = "nextBtn")
	private WebElement nextBtn;

	@FindBy(xpath = "//*[@id=\"orderListTuple\"]")
	private List<WebElement> orderListTuples;

	@FindBy(xpath = "//*[@id=\"fileInput\"]")
	private WebElement uploadReceipt;

	@FindBy(id = "updateBtn")
	private WebElement updateBtn;

	public SelectVendor(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		file = new File("resources\\purchaseDummy.xlsx");
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

	private void searchVendor(String vendorName) {
		searchInput.sendKeys(vendorName);
		searchInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	private String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}

	private void fetchData() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		XSSFCell cell3;
		XSSFCell cell4;
		sheet = workbook.getSheetAt(0);
		data = new String[sheet.getLastRowNum() + 1][4];
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			cell1 = sheet.getRow(i).getCell(0);
			data[i][0] = formatter.formatCellValue(cell1);

			cell2 = sheet.getRow(i).getCell(1);
			data[i][1] = formatter.formatCellValue(cell2);

			cell3 = sheet.getRow(i).getCell(2);
			data[i][2] = formatter.formatCellValue(cell1);

			cell4 = sheet.getRow(i).getCell(3);
			data[i][3] = formatter.formatCellValue(cell2);
		}
		closeFileInputStream();
	}

	public void selectVendorInPurchaseList(String vendorName, String invoiceNumberArg) {
		searchVendor(vendorName);
		assertThat(vendorListTuples.size())
				.withFailMessage("no tuples in Select Vendor in purchase List for VendorName " + vendorName)
				.isGreaterThan(0);
		for (int i = 0; i < vendorListTuples.size(); i++) {
			String vendorNameInTupleString = vendorListTuples.get(i).findElement(By.xpath("//td")).getText();
			String name = vendorNameInTupleString.split("|")[0];
			if (name.trim().equalsIgnoreCase(vendorName)) {
				WebElement temp = vendorListTuples.get(i).findElement(By.xpath("//td"));
				js.executeScript("arguments[0].scrollIntoViewIfNeeded();", temp);
				temp.click();
				aw.waitAllRequest();
				break;
			}
		}

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				grnDate);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				invoiceDate);

		invoiceNumber.sendKeys(invoiceNumberArg);

		nextBtn.click();
		aw.waitAllRequest();

		fetchData();

		assertThat(orderListTuples.size()).withFailMessage("no tuples in orders in purchase List").isGreaterThan(0);
		for (int i = 0; i < orderListTuples.size(); i++) {
			String tupleOrderName = orderListTuples.get(i).findElement(By.xpath("//td[2]")).getText().split("\n")[0]
					.trim();
			assertThat(tupleOrderName).withFailMessage("skuName not matching").isEqualToIgnoringCase(data[i][0]);

			String tupleVariationName = orderListTuples.get(i).findElement(By.xpath("//td[4]")).getText().trim();
			assertThat(tupleVariationName).withFailMessage("variation Name not matching")
					.isEqualToIgnoringCase(data[i][1]);

			WebElement countWE = orderListTuples.get(i).findElement(By.xpath("//td[8]/input"));
			countWE.sendKeys(data[i][2]);

			WebElement rateWE = orderListTuples.get(i).findElement(By.xpath("//td[9]/input"));
			rateWE.sendKeys(data[i][3]);
		}
		uploadReceipt.sendKeys("resources\\receipt.png");
		aw.waitAllRequest();
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", updateBtn);
		updateBtn.click();
		aw.waitAllRequest();
	}

	public void closeFileInputStream() {
		try {
			finput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public void closeFileOutputStream() { try { fos.close(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 */
}
