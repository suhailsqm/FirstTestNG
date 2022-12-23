/**
 * 
 */
package pom.com.vilcart.pom.goods;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

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
import org.testng.SkipException;

import pom.com.vilcart.pom.inventory.Inventory;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.LineNumber;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

/**
 * @author win10
 *
 */
public class GoodsTransferCreation {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private Inventory inventory;
	private File file;
	private FileInputStream finput;
//	private FileOutputStream fos;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;
	public String DC;
	public String vehicleArg;
	public String[][] data;
	public String[][] updateStockInventory;
	public int itemsCount;
	public int skuCount;
	public String challanNo;

	@FindBy(id = "searchItem")
	private WebElement searchItem;

	@FindBy(id = "itemTuple")
	private List<WebElement> itemTuples;

	@FindBy(xpath = "//*[@id=\"addToCartButton\"]")
	List<WebElement> addToCartButton;

	@FindBy(xpath = "//input[@id=\"countNumber\"]")
	List<WebElement> countNumber;

	@FindBy(xpath = "//button[normalize-space()='Continue']")
	WebElement continueButton;

	@FindBy(id = "transferToDc")
	private WebElement transferToDc;

	@FindBy(id = "transferVehicle")
	private WebElement transferVehicle;

	@FindBy(xpath = "//button[normalize-space()='Transfer']")
	WebElement transferButton;

	@FindBy(xpath = "//*[@id=\"swal2-title\"]")
	WebElement challanNoText;

	@FindBy(xpath = "//button[normalize-space()='OK']")
	WebElement challanOk;

	public GoodsTransferCreation(WebDriver driver, AngularWait aw, String skufile, Inventory inventory) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		this.inventory = inventory;
		this.DC = "";
		this.vehicleArg = "";
		this.itemsCount = 0;
		this.skuCount = 0;
		this.challanNo = "";
		PageFactory.initElements(driver, this);
		try {
			file = new File(skufile);
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

	public void fetchData() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		XSSFCell cell3;
		XSSFCell cell4;
		XSSFCell cell5;
		int rowKey = 0;
		int rowKey1 = 0;
		sheet = workbook.getSheetAt(0);
		cell5 = sheet.getRow(26).getCell(1);
		DC = formatter.formatCellValue(cell5);

		cell5 = sheet.getRow(27).getCell(1);
		vehicleArg = formatter.formatCellValue(cell5);

		cell5 = sheet.getRow(28).getCell(1);
		itemsCount = Integer.parseInt(formatter.formatCellValue(cell5));

		cell5 = sheet.getRow(29).getCell(1);
		skuCount = Integer.parseInt(formatter.formatCellValue(cell5));

		updateStockInventory = new String[skuCount][2];
		data = new String[itemsCount][4];
		for (int i = 31; i <= sheet.getLastRowNum() && rowKey < itemsCount; i++) {
			cell1 = sheet.getRow(i).getCell(0);
			data[rowKey][0] = formatter.formatCellValue(cell1);

			cell2 = sheet.getRow(i).getCell(1);
			data[rowKey][1] = formatter.formatCellValue(cell2);

			cell3 = sheet.getRow(i).getCell(2);
			data[rowKey][2] = formatter.formatCellValue(cell3);

			cell4 = sheet.getRow(i).getCell(3);
			data[rowKey][3] = formatter.formatCellValue(cell4);

			rowKey++;
		}

		for (int i = 31; i <= sheet.getLastRowNum() && rowKey1 < skuCount; i++) {
			cell1 = sheet.getRow(i).getCell(7);
			updateStockInventory[rowKey1][0] = formatter.formatCellValue(cell1);

			cell2 = sheet.getRow(i).getCell(8);
			updateStockInventory[rowKey1][1] = formatter.formatCellValue(cell2);

			rowKey1++;
		}
		closeFileInputStream();

		for (int i = 0; i < data.length; i++) {
			Reporter.log(data[i][0] + " ", true);
			Reporter.log(data[i][1] + " ", true);
			Reporter.log(data[i][2] + " ", true);
			Reporter.log(data[i][3] + " ", true);
		}

	}

	private void searchCreateTransferGoods(String skuName) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchItem.clear();
		searchItem.sendKeys(skuName);
		searchItem.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	public void createGoodsTransfer(String dc, String vehicle) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
//		int items = fetchData();
		if (this.itemsCount == 0)
			throw new SkipException("Skipping this exception No data in resources\\Goods\\GoodsTransfer.xlsx");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		for (int i = 0; i < this.itemsCount; i++) {
			searchCreateTransferGoods(data[i][1]);
			if (itemTuples.size() == 0) {
				assertThat(false).withFailMessage("No tuple with search Criteria " + data[i][1]).isEqualTo(true);
			}
			// only First tuple is considered.
			String skuName = itemTuples.get(0).findElement(By.xpath(".//td/div/div[1]/div[1]")).getText().split("\n")[0]
					.trim();
			assertThat(data[i][1])
					.withFailMessage(
							"retrieved tuple with search Criteria " + data[i][1] + " doesn't match required " + skuName)
					.isEqualToIgnoringCase(skuName);
			String xpath = ".//td/div/div[1]/div[2]/div/button";
			WebElement list = itemTuples.get(0).findElement(By.xpath(xpath));
			list.click();

			String xpath1 = ".//td/div/div[1]/div[2]/div/div/button";
			List<WebElement> listSelectDropDown = itemTuples.get(0).findElements(By.xpath(xpath1));
			int numOfVariations = listSelectDropDown.size();
			Reporter.log(xpath1 + "----" + numOfVariations, true);
			for (int i1 = 0; i1 < listSelectDropDown.size(); i1++) {
				WebElement varSelectDropDown = listSelectDropDown.get(i1);
				Reporter.log("----" + varSelectDropDown.getText(), true);
				if (varSelectDropDown.getText().trim().equalsIgnoreCase(data[i][2])) {
					assertThat(data[i][2]).withFailMessage("No variation with search Criteria " + data[i][2])
							.isEqualToIgnoringCase(varSelectDropDown.getText().trim());

					varSelectDropDown.click();
					aw.waitAllRequest();

					WebElement varInputCount = countNumber.get(0);
					varInputCount.clear();
					varInputCount.sendKeys("" + data[i][3]);
					aw.waitAllRequest();

					WebElement varAddToCart = addToCartButton.get(0);
					varAddToCart.click();
					aw.waitAllRequest();
					break;
				}
				if (i1 == listSelectDropDown.size() - 1) {
					assertThat(data[i][2]).withFailMessage("No variation with search Criteria " + data[i][2])
							.isEqualToIgnoringCase(varSelectDropDown.getText().trim());
				}
			}
		}
		// Toastr message lasts for 2 seconds, so putting a wait for 3 seconds.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		WebDriverWait wait1;
		wait1 = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait1.until(ExpectedConditions.elementToBeClickable(continueButton));
		continueButton.click();
		aw.waitAllRequest();

		WebElement input = transferToDc.findElement(By.xpath(".//div/div/div[3]/input"));
		input.click();
		aw.waitAllRequest();
		List<WebElement> input1 = transferToDc.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < input1.size(); i++) {
			if (input1.get(i).findElement(By.xpath(".//span")).getText().equalsIgnoreCase(this.DC)) {
				input1.get(i).findElement(By.xpath(".//span")).click();
				break;
			}
			if (i == input1.size() - 1) {
				assertThat(false).withFailMessage("No specific '" + this.DC + "' in list.").isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		WebElement number = transferVehicle.findElement(By.xpath(".//div/div/div[3]/input"));
		number.click();
		aw.waitAllRequest();
		List<WebElement> number1 = transferVehicle.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < number1.size(); i++) {
			if (number1.get(i).findElement(By.xpath(".//span")).getText().equalsIgnoreCase(this.vehicleArg)) {
				number1.get(i).findElement(By.xpath(".//span")).click();
				break;
			}
			if (i == number1.size() - 1) {
				assertThat(false).withFailMessage("No specific '" + this.vehicleArg + "' in list.").isEqualTo(true);
			}
		}
		aw.waitAllRequest();
		String handle = driver.getWindowHandle();
		Reporter.log("Window not supposed to close is " + handle, true);
		transferButton.click();
		handle = driver.getWindowHandle();
		Reporter.log("Window not supposed to close is " + handle, true);
		aw.waitAllRequest();
		handle = driver.getWindowHandle();
		Reporter.log("Window not supposed to close is " + handle, true);

		WebDriverWait wait;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		try {
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			WebElement cancel = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//*[@id=\"sidebar\"]//print-preview-button-strip//div/cr-button[2]")));
			cancel.click();
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		} catch (org.openqa.selenium.TimeoutException e) {
			Reporter.log("timeOut Exception", true);
			js.executeScript("window.stop();");
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		}
		try {
			Set<String> handles = driver.getWindowHandles();
			for (String actual : handles) {
				if (0 != actual.compareToIgnoreCase(handle)) {
					Reporter.log(LineNumber.getLineNumber() + " close " + actual, true);
					try {
						driver.switchTo().window(actual);
						Reporter.log("title is " + driver.getTitle(), true);
						driver.close();
//					driver.switchTo().window(actual).close();

					} catch (org.openqa.selenium.WebDriverException e) {
						Reporter.log("unknown error: failed to close window in 20 seconds Exception", true);
						js.executeScript("window.close();");
					}
				}
			}
			driver.switchTo().window(handle);
			aw.waitAllRequest();
		} catch (org.openqa.selenium.NoSuchWindowException e) {
			driver.switchTo().window(handle);
			aw.waitAllRequest();
		}
		challanNo = challanNoText.getText().split(":")[1].trim();
		challanOk.click();
		aw.waitAllRequest();
	}
}
