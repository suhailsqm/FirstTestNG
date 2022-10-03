package pom.com.vilcart.pom.sku;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

public class Sku {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
	private int numberOfVariations;
	private int maxNumberOfVariations = 4;

	private File file;
	private FileInputStream finput;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement search;

	@FindBy(xpath = "//button[normalize-space(.)='Add SKU']")
	private WebElement addSKU;

	@FindBy(id = "itemHSNCode")
	private WebElement itemHSNCode;

	@FindBy(id = "skuName")
	private WebElement skuName;

	@FindBy(id = "localName")
	private WebElement localName;

	@FindBy(id = "description")
	private WebElement description;

	@FindBy(id = "category")
	private WebElement category;

	@FindBy(id = "subCategory")
	private WebElement subCategory;

	@FindBy(id = "gstRate")
	private WebElement gstRate;

	@FindBy(id = "cessAmount")
	private WebElement cessAmount;

	@FindBy(id = "amountType")
	private WebElement amountType;

	@FindBy(id = "brandName")
	private WebElement brandName;

	@FindBy(id = "variationButton")
	private WebElement variationButton;

	@FindBy(id = "variationName")
	private List<WebElement> variationName;

	@FindBy(id = "price")
	private List<WebElement> price;

	@FindBy(id = "valueText")
	private List<WebElement> valueText;

	@FindBy(id = "saveButton")
	private WebElement saveButton;

	@FindBy(id = "skuListTuple")
	List<WebElement> skuListTuples;

	public Sku(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		file = new File("resources\\SKU.xlsx");
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
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
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

	/*
	 * Index in this function is 1 based, not 0 based.
	 */
	private String fetchFeildWithIndex(String feild, int index) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		int temp = 0;
		sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			cell1 = sheet.getRow(i).getCell(0);
			String value = formatter.formatCellValue(cell1);
			if (feild.contentEquals(value)) {
				temp++;
				if (temp == index) {
					cell2 = sheet.getRow(i).getCell(1);
					String value2 = formatter.formatCellValue(cell2);
					return value2;
				}
			}
		}
		assertThat(false).withFailMessage("No specific '" + feild + "' in workbook resource.").isEqualTo(true);
		return null;
	}

	public void searchSku(String keyword) {
		search.clear();
		search.sendKeys(keyword);
		aw.waitAllRequest();
	}

	public String createSku() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		addSKU.click();
		aw.waitAllRequest();

		itemHSNCode.clear();
		itemHSNCode.sendKeys(fetchFeild("itemHSNCode"));

		skuName.clear();
		skuName.sendKeys(fetchFeild("skuName"));

		localName.clear();
		localName.sendKeys(fetchFeild("localName"));

		description.clear();
		description.sendKeys(fetchFeild("description"));

		category.findElement(By.xpath("//div/div/div[3]/input")).click();
		List<WebElement> categoryList = category.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (int j = 0; j < categoryList.size(); j++) {
			WebElement option = categoryList.get(j).findElement(By.xpath("//span"));
			if (option.getText().equalsIgnoreCase(fetchFeild("category"))) {
				option.click();
				break;
			}
			if (j == categoryList.size()) {
				assertThat(false)
						.withFailMessage("given category " + fetchFeild("category") + " not present in category List")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", subCategory);
		subCategory.findElement(By.xpath("//div/div/div[2]/input")).click();
		List<WebElement> subCategoryList = subCategory.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		int j;
		for (j = 0; j < subCategoryList.size(); j++) {
			WebElement option = subCategoryList.get(j).findElement(By.xpath("//span"));
			if (option.getText().equalsIgnoreCase(fetchFeild("subCategory"))) {
				option.click();
				break;
			}
		}
		if (j == subCategoryList.size()) {
			assertThat(false)
					.withFailMessage(
							"given subCategory " + fetchFeild("subCategory") + " not present in subCategory List")
					.isEqualTo(true);
		}

		gstRate.clear();
		gstRate.sendKeys(fetchFeild("gstRate"));

		cessAmount.clear();
		cessAmount.sendKeys(fetchFeild("fetchFeild"));

		amountType.findElement(By.xpath("//div/div/div[3]/input")).click();
		List<WebElement> subAmountType = amountType.findElements(By.xpath("//ng-dropdown-panel/div/div[2]/div"));
		for (j = 0; j < subAmountType.size(); j++) {
			WebElement option = subAmountType.get(j).findElement(By.xpath("//span"));
			if (option.getText().equalsIgnoreCase(fetchFeild("amountType"))) {
				option.click();
				break;
			}
		}
		if (j == subAmountType.size()) {
			assertThat(false)
					.withFailMessage("given amountType " + fetchFeild("amountType") + " not present in amountType List")
					.isEqualTo(true);
		}

		brandName.findElement(By.xpath("//div/div/div[2]/input"));
		js.executeScript(
				"arguments[0].value='" + fetchFeild("amountType")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				brandName);

		this.numberOfVariations = Integer.parseInt("numberOfVariations");
		assertThat(this.numberOfVariations).withFailMessage("Number of variations cannot be greater than 4")
				.isGreaterThan(numberOfVariations);
		for (int k = 0; k < this.numberOfVariations; k++) {
			WebElement variationButton = driver.findElement(By.xpath("//*[@id=\"variationButton\"]"));
			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", variationButton);
			variationButton.click();

			variationName.get(k).clear();
			variationName.get(k).sendKeys(fetchFeildWithIndex("variationName", k + 1));
			Reporter.log(variationName.get(k).getAttribute("value"), true);

			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", price.get(k));
			price.get(k).clear();
			price.get(k).sendKeys(fetchFeildWithIndex("price", k + 1));
			Reporter.log(variationName.get(k).getAttribute("value"), true);
			// WebElement unit = driver.findElement(By.xpath("//*[@id=\"unitText\"]"));
			// unit.sendKeys("1");

			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", valueText.get(k));
			valueText.get(k).clear();
			valueText.get(k).sendKeys(fetchFeildWithIndex("valueText", k + 1));
			Reporter.log(valueText.get(k).getAttribute("value"), true);
		}

////		FileOutputStream fos = new FileOutputStream("resources\\SKU.xlsx");
////		workbook.write(fos);
//		fos.close();
//		closeFileInputStream();

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", saveButton);
		saveButton.click();
		aw.waitAllRequest();

		return fetchFeild("skuName");
	}

	public void deleteFirstSku(String skuName) {
		searchSku(skuName);
		assertThat(skuListTuples.size()).withFailMessage("No Tuples in sku List for the search criteria " + skuName)
				.isGreaterThan(0);
		int i;
		for (i = 0; i < skuListTuples.size(); i++) {
			if (skuListTuples.get(i).findElement(By.xpath("//td[6]")).getText().equalsIgnoreCase(skuName)) {

			}
		}
		if (i == skuListTuples.size()) {
			assertThat(false).withFailMessage("No Sku with name " + skuName + " in the searched tuples")
					.isEqualTo(true);
		}
	}
}
