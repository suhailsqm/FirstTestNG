/**
 * 
 */
package pom.com.vilcart.pom.goods;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.testng.Reporter;
import org.testng.SkipException;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.LineNumber;
import util.com.vilcart.util.TimeStamp;

/**
 * @author win10
 *
 */
public class GoodsReceiveCreation {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private File file;
	private FileInputStream finput;
//	private FileOutputStream fos;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;
	private String[][] data;

	@FindBy(id = "searchItem")
	private WebElement searchItem;

	@FindBy(id = "itemTuple")
	private List<WebElement> itemTuples;

	@FindBy(xpath = "//*[@id=\"addToCartButton\"]")
	List<WebElement> addToCartButton;

	@FindBy(xpath = "//input[@id=\"countNumber\"]")
	List<WebElement> countNumber;

	@FindBy(xpath = "//button[text()='Continue']")
	WebElement continueButton;

	@FindBy(id = "receiveFromDc")
	private WebElement receiveFromDc;

	@FindBy(id = "transferVehicle")
	private WebElement transferVehicle;

	@FindBy(id = "challanNo")
	private WebElement challanNo;

	@FindBy(xpath = "//button[text()='Receive']")
	WebElement receiveBtn;

	public GoodsReceiveCreation(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		file = new File("resources\\GoodsReceive.xlsx");
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

	private int fetchData() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		XSSFCell cell3;
		XSSFCell cell4;
		sheet = workbook.getSheetAt(0);
		data = new String[sheet.getLastRowNum() + 1][4];
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
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
		return sheet.getLastRowNum() - 1;
	}

	private void searchCreateReceiveGoods(String skuName) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchItem.clear();
		searchItem.sendKeys(skuName);
		searchItem.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	public void createGoodsReceive(String dc, String vehicle, String challanNoArg) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		int items = fetchData();
		if (items == 0)
			throw new SkipException("Skipping this exception No data in resources\\\\GoodsReceive.xlsx");
		for (int i = 0; i < items; i++) {
			searchCreateReceiveGoods(data[i][0]);
			// only First tuple is considered.
			String skuName = itemTuples.get(0).findElement(By.xpath("//td/div/div[1]/div[1]")).getText().split("\n")[0]
					.trim();
			assertThat(data[i][0]).withFailMessage("No tuple with search Criteria " + data[i][0])
					.isEqualToIgnoringCase(skuName);
			String xpath = "//td/div/div[1]/div[2]/div/button";
			WebElement list = itemTuples.get(0).findElement(By.xpath(xpath));
			list.click();

			String xpath1 = "//td/div/div[1]/div[2]/div/div/button";
			List<WebElement> listSelectDropDown = itemTuples.get(0).findElements(By.xpath(xpath1));
			int numOfVariations = listSelectDropDown.size();
			Reporter.log(xpath1 + "----" + numOfVariations, true);
			for (int i1 = 0; i1 < listSelectDropDown.size(); i1++) {
				WebElement varSelectDropDown = listSelectDropDown.get(i1);
				Reporter.log("----" + varSelectDropDown.getText(), true);
				if (varSelectDropDown.getText().trim().equalsIgnoreCase(data[i][1])) {
					assertThat(data[i][1]).withFailMessage("No variation with search Criteria " + data[i][1])
							.isEqualToIgnoringCase(varSelectDropDown.getText().trim());

					varSelectDropDown.click();
					aw.waitAllRequest();

					WebElement varInputCount = countNumber.get(0);
					varInputCount.clear();
					varInputCount.sendKeys("" + data[i][2]);
					aw.waitAllRequest();

					WebElement receivePrice = itemTuples.get(0).findElement(By.xpath("//td/div/div[2]/div[1]/input"));
					receivePrice.clear();
					receivePrice.sendKeys("" + data[i][3]);

					WebElement varAddToCart = addToCartButton.get(0);
					varAddToCart.click();
					aw.waitAllRequest();
					break;
				}
				if (i1 == listSelectDropDown.size() - 1) {
					assertThat(data[i][1]).withFailMessage("No variation with search Criteria " + data[i][1])
							.isEqualToIgnoringCase(varSelectDropDown.getText().trim());
				}
			}
		}
		continueButton.click();
		aw.waitAllRequest();

		WebElement input = receiveFromDc.findElement(By.xpath("//div/div/div[3]/input"));
		input.click();
		aw.waitAllRequest();
		List<WebElement> input1 = receiveFromDc.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < input1.size(); i++) {
			if (input1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(dc)) {
				input1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == input1.size() - 1) {
				assertThat(false).withFailMessage("No specific '" + dc + "' in list.").isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		WebElement number = transferVehicle.findElement(By.xpath("//div/div/div[3]/input"));
		number.click();
		aw.waitAllRequest();
		List<WebElement> number1 = transferVehicle.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < number1.size(); i++) {
			if (number1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(vehicle)) {
				number1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == number1.size() - 1) {
				assertThat(false).withFailMessage("No specific '" + vehicle + "' in list.").isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		challanNo.clear();
		challanNo.sendKeys(challanNoArg);

		String handle = driver.getWindowHandle();
		receiveBtn.click();
		aw.waitAllRequest();
		Set<String> handles = driver.getWindowHandles();
		for (String actual : handles) {
			if (0 != actual.compareToIgnoreCase(handle)) {
				Reporter.log(LineNumber.getLineNumber() + "close " + actual, true);
				driver.switchTo().window(actual).close();
			}
		}
		driver.switchTo().window(handle);
		aw.waitAllRequest();
	}

}
