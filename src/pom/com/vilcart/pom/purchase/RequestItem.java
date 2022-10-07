/**
 * 
 */
package pom.com.vilcart.pom.purchase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author win10
 *
 */
public class RequestItem {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	private File file;
	private FileInputStream finput;
	private FileOutputStream fos;
	private DataFormatter formatter;
	private XSSFWorkbook workbook;
	private int writeRowKey;
	private Random randNumber;

	@FindBy(id = "searchInput")
	WebElement searchInput;

	@FindBy(id = "requestItemTuple")
	List<WebElement> requestItemTuples;

	@FindBy(xpath = "//input[@id=\"countNumber\"]")
	List<WebElement> countNumber;

	@FindBy(xpath = "//*[@id=\"addToCartButton\"]")
	List<WebElement> addToCartButton;

	@FindBy(xpath = "//button[text()='Continue']")
	WebElement continueButton;

	@FindBy(xpath = "//button[text()='Order All']")
	WebElement orderAllButton;

	public RequestItem(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		this.writeRowKey = 0;
		randNumber = new Random();
		PageFactory.initElements(this.driver, this);
		file = new File("resources\\purchaseDummy.xlsx");
		try {
			finput = new FileInputStream(file);
			fos = new FileOutputStream("resources\\purchaseDummy.xlsx");
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

	public void closeFileOutputStream() {
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void searchSku(String skuName) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(skuName);
		aw.waitAllRequest();
	}


	private void writeFeild(String feild, String feildVariation, int feildValue, int price) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		XSSFSheet sheet;
		sheet = workbook.getSheetAt(0);
		Row row = sheet.createRow(writeRowKey++);
		Cell cell = row.createCell(0);
		cell.setCellValue(feild);
		cell = row.createCell(1);
		cell.setCellValue(feildVariation);
		cell = row.createCell(2);
		cell.setCellValue(feildValue);
		cell = row.createCell(3);
		cell.setCellValue(feildValue);
		try {
			workbook.write(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		assertThat(false).withFailMessage("No specific '" + feild + "' in workbook resource.").isEqualTo(true);
	}

	public void processRequestItem(String skuName, int count) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchSku(skuName);

		assertThat(requestItemTuples.size()).withFailMessage("No Tuples with Sku Name " + skuName).isGreaterThan(0);

		int numberOfTuples = 0;
		String varFeild = "";
		String varFeildVariation = "";
		int varFeildValue = 0;
		if (requestItemTuples.size() < 2) {
			numberOfTuples = 1;
		} else {
			numberOfTuples = 2;
		}
		for (int i = 0; i < numberOfTuples; i++) {
			String xpathTemp = "//td/div/div[1]/div[1]";
			varFeild = requestItemTuples.get(i).findElement(By.xpath(xpathTemp)).getText().split("\n")[0];
			String xpath = "//td/div/div[1]/div[2]/div/button";
			WebElement list = requestItemTuples.get(i).findElement(By.xpath(xpath));
			Reporter.log("---" + list.getText(), true);
			list.click();
			String xpath1 = "//td/div/div[1]/div[2]/div/div/button";
			List<WebElement> listSelectDropDown = requestItemTuples.get(i).findElements(By.xpath(xpath1));
			int numOfVariations = listSelectDropDown.size();
			Reporter.log(xpath1 + "----" + numOfVariations, true);
			for (int i1 = 0; i1 < listSelectDropDown.size(); i1++) {
				WebElement varSelectDropDown = listSelectDropDown.get(i1);
				Reporter.log("----" + varSelectDropDown.getText(), true);
				varSelectDropDown.click();

				String xpathTemp1 = "//td/div/div[3]/span";
				varFeildVariation = requestItemTuples.get(i).findElement(By.xpath(xpathTemp1)).getText();

				WebElement varInputCount = countNumber.get(i);
				varInputCount.clear();
				varInputCount.sendKeys("" + count);
				varFeildValue = count;

				WebElement varAddToCart = addToCartButton.get(i);
				varAddToCart.click();
				writeFeild(varFeild, varFeildVariation, varFeildValue, randNumber.nextInt(90) + 10);

				if (i1 != numOfVariations - 1) {
					requestItemTuples.get(i).findElement(By.xpath(xpath)).click();
				}

			}
		}

		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", continueButton);
		aw.waitAllRequest();
		continueButton.click();
		aw.waitAllRequest();
		orderAllButton.click();
		aw.waitAllRequest();

		closeFileOutputStream();
		closeFileInputStream();
	}

}
