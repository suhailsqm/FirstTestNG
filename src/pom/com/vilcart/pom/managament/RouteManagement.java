/**
 * 
 */
package pom.com.vilcart.pom.managament;

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
import org.testng.SkipException;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author win10
 *
 */
/**
 * @author win10
 *
 */
public class RouteManagement {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	private File file;
	private FileInputStream finput;
//	private FileOutputStream fos;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;
	private String[][] data;

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement searchInput;

	@FindBy(id = "addRoute")
	private WebElement addRoute;

	@FindBy(id = "routeName")
	private WebElement routeName;

	@FindBy(id = "executiveName")
	private WebElement executiveName;

	@FindBy(id = "telecallerName")
	private WebElement telecallerName;

	@FindBy(id = "closeBtn")
	private WebElement closeBtn;

	@FindBy(id = "saveBtn")
	private WebElement saveBtn;

	@FindBy(id = "routeTuple")
	private List<WebElement> routeTuples;
	
	@FindBy(xpath = "(//button[normalize-space()='Delete'])[1]")
	private WebElement delete;

	public RouteManagement(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		try {
			file = new File(ReadPropertiesFile.readPropertiesFile().getProperty("resources.CreateRouteMgmt"));
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

	public void searchInRouteMgmt(String key) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(key);
		searchInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	private int fetchData() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		XSSFCell cell3;
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

			rowKey++;

		}
		closeFileInputStream();
		return sheet.getLastRowNum();
	}

	/**
	 * Handling only the first route tuple from fetch.
	 */
	public void createAndValidateRouteMgmt() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		int items = fetchData();
		if (items == 0)
			throw new SkipException("Skipping this exception No data in resources\\\\CreateAssetMgmt.xlsx");

		for (int i = 0; i < items; i++) {
			addRoute.click();
			routeName.clear();
			routeName.sendKeys(data[i][0]);

			WebElement temp = executiveName.findElement(By.xpath("//div/div/div[3]/input"));
			temp.click();
			aw.waitAllRequest();
			List<WebElement> temp1 = executiveName.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
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

			temp = telecallerName.findElement(By.xpath("//div/div/div[3]/input"));
			temp.click();
			aw.waitAllRequest();
			temp1 = telecallerName.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
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

			saveBtn.click();

		}

		for (int i = 0; i < items; i++) {

			searchInRouteMgmt(data[i][0]);
			for (int j = 0; j < routeTuples.size(); j++) {
				if (routeTuples.get(j).findElement(By.xpath("//div/div[1]/div[1]/h4")).getText()
						.equalsIgnoreCase(data[i][0])) {
					break;
				}
				if (j == routeTuples.size() - 1) {
					assertThat(false).withFailMessage("Created Route " + data[i][0] + "Not Matching.").isEqualTo(true);
				}
			}
		}
	}

	public void deleteRoute() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		int items = fetchData();
		if (items == 0)
			throw new SkipException("Skipping this exception No data in resources\\\\CreateAssetMgmt.xlsx");
		
		for (int i = 0; i < items; i++) {
			searchInRouteMgmt(data[i][0]);
			for (int j = 0; j < routeTuples.size(); j++) {
				if (routeTuples.get(j).findElement(By.xpath("//div/div[1]/div[1]/h4")).getText()
						.equalsIgnoreCase(data[i][0])) {					
					routeTuples.get(j).findElement(By.xpath("//div/div[2]/div/button[2]/i")).click();
					delete.click();
					break;
				}
				if (j == routeTuples.size() - 1) {
					assertThat(false).withFailMessage("Created Route " + data[i][0] + "Not Matching.").isEqualTo(true);
				}
			}
		}
	}
}
