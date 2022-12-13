package pom.com.vilcart.pom.goods;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import pom.com.vilcart.pom.inventory.Inventory;
import pom.com.vilcart.pom.menu.Menu;
import pom.com.vilcart.pom.sku.Sku;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

public class GoodsReceive {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	public GoodsReceiveCreation grc;
	private Inventory inventory;
	private Menu menu;
	public String maps[][];

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement searchInput;

	@FindBy(id = "createGoodsReceive")
	private WebElement createGoodsReceive;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(id = "receiveListTuple")
	private List<WebElement> receiveListTuples;

	@FindBy(id = "orderListTuple")
	private List<WebElement> orderListTuples;

	public GoodsReceive(WebDriver driver, AngularWait aw, String skufile, Inventory inventory, Menu menu) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		this.inventory = inventory;
		this.menu = menu;
		grc = new GoodsReceiveCreation(driver, aw, skufile, inventory);
	}

	public void searchInGoodsreceive(String key) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(key);
		searchInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}

	private void getTuplesForCurrentDate() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				startDate);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				endDate);
		submit.click();
		aw.waitAllRequest();
	}

	private String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}

	public void verifyReceiveGoods() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
//		createGoodsReceive.click();
//		grc.createGoodsReceive(dc, vehicle, challanNoArg);
//		menu.goToGoodsReceive();
		getTuplesForCurrentDate();
		searchInGoodsreceive(grc.challanNo);
		// ToDo verifying with vehicle number should be a challan no which has to pop up
		// in
		// create Goods receive.
		for (int i = 0; i < receiveListTuples.size(); i++) {
			if (receiveListTuples.get(i).findElement(By.xpath(".//td[4]")).getText().equalsIgnoreCase(grc.challanNo)) {
				assertThat(receiveListTuples.get(i).findElement(By.xpath(".//td[4]")).getText()).withFailMessage(
						"Vehicle No mismatch " + receiveListTuples.get(i).findElement(By.xpath(".//td[4]")).getText()
								+ " " + grc.vehicleArg)
						.isEqualTo(grc.vehicleArg);
				assertThat(receiveListTuples.get(i).findElement(By.xpath(".//td[2]")).getText())
						.withFailMessage("DC mismatch "
								+ receiveListTuples.get(i).findElement(By.xpath(".//td[2]")).getText() + " " + grc.DC)
						.isEqualTo(grc.DC);

				receiveListTuples.get(i).findElement(By.xpath(".//td[11]/div/button[1]")).click();
				aw.waitAllRequest();
				// ToDo check if all order Items are present in the Goods receive.
				for (int j = 0; j < grc.itemsCount; j++) {
					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[2]")).getText())
							.withFailMessage("Doesn't Tally SKUName " + grc.data[j][1])
							.isEqualToIgnoringCase(grc.data[j][1]);
					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[5]")).getText())
							.withFailMessage("Doesn't Tally SKU Vaiation Name " + grc.data[j][2])
							.isEqualToIgnoringCase(grc.data[j][2]);
					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[4]")).getText())
							.withFailMessage("Doesn't Tally count " + grc.data[j][3])
							.isEqualToIgnoringCase(grc.data[j][2]);
					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[6]")).getText())
							.withFailMessage("Doesn't Tally receive price " + grc.data[j][4])
							.isEqualToIgnoringCase(grc.data[j][4]);
					assertThat(Integer.parseInt(orderListTuples.get(j).findElement(By.xpath(".//td[7]")).getText()))
							.withFailMessage("Doesn't Tally total amount "
									+ Integer.parseInt(grc.data[j][3]) * Integer.parseInt(grc.data[j][4]))
							.isEqualTo(Integer.parseInt(grc.data[j][3]) * Integer.parseInt(grc.data[j][4]));

				}
				break;
			}
			if (i == receiveListTuples.size() - 1) {
				assertThat(false).withFailMessage("No tuple with challan no " + grc.challanNo).isEqualTo(true);
			}
		}

	}

	public void receiveGoods(String dc, String vehicle, String challanNo) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		createGoodsReceive.click();
		grc.createGoodsReceive(dc, vehicle, challanNo);
	}

	public String[][] verifyInventoryAfterGoodsReceive(String[][][] skuVarValueData) throws InterruptedException {
		maps = new String[skuVarValueData.length][3];

		// filling the maps with null
		for (int i = 0; i < skuVarValueData.length; i++) {
			maps[i][0] = null;
			maps[i][1] = null;
			maps[i][2] = null;
		}

		// filling the maps with unique SKU data.
		for (int i = 0; i < grc.itemsCount; i++) {
			int j = 0;
			int k = 0;
			for (; j < skuVarValueData.length; j++) {
				if (maps[j][0] == null && maps[j][1] == null) {
					maps[k][0] = grc.data[i][1];
					maps[k][1] = "0";
					menu.goToInventory();
					maps[k][2] = Double.toString(inventory.getInventoryGoodsOutCount(grc.data[i][1]));
					k++;
					break;
				} else if (!(maps[j][0] == null) && !(maps[j][1] == null) && !(maps[j][2] == null)
						&& !maps[j][0].equals(grc.data[i][1])) {
					k++;
					continue;
				} else if (!(maps[j][0] == null) && !(maps[j][1] == null) && !(maps[j][2] == null)
						&& maps[j][0].equals(grc.data[i][1])) {
					break;
				}
			}
		}

		// Map to check if all items are iterated over for filling the SKU count.
		boolean itemsMap[] = new boolean[grc.itemsCount];
		for (int i = 0; i < grc.itemsCount; i++)
			itemsMap[i] = false;

		for (int i = 0; i < grc.data.length; i++) {
			Reporter.log(grc.data[i][0] + " ", true);
			Reporter.log(grc.data[i][1] + " ", true);
			Reporter.log(grc.data[i][2] + " ", true);
			Reporter.log(grc.data[i][3] + " ", true);
			Reporter.log(grc.data[i][4] + " ", true);
		}

		for (int i = 0; i < grc.data.length; i++) {
			String skuName = grc.data[i][1];
			String variationName = grc.data[i][2];
			int count = Integer.parseInt(grc.data[i][3]);

			for (int j = 0; j < skuVarValueData.length && itemsMap[i] == false; j++) {
				for (int k = 0; k < Sku.maxNumberOfVariations; k++) {
					if (skuVarValueData[j][k][0] != null && skuVarValueData[j][k][1] != null
							&& skuVarValueData[j][k][2] != null) {
						if (skuVarValueData[j][k][0].equals(skuName)
								&& skuVarValueData[j][k][1].equals(variationName)) {
							for (int l = 0; l < maps.length; l++) {
								double temp = count / Double.parseDouble(skuVarValueData[j][k][2]);
								if (maps[l][0].equals(skuVarValueData[j][k][0])) {
									double temp1 = Double.parseDouble(maps[l][1]);
									maps[l][1] = Double.toString(temp1 + temp);
								}
							}
						} else {
							continue;
						}
					}
				}
			}
			itemsMap[i] = true;
		}
		return maps;
	}

}
