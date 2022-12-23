/**
 * 
 */
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

/**
 * @author win10
 *
 */
public class GoodsTransfer {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	public GoodsTransferCreation gtc;
	private Inventory inventory;
	private Menu menu;
	public String maps[][];

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement searchInput;

	@FindBy(id = "createGoodsTransfer")
	private WebElement createGoodsTransfer;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(id = "transferListTuple")
	private List<WebElement> transferListTuples;

	@FindBy(id = "orderListTuple")
	private List<WebElement> orderListTuples;

	public GoodsTransfer(WebDriver driver, AngularWait aw, String skufile, Inventory inventory, Menu menu) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		this.inventory = inventory;
		this.menu = menu;
		PageFactory.initElements(driver, this);
		gtc = new GoodsTransferCreation(driver, aw, skufile, inventory);
	}

	public void searchInGoodsTransfer(String key) {
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

	public void verifyTransferGoods() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		getTuplesForCurrentDate();
		searchInGoodsTransfer(gtc.challanNo);
		assertThat(transferListTuples.size()).withFailMessage("No tuple with challan no " + gtc.challanNo)
				.isGreaterThan(0);
		for (int i = 0; i < transferListTuples.size(); i++) {
			if (transferListTuples.get(i).findElement(By.xpath(".//td[5]")).getText().equalsIgnoreCase(gtc.challanNo)) {

				assertThat(transferListTuples.get(i).findElement(By.xpath(".//td[4]")).getText()).withFailMessage(
						"Vehicle No mismatch " + transferListTuples.get(i).findElement(By.xpath(".//td[4]")).getText()
								+ " " + gtc.vehicleArg)
						.isEqualTo(gtc.vehicleArg);
				assertThat(transferListTuples.get(i).findElement(By.xpath(".//td[2]")).getText())
						.withFailMessage("DC mismatch "
								+ transferListTuples.get(i).findElement(By.xpath(".//td[2]")).getText() + " " + gtc.DC)
						.isEqualTo(gtc.DC);
				transferListTuples.get(i).findElement(By.xpath(".//td[11]/div/button[1]")).click();
				aw.waitAllRequest();
				// ToDo check if all order Items are present in the Goods Transfer.
				for (int j = 0; j < gtc.itemsCount; j++) {
					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[2]")).getText())
							.withFailMessage("Doesn't Tally SKUName " + gtc.data[j][1])
							.isEqualToIgnoringCase(gtc.data[j][1]);
					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[5]")).getText())
							.withFailMessage("Doesn't Tally SKU Vaiation Name " + gtc.data[j][2])
							.isEqualToIgnoringCase(gtc.data[j][2]);
					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[4]")).getText())
							.withFailMessage("Doesn't Tally count " + gtc.data[j][3])
							.isEqualToIgnoringCase(gtc.data[j][3]);
					// TODO tally unit price/ GRN Price and Total price.
//					assertThat(orderListTuples.get(j).findElement(By.xpath(".//td[6]")).getText())
//							.withFailMessage("Doesn't Tally receive price " + gtc.data[j][3])
//							.isEqualToIgnoringCase(gtc.data[j][3]);
//					assertThat(Integer.parseInt(
//							orderListTuples.get(j).findElement(By.xpath(".//td[7]")).getText()))
//							.withFailMessage("Doesn't Tally total amount " + gtc.data[j][3])
//							.isEqualTo(Integer.parseInt(gtc.data[j][2])
//									* Integer.parseInt(gtc.data[j][3]));

				}
				break;
			}
			if (i == transferListTuples.size() - 1) {
				assertThat(false).withFailMessage("No tuple with challan no " + gtc.challanNo).isEqualTo(true);
			}
		}
	}

	public void transferGoods(String dc, String vehicle) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		createGoodsTransfer.click();
		gtc.createGoodsTransfer(dc, vehicle);
	}

	public String[][] verifyInventoryAfterGoodsTransfer(String[][][] skuVarValueData) throws InterruptedException {
		maps = new String[skuVarValueData.length][3];

		// filling the maps with null
		for (int i = 0; i < skuVarValueData.length; i++) {
			maps[i][0] = null;
			maps[i][1] = null;
			maps[i][2] = null;
		}

		// filling the maps with unique SKU data.
		for (int i = 0; i < gtc.itemsCount; i++) {
			int j = 0;
			int k = 0;
			for (; j < skuVarValueData.length; j++) {
				if (maps[j][0] == null && maps[j][1] == null) {
					maps[k][0] = gtc.data[i][1];
					maps[k][1] = "0";
					menu.goToInventory();
					maps[k][2] = Double.toString(inventory.getInventoryGoodsOutCount(gtc.data[i][1]));
					k++;
					break;
				} else if (!(maps[j][0] == null) && !(maps[j][1] == null) && !(maps[j][2] == null)
						&& !maps[j][0].equals(gtc.data[i][1])) {
					k++;
					continue;
				} else if (!(maps[j][0] == null) && !(maps[j][1] == null) && !(maps[j][2] == null)
						&& maps[j][0].equals(gtc.data[i][1])) {
					break;
				}
			}
		}

		// Map to check if all items are iterated over for filling the SKU count.
		boolean itemsMap[] = new boolean[gtc.itemsCount];
		for (int i = 0; i < gtc.itemsCount; i++)
			itemsMap[i] = false;

		for (int i = 0; i < gtc.data.length; i++) {
			Reporter.log(gtc.data[i][0] + " ", true);
			Reporter.log(gtc.data[i][1] + " ", true);
			Reporter.log(gtc.data[i][2] + " ", true);
			Reporter.log(gtc.data[i][3] + " ", true);
		}

		for (int i = 0; i < gtc.data.length; i++) {
			String skuName = gtc.data[i][1];
			String variationName = gtc.data[i][2];
			int count = Integer.parseInt(gtc.data[i][3]);

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
