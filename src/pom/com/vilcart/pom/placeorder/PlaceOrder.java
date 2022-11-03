package pom.com.vilcart.pom.placeorder;

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

public class PlaceOrder {
	private WebDriver driver;
	private AngularWait aw;
	private String customerName;
	private String phoneNumber;

	private File file;
	private FileInputStream finput;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;
	private String[][] data;

	@FindBy(xpath = "//*[@id=\"iconLeft1\"]")
	private WebElement searchCustomer;

//	@FindBy(xpath = "//*[@id=\"currCustomer\"]/h2")
	@FindBy(xpath = "//*[@id=\"currCustomer\"]")
	private List<WebElement> currCustomer;

	@FindBy(xpath = "//*[@id=\"itemName\"]")
	private WebElement skuInput;

	@FindBy(id = "liList")
	private List<WebElement> liList;

	@FindBy(id = "varPrice")
	private List<WebElement> varPrice;

	@FindBy(id = "cartList")
	private List<WebElement> cartList;

	@FindBy(id = "quantity")
	private List<WebElement> quantity;

	@FindBy(id = "cumulativeVarPrice")
	private List<WebElement> cumulativeVarPrice;

	@FindBy(id = "addToCartList")
	private List<WebElement> addToCartButton;

	@FindBy(id = "itemNameList")
	List<WebElement> itemNameList;

	@FindBy(id = "cartTotal")
	WebElement cartTotal;

	@FindBy(id = "placeOrderButton")
	WebElement placeOrderButton;

	@FindBy(id = "swal2-input")
	WebElement remarksInput;

	@FindBy(className = "swal2-confirm")
	WebElement buyAllButton;

	public PlaceOrder(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		PageFactory.initElements(this.driver, this);
		try {
			file = new File(ReadPropertiesFile.readPropertiesFile().getProperty("resources.PlaceOrder"));
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

	public void searchCustomer(String customerName) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchCustomer.clear();
		searchCustomer.sendKeys(customerName);
		aw.waitAllRequest();
	}

	private int fetchData() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		XSSFCell cella;
		XSSFCell cellb;
		XSSFCell cell1;
		XSSFCell cell2;
		XSSFCell cell3;
		XSSFCell cell4;
		int rowKey = 0;
		sheet = workbook.getSheetAt(0);
		data = new String[sheet.getLastRowNum() + 1][4];
		cella = sheet.getRow(0).getCell(1);
		customerName = formatter.formatCellValue(cella);
		cellb = sheet.getRow(1).getCell(1);
		phoneNumber = formatter.formatCellValue(cellb);
		for (int i = 3; i <= sheet.getLastRowNum(); i++) {
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
		closeFileInputStream();
		return sheet.getLastRowNum() - 3 + 1;
	}

	public void clickFirstCustomer() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		currCustomer.get(0).click();
		;
		aw.waitAllRequest();
	}

	public void searchItem(String skuName) {
		Reporter.log("====>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		skuInput.clear();
		skuInput.sendKeys(skuName);
		aw.waitAllRequest();
	}

	public void placeOrderFirstTwoTuple() {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		for (int i1 = 0; i1 < liList.size() && i1 < 2; i1++) {
			List<WebElement> temp = liList.get(i1).findElements(By.xpath(".//following-sibling::span"));

			for (int i2 = 0; i2 < temp.size(); i2++) {
				temp.get(i2).getText();
				Reporter.log(temp.get(i2).getText(), true);
				temp.get(i2).click();
				addToCartButton.get(i1).click();
			}
			Reporter.log("" + itemNameList.get(i1).getText(), true);
		}
		placeOrderButton.click();
		remarksInput.sendKeys("placing order");
		// WebElement buyAllButton =
		// driver.findElement(By.xpath("/html/body/div/div/div[6]/button[3]"));
		buyAllButton.click();
	}

	public void placeOrderFromFile(String phoneNumber) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		int items = fetchData();
		if (items == 0)
			throw new SkipException("Skipping this exception No data in resources\\PlaceOrder.xlsx");

		searchCustomer(phoneNumber);
		clickFirstCustomer();
		int totalPrice = 0;
		for (int i = 0; i < items; i++) {
			data[i][1] = data[i][1].trim();
			data[i][2] = data[i][2].trim();
			data[i][3] = data[i][3].trim();
			searchItem(data[i][1]);
			if (liList.size() == 0) {
				assertThat(false).withFailMessage("No tuple with search Criteria " + data[i][1]).isEqualTo(true);
			}
			String skuName = liList.get(0).findElement(By.xpath(".//h2")).getText();
			assertThat(data[i][1]).withFailMessage("No tuple with search Criteria " + data[i][1])
					.isEqualToIgnoringCase(skuName);
			Reporter.log(skuName, true);
			List<WebElement> temp = liList.get(0).findElements(By.xpath(".//following-sibling::span"));
			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).getText().trim().equalsIgnoreCase(data[i][2])) {
//					Reporter.log("\""+temp.get(j).getText().trim()+"\" \""+data[i][2]+"\"",true);
					assertThat(data[i][2]).withFailMessage("No variation with search Criteria " + data[i][2])
							.isEqualToIgnoringCase(temp.get(j).getText().trim());
					Reporter.log(temp.get(j).getText(), true);
					temp.get(j).click();
					aw.waitAllRequest();
					int variationPrice = Integer.parseInt(varPrice.get(0).getText().trim());
					addToCartButton.get(0).click();
					aw.waitAllRequest();
					int count = Integer.parseInt(data[i][3]);
					quantity.get(0).clear();
					quantity.get(0).sendKeys(data[i][3]);
					aw.waitAllRequest();
//					Reporter.log("\""+variationPrice * count+"\" \""+ cumulativeVarPrice.get(0).getText().trim()+"\"", true);
					assertThat(variationPrice * count).withFailMessage("Cumulative variation Price Doesn't tally")
							.isEqualTo(Integer.parseInt(cumulativeVarPrice.get(0).getText().trim()));
					totalPrice += variationPrice * count;
					break;
				}
				if (j == temp.size() - 1) {
					assertThat(data[i][1])
							.withFailMessage("No variation with search Criteria " + data[i][1] + " " + data[i][2])
							.isEqualToIgnoringCase(temp.get(j).getText().trim());
				}
			}
		}
		assertThat(totalPrice).withFailMessage("Total Price Doesn't Tally")
				.isEqualTo(Integer.parseInt(cartTotal.getText().trim()));
		placeOrderButton.click();
		remarksInput.sendKeys("placing order");
		buyAllButton.click();
	}
}
