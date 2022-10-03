/**
 * 
 */
package pom.com.vilcart.pom.purchase;

import java.util.List;

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
		PageFactory.initElements(this.driver, this);
	}

	public void searchSku(String skuName) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(skuName);
		aw.waitAllRequest();
	}

	public void processRequestItem(String skuName, int count) {
		Reporter.log("===>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchSku(skuName);
		
		assertThat(requestItemTuples.size()).withFailMessage("No Tuples with Sku Name " + skuName).isGreaterThan(0);
		
		int numberOfTuples = 0;
		if (requestItemTuples.size() < 2) {
			numberOfTuples = 1;
		} else {
			numberOfTuples = 2;
		}
		for (int i = 0; i < numberOfTuples; i++) {
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

				WebElement varInputCount = countNumber.get(i);
				varInputCount.clear();
				varInputCount.sendKeys("" + count);

				WebElement varAddToCart = addToCartButton.get(i);
				varAddToCart.click();

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
	}

}
