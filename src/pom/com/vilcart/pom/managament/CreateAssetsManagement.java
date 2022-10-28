/**
 * 
 */
package pom.com.vilcart.pom.managament;

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
import org.testng.SkipException;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

/**
 * @author win10
 *
 */
public class CreateAssetsManagement {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	private File file;
	private FileInputStream finput;
//	private FileOutputStream fos;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;
	private String[][] data;

	@FindBy(id = "fileInput")
	private WebElement fileInput;

	@FindBy(id = "category")
	private WebElement category;

	@FindBy(id = "product")
	private WebElement product;

	@FindBy(id = "owner")
	private WebElement owner;

	@FindBy(id = "assignee")
	private WebElement assignee;

	@FindBy(id = "number")
	private WebElement number;

	@FindBy(id = "description")
	private WebElement description;

	@FindBy(xpath = "(//button[normalize-space()='Save'])[1]")
	private WebElement save;

	public CreateAssetsManagement(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		try {
			file = new File(ReadPropertiesFile.readPropertiesFile().getProperty("resources.createAssetMgmt"));
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
		XSSFCell cell5;
		XSSFCell cell6;
		XSSFCell cell7;
		int rowKey = 0;

		sheet = workbook.getSheetAt(0);
		data = new String[sheet.getLastRowNum() + 1][4];
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			cell1 = sheet.getRow(i).getCell(0);
			data[rowKey][0] = formatter.formatCellValue(cell1);

			cell2 = sheet.getRow(i).getCell(1);
			data[rowKey][1] = formatter.formatCellValue(cell2);

			cell3 = sheet.getRow(i).getCell(2);
			data[rowKey][2] = formatter.formatCellValue(cell3);

			cell4 = sheet.getRow(i).getCell(3);
			data[rowKey][3] = formatter.formatCellValue(cell4);

			cell5 = sheet.getRow(i).getCell(4);
			data[rowKey][4] = formatter.formatCellValue(cell5);

			cell6 = sheet.getRow(i).getCell(5);
			data[rowKey][5] = formatter.formatCellValue(cell6);

			cell7 = sheet.getRow(i).getCell(6);
			data[rowKey][6] = formatter.formatCellValue(cell7);
			
			rowKey++;
		}
		closeFileInputStream();
		return sheet.getLastRowNum();
	}

	public void createAssetMgmt() {
		int items = fetchData();
		if (items == 0)
			throw new SkipException("Skipping this exception No data in resources\\\\CreateAssetMgmt.xlsx");

		for (int i = 0; i < items; i++) {
			fileInput.sendKeys(data[i][0]);

			WebElement temp = category.findElement(By.xpath("//div/div/div[3]/input"));
			temp.click();
			aw.waitAllRequest();
			List<WebElement> temp1 = category.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
			for (int j = 0; j < temp1.size(); j++) {
				if (temp1.get(j).findElement(By.xpath("//span")).getText().equalsIgnoreCase(data[i][1])) {
					temp1.get(j).findElement(By.xpath("//span")).click();
					break;
				}
				if (j == temp1.size() - 1) {
					assertThat(false).withFailMessage("No specific '" + data[i][1] + "' in workbook resource.")
							.isEqualTo(true);
				}
			}
			aw.waitAllRequest();

			temp = product.findElement(By.xpath("//div/div/div[3]/input"));
			temp.click();
			aw.waitAllRequest();
			temp1 = product.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
			for (int j = 0; j < temp1.size(); j++) {
				if (temp1.get(j).findElement(By.xpath("//span")).getText().equalsIgnoreCase(data[i][2])) {
					temp1.get(j).findElement(By.xpath("//span")).click();
					break;
				}
				if (j == temp1.size() - 1) {
					assertThat(false).withFailMessage("No specific '" + data[i][2] + "' in workbook resource.")
							.isEqualTo(true);
				}
			}
			aw.waitAllRequest();

			temp = owner.findElement(By.xpath("//div/div/div[3]/input"));
			temp.click();
			aw.waitAllRequest();
			temp1 = owner.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
			for (int j = 0; j < temp1.size(); j++) {
				if (temp1.get(j).findElement(By.xpath("//span")).getText().equalsIgnoreCase(data[i][3])) {
					temp1.get(j).findElement(By.xpath("//span")).click();
					break;
				}
				if (j == temp1.size() - 1) {
					assertThat(false).withFailMessage("No specific '" + data[i][3] + "' in workbook resource.")
							.isEqualTo(true);
				}
			}
			aw.waitAllRequest();

			temp = assignee.findElement(By.xpath("//div/div/div[3]/input"));
			temp.click();
			aw.waitAllRequest();
			temp1 = assignee.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
			for (int j = 0; j < temp1.size(); j++) {
				if (temp1.get(j).findElement(By.xpath("//span")).getText().equalsIgnoreCase(data[i][4])) {
					temp1.get(j).findElement(By.xpath("//span")).click();
					break;
				}
				if (j == temp1.size() - 1) {
					assertThat(false).withFailMessage("No specific '" + data[i][4] + "' in workbook resource.")
							.isEqualTo(true);
				}
			}
			aw.waitAllRequest();

			number.clear();
			number.sendKeys(data[i][5]);

			description.clear();
			description.sendKeys(data[i][6]);
			
			save.click();
			aw.waitAllRequest();
			//TODO to see if any popups are coming

		}

	}
}
