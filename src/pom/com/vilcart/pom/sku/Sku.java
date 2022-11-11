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
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

public class Sku {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	private WebDriverWait wait;
	private int[] numberOfVariations;
	public static final int maxNumberOfVariations = 4;
	public static final int dataLengthOn1Variation = 14;
	public static final int dataLengthOn2Variation = 17;
	public static final int dataLengthOn3Variation = 20;
	public static final int dataLengthOn4Variation = 23;
	public int numberOfSku = 0;
	public String[][] data;
	// data length is 0 indexed based value with sku as index
	public int[] dataLength;
	// 0 based for any SKU variation index is common.
	public int variationIndex;
	public String[][] skuNameArray;
	public boolean dataCollected = false;

	private String fileName;
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
	private List<WebElement> skuListTuples;

	@FindBy(xpath = "(//button[normalize-space()='Yes, delete it!'])[1]")
	private WebElement deleteConfirmation;

	@FindBy(id = "variationCard")
	private List<WebElement> variationCards;

	public Sku(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		try {
			fileName = ReadPropertiesFile.readPropertiesFile().getProperty("resources.sku");
			file = new File(ReadPropertiesFile.readPropertiesFile().getProperty("resources.sku"));
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

	public Sku(WebDriver driver, AngularWait aw, String skufile) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		try {
			fileName = skufile;
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

	private String fetchField(String field) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			cell1 = sheet.getRow(i).getCell(0);
			String value = formatter.formatCellValue(cell1);
			if (field.contentEquals(value)) {
				cell2 = sheet.getRow(i).getCell(1);
				String value2 = formatter.formatCellValue(cell2);
				return value2;
			}
		}
		assertThat(false).withFailMessage("No specific '" + field + "' in workbook resource\"" + fileName + "\".")
				.isEqualTo(true);
		return null;
	}

	/*
	 * Index in this function is 1 based, not 0 based. Index is the iteration of
	 * Occurrence of the variant field.
	 */
	private String fetchField(String field, int index) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		int temp = 0;
		sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			cell1 = sheet.getRow(i).getCell(0);
			String value = formatter.formatCellValue(cell1);
			if (field.contentEquals(value)) {
				temp++;
				if (temp == index) {
					cell2 = sheet.getRow(i).getCell(1);
					String value2 = formatter.formatCellValue(cell2);
					return value2;
				}
			}
		}
		assertThat(false).withFailMessage("No specific '" + field + "' in workbook resource\"" + fileName + "\".")
				.isEqualTo(true);
		return null;
	}

	/*
	 * field is the field index is the iteration of the field per SKU used for
	 * multiple variants skuIndex is 0 based for SKU count. 0 for First SKU and 1
	 * for 2nd SKU etc...
	 */
	private String fetchField(String field, int index, int skuIndex) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cell1;
		XSSFCell cell2;
		int temp = 0;
		sheet = workbook.getSheetAt(0);
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			cell1 = sheet.getRow(i).getCell(0);
			String value = formatter.formatCellValue(cell1);
			if (field.contentEquals(value)) {
				temp++;
				if (temp == index) {
					cell2 = sheet.getRow(i).getCell(skuIndex + 1);
					String value2 = formatter.formatCellValue(cell2);
					return value2;
				}
			}
		}
		assertThat(false).withFailMessage("No specific '" + field + "' in workbook resource\"" + fileName + "\".")
				.isEqualTo(true);
		return null;
	}

	public String[][] getExcelData() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		String value = fetchField("NoOfSku");
		if (Integer.parseInt(value) <= 0) {
			assertThat(false)
					.withFailMessage("no of SKU value is  '" + value + "' in workbook resource\"" + fileName + "\".")
					.isEqualTo(true);
		}
		numberOfSku = Integer.parseInt(value);
		// 23 is the max number of Fields in the file from which SKU is read.
		data = new String[Integer.parseInt(value)][23];
		dataLength = new int[Integer.parseInt(value)];
		skuNameArray = new String[Integer.parseInt(value)][1];

		for (int i = 0; i < Integer.parseInt(value); i++) {
			int key = 0;
			data[i][key++] = fetchField("itemHSNCode", 1, i);
			data[i][key++] = fetchField("skuName", 1, i);
			skuNameArray[i][0] = fetchField("skuName", 1, i);

			data[i][key++] = fetchField("localName", 1, i);
			data[i][key++] = fetchField("description", 1, i);
			data[i][key++] = fetchField("category", 1, i);
			data[i][key++] = fetchField("subCategory", 1, i);
			data[i][key++] = fetchField("gstRate", 1, i);
			data[i][key++] = fetchField("cessAmount", 1, i);
			data[i][key++] = fetchField("amountType", 1, i);
			data[i][key++] = fetchField("brandName", 1, i);
			data[i][key] = fetchField("numberOfVariations", 1, i);
			variationIndex = key;
			key++;

			int numberOfVariation = Integer.parseInt(fetchField("numberOfVariations", 1, i));
			assertThat(numberOfVariation).withFailMessage("Number of variations cannot be greater than 4")
					.isLessThanOrEqualTo(maxNumberOfVariations);
			assertThat(numberOfVariation).withFailMessage("Number of variations should be greater than 0")
					.isGreaterThan(0);

			for (int j = 0; j < numberOfVariation; j++) {
				data[i][key++] = fetchField("variationName", j + 1, i);
				data[i][key++] = fetchField("price", j + 1, i);
				data[i][key++] = fetchField("valueText", j + 1, i);
			}
			dataLength[i] = key - 1;
		}

		for (int i = 0; i < numberOfSku; i++) {
			switch (dataLength[i]) {
			case Sku.dataLengthOn1Variation:
				Reporter.log("dataLength of size \"" + Sku.dataLengthOn1Variation + "\"", true);
				break;
			case Sku.dataLengthOn2Variation:
				Reporter.log("dataLength of size \"" + Sku.dataLengthOn2Variation + "\"", true);
				break;
			case Sku.dataLengthOn3Variation:
				Reporter.log("dataLength of size \"" + Sku.dataLengthOn3Variation + "\"", true);
				break;
			case Sku.dataLengthOn4Variation:
				Reporter.log("dataLength of size \"" + Sku.dataLengthOn4Variation + "\"", true);
				break;
			default:
				Reporter.log("dataLength of size \"" + dataLength[i] + "\" Which doesn't fit into standard", true);
				assertThat(false)
						.withFailMessage("dataLength of size \"" + dataLength[i] + "\" Which doesn't fit into standard")
						.isEqualTo(true);
			}
		}

		dataCollected = true;
		return data;
	}

	public void searchSku(String keyword) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		search.clear();
		search.sendKeys(keyword);
		aw.waitAllRequest();
	}

	public String createSku() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		int skuIndex = 0;
		addSKU.click();
		aw.waitAllRequest();

		itemHSNCode.clear();
		itemHSNCode.sendKeys(fetchField("itemHSNCode"));

		skuName.clear();
		skuName.sendKeys(fetchField("skuName"));

		localName.clear();
		localName.sendKeys(fetchField("localName"));

		description.clear();
		description.sendKeys(fetchField("description"));

		category.findElement(By.xpath(".//div/div/div[3]/input")).click();
		List<WebElement> categoryList = category.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
		for (int j = 0; j < categoryList.size(); j++) {
			WebElement option = categoryList.get(j).findElement(By.xpath(".//span"));
			if (option.getText().equalsIgnoreCase(fetchField("category"))) {
				option.click();
				break;
			}
			if (j == categoryList.size()) {
				assertThat(false)
						.withFailMessage("given category " + fetchField("category") + " not present in category List")
						.isEqualTo(true);
			}
		}
		aw.waitAllRequest();

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", subCategory);
		subCategory.findElement(By.xpath(".//div/div/div[2]/input")).click();
		List<WebElement> subCategoryList = subCategory.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
		int j;
		for (j = 0; j < subCategoryList.size(); j++) {
			WebElement option = subCategoryList.get(j).findElement(By.xpath(".//span"));
			if (option.getText().equalsIgnoreCase(fetchField("subCategory"))) {
				option.click();
				break;
			}
		}
		if (j == subCategoryList.size()) {
			assertThat(false)
					.withFailMessage(
							"given subCategory " + fetchField("subCategory") + " not present in subCategory List")
					.isEqualTo(true);
		}

		gstRate.clear();
		gstRate.sendKeys(fetchField("gstRate"));

		cessAmount.clear();
		cessAmount.sendKeys(fetchField("cessAmount"));

		amountType.findElement(By.xpath(".//div/div/div[3]/input")).click();
		List<WebElement> subAmountType = amountType.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
		for (j = 0; j < subAmountType.size(); j++) {
			WebElement option = subAmountType.get(j).findElement(By.xpath(".//span"));
			if (option.getText().equalsIgnoreCase(fetchField("amountType"))) {
				option.click();
				break;
			}
		}
		if (j == subAmountType.size()) {
			assertThat(false)
					.withFailMessage("given amountType " + fetchField("amountType") + " not present in amountType List")
					.isEqualTo(true);
		}

		brandName.findElement(By.xpath(".//div/div/div[2]/input"));
		js.executeScript(
				"arguments[0].value='" + fetchField("brandName")
						+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
				brandName);

		this.numberOfVariations[skuIndex] = Integer.parseInt(fetchField("numberOfVariations"));
		Reporter.log("Number of Variations " + numberOfVariations[skuIndex], true);
		assertThat(this.numberOfVariations[skuIndex]).withFailMessage("Number of variations cannot be greater than 4")
				.isLessThanOrEqualTo(maxNumberOfVariations);
		for (int k = 0; k < this.numberOfVariations[skuIndex]; k++) {
			WebElement variationButton = driver.findElement(By.xpath("//*[@id=\"variationButton\"]"));
			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", variationButton);
			variationButton.click();

			variationName.get(k).clear();
			variationName.get(k).sendKeys(fetchField("variationName", k + 1));
			Reporter.log(variationName.get(k).getAttribute("value"), true);

			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", price.get(k));
			price.get(k).clear();
			price.get(k).sendKeys(fetchField("price", k + 1));
			Reporter.log(variationName.get(k).getAttribute("value"), true);
			// WebElement unit = driver.findElement(By.xpath("//*[@id=\"unitText\"]"));
			// unit.sendKeys("1");

			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", valueText.get(k));
			valueText.get(k).clear();
			valueText.get(k).sendKeys(fetchField("valueText", k + 1));
			Reporter.log(valueText.get(k).getAttribute("value"), true);
		}

////		FileOutputStream fos = new FileOutputStream("resources\\SKU.xlsx");
////		workbook.write(fos);
//		fos.close();
		closeFileInputStream();

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", saveButton);
		saveButton.click();
		aw.waitAllRequest();

		return fetchField("skuName");
	}

	// TODO update SKU's
	public String createSku(String... tuples) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		switch (tuples.length) {
		case Sku.dataLengthOn1Variation:
			Reporter.log("dataLength of size \"" + Sku.dataLengthOn1Variation + "\"", true);
			break;
		case Sku.dataLengthOn2Variation:
			Reporter.log("dataLength of size \"" + Sku.dataLengthOn2Variation + "\"", true);
			break;
		case Sku.dataLengthOn3Variation:
			Reporter.log("dataLength of size \"" + Sku.dataLengthOn3Variation + "\"", true);
			break;
		case Sku.dataLengthOn4Variation:
			Reporter.log("dataLength of size \"" + Sku.dataLengthOn4Variation + "\"", true);
			break;
		default:
			Reporter.log("dataLength of size \"" + tuples.length + "\" Which doesn't fit into standard", true);
			assertThat(false)
					.withFailMessage("dataLength of size \"" + tuples.length + "\" Which doesn't fit into standard")
					.isEqualTo(true);
		}

		int NoOfSku = Integer.parseInt(fetchField("NoOfSku"));
		for (int i = 0; i < NoOfSku; i++) {
			int skuIndex = i;

			addSKU.click();
			aw.waitAllRequest();

			itemHSNCode.clear();
			itemHSNCode.sendKeys(fetchField("itemHSNCode", 1, i));

			skuName.clear();
			skuName.sendKeys(fetchField("skuName", 1, i));

			localName.clear();
			localName.sendKeys(fetchField("localName", 1, i));

			description.clear();
			description.sendKeys(fetchField("description", 1, i));

			category.findElement(By.xpath(".//div/div/div[3]/input")).click();
			List<WebElement> categoryList = category.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
			for (int j = 0; j < categoryList.size(); j++) {
				WebElement option = categoryList.get(j).findElement(By.xpath(".//span"));
				if (option.getText().equalsIgnoreCase(fetchField("category", 1, i))) {
					option.click();
					break;
				}
				if (j == categoryList.size()) {
					assertThat(false)
							.withFailMessage(
									"given category " + fetchField("category", 1, i) + " not present in category List")
							.isEqualTo(true);
				}
			}
			aw.waitAllRequest();

			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", subCategory);
			subCategory.findElement(By.xpath(".//div/div/div[2]/input")).click();
			List<WebElement> subCategoryList = subCategory
					.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
			int j;
			for (j = 0; j < subCategoryList.size(); j++) {
				WebElement option = subCategoryList.get(j).findElement(By.xpath(".//span"));
				if (option.getText().equalsIgnoreCase(fetchField("subCategory", 1, i))) {
					option.click();
					break;
				}
			}
			if (j == subCategoryList.size()) {
				assertThat(false).withFailMessage(
						"given subCategory " + fetchField("subCategory", 1, i) + " not present in subCategory List")
						.isEqualTo(true);
			}

			gstRate.clear();
			gstRate.sendKeys(fetchField("gstRate", 1, i));

			cessAmount.clear();
			cessAmount.sendKeys(fetchField("cessAmount", 1, i));

			amountType.findElement(By.xpath(".//div/div/div[3]/input")).click();
			List<WebElement> subAmountType = amountType.findElements(By.xpath(".//ng-dropdown-panel/div/div[2]/div"));
			for (j = 0; j < subAmountType.size(); j++) {
				WebElement option = subAmountType.get(j).findElement(By.xpath(".//span"));
				if (option.getText().equalsIgnoreCase(fetchField("amountType", 1, i))) {
					option.click();
					break;
				}
			}
			if (j == subAmountType.size()) {
				assertThat(false).withFailMessage(
						"given amountType " + fetchField("amountType", 1, i) + " not present in amountType List")
						.isEqualTo(true);
			}

			brandName.findElement(By.xpath(".//div/div/div[2]/input"));
			js.executeScript("arguments[0].value='" + fetchField("brandName", 1, i)
					+ "';arguments[0].click();arguments[0].dispatchEvent(new Event('input', { bubbles: true }))",
					brandName);

			this.numberOfVariations[skuIndex] = Integer.parseInt(fetchField("numberOfVariations", 1, i));
			Reporter.log("Number of Variations " + numberOfVariations[skuIndex], true);
			assertThat(this.numberOfVariations[skuIndex])
					.withFailMessage("Number of variations cannot be greater than 4")
					.isLessThanOrEqualTo(maxNumberOfVariations);
			for (int k = 0; k < this.numberOfVariations[skuIndex]; k++) {
				WebElement variationButton = driver.findElement(By.xpath("//*[@id=\"variationButton\"]"));
				js.executeScript("arguments[0].scrollIntoViewIfNeeded();", variationButton);
				variationButton.click();

				variationName.get(k).clear();
				variationName.get(k).sendKeys(fetchField("variationName", k + 1, i));
				Reporter.log(variationName.get(k).getAttribute("value"), true);

				js.executeScript("arguments[0].scrollIntoViewIfNeeded();", price.get(k));
				price.get(k).clear();
				price.get(k).sendKeys(fetchField("price", k + 1, i));
				Reporter.log(variationName.get(k).getAttribute("value"), true);
				// WebElement unit = driver.findElement(By.xpath("//*[@id=\"unitText\"]"));
				// unit.sendKeys("1");

				js.executeScript("arguments[0].scrollIntoViewIfNeeded();", valueText.get(k));
				valueText.get(k).clear();
				valueText.get(k).sendKeys(fetchField("valueText", k + 1, i));
				Reporter.log(valueText.get(k).getAttribute("value"), true);
			}

////		FileOutputStream fos = new FileOutputStream("resources\\SKU.xlsx");
////		workbook.write(fos);
//		fos.close();
//		closeFileInputStream();

			js.executeScript("arguments[0].scrollIntoViewIfNeeded();", saveButton);
			saveButton.click();
			aw.waitAllRequest();
		}
		return null;
	}

	public void skuList(String skuName) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(skuName).withFailMessage("skuName is null").isNotBlank();
		searchSku(skuName);
		assertThat(skuListTuples.size()).withFailMessage("No Tuples in sku List for the search criteria " + skuName)
				.isGreaterThan(0);

	}

	public void deleteFirstSku(String skuName) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(skuName).withFailMessage("skuName is null").isNotBlank();
		searchSku(skuName.trim());
		assertThat(skuListTuples.size()).withFailMessage("No Tuples in sku List for the search criteria " + skuName)
				.isGreaterThan(0);

		for (int i = 0; i < skuListTuples.size(); i++) {
			if (skuListTuples.get(i).findElement(By.xpath(".//td[6]")).getText().equalsIgnoreCase(skuName.trim())) {
				skuListTuples.get(i).findElement(By.xpath("./td[11]/div/button[2]")).click();
				deleteConfirmation.click();
				break;
			}
			if (i == skuListTuples.size() - 1) {
				assertThat(false).withFailMessage("No Sku with name " + skuName + " in the searched tuples")
						.isEqualTo(true);
			}
		}
	}

	public int getSkuValue(String skuName, String skuVariation) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(skuName).withFailMessage("skuName is null").isNotBlank();
		searchSku(skuName.trim());
		assertThat(skuListTuples.size()).withFailMessage("No Tuples in sku List for the search criteria " + skuName)
				.isGreaterThan(0);

		String temp = "";
		for (int i = 0; i < skuListTuples.size(); i++) {
			if (skuListTuples.get(i).findElement(By.xpath(".//td[6]")).getText().equalsIgnoreCase(skuName.trim())) {
				skuListTuples.get(i).findElement(By.xpath("./td[11]/div/button[1]")).click();
				for (int j = 0; j < variationCards.size(); j++) {
					if (variationCards.get(j).findElement(By.xpath(".//div/div/div[2]/div[1]/div[1]/div/input"))
							.getAttribute("ng-reflect-name").trim().equalsIgnoreCase(skuVariation.trim())) {
						temp = variationCards.get(j).findElement(By.xpath(".//div/div/div[2]/div[3]/div[2]/div/input"))
								.getAttribute("ng-reflect-name").trim();
						break;
					}
					if (j == variationCards.size() - 1) {
						assertThat(false)
								.withFailMessage(
										"No Sku variation with name " + skuVariation + " in the searched tuples")
								.isEqualTo(true);
					}
				}
				break;
			}
			if (i == skuListTuples.size() - 1) {
				assertThat(false).withFailMessage("No Sku with name " + skuName + " in the searched tuples")
						.isEqualTo(true);
			}
		}

		return Integer.parseInt(temp);
	}
}
