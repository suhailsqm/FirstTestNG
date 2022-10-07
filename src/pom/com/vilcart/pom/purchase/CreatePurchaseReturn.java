/**
 * 
 */
package pom.com.vilcart.pom.purchase;

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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author win10
 *
 */
public class CreatePurchaseReturn {
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

	@FindBy(id = "searchTuples")
	private List<WebElement> searchTuples;

	@FindBy(xpath = "//*[@id=\"addToCartButton\"]")
	List<WebElement> addToCartButton;

	@FindBy(xpath = "//input[@id=\"countNumber\"]")
	List<WebElement> countNumber;

	@FindBy(xpath = "//button[text()='Continue']")
	WebElement continueButton;

	@FindBy(id = "vendorName")
	private WebElement vendorName;

	@FindBy(id = "poNumber")
	private WebElement poNumber;

	@FindBy(xpath = "//button[text()='Return']")
	WebElement returnButton;
	
	public CreatePurchaseReturn(WebDriver driver, AngularWait aw) {
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
		return sheet.getLastRowNum() + 1;
	}

	private void searchCreatePurchaseReturn(String skuName) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(skuName);
		searchInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	public void selectpurchaseToReturn(String poNumberArg, String vendorNameArg) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		int items = fetchData();
		for (int i = 0; i < items; i++) {
			searchCreatePurchaseReturn(data[i][0]);
			// only First tuple is considered.
			String skuName = searchTuples.get(0).findElement(By.xpath("//td/div/div[1]/div[1]")).getText()
					.split("\n")[0].trim();
			assertThat(data[i][0]).withFailMessage("No tuple with search Criteria " + data[i][0])
					.isEqualToIgnoringCase(skuName);
			String xpath = "//td/div/div[1]/div[2]/div/button";
			WebElement list = searchTuples.get(0).findElement(By.xpath(xpath));
			list.click();

			String xpath1 = "//td/div/div[1]/div[2]/div/div/button";
			List<WebElement> listSelectDropDown = searchTuples.get(0).findElements(By.xpath(xpath1));
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

		WebElement name = vendorName.findElement(By.xpath("//div/div/div[3]/input"));
		name.click();
		aw.waitAllRequest();
		List<WebElement> name1 = vendorName.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < name1.size(); i++) {
			if (name1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(vendorNameArg)) {
				name1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == name1.size() - 1) {
				assertThat(false).withFailMessage("No specific '" + vendorNameArg + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();
		
		WebElement number = poNumber.findElement(By.xpath("//div/div/div[3]/input"));
		number.click();
		aw.waitAllRequest();
		List<WebElement> number1 = poNumber.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int i = 0; i < number1.size(); i++) {
			if (number1.get(i).findElement(By.xpath("//span")).getText().equalsIgnoreCase(poNumberArg)) {
				number1.get(i).findElement(By.xpath("//span")).click();
				break;
			}
			if (i == number1.size() - 1) {
				assertThat(false).withFailMessage("No specific '" + poNumberArg + "' in workbook resource.")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		returnButton.click();
		aw.waitAllRequest();
	}

}
