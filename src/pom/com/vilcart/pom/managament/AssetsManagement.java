/**
 * 
 */
package pom.com.vilcart.pom.managament;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import pom.com.vilcart.pom.goods.GoodsReceiveCreation;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

/**
 * @author win10
 *
 */
public class AssetsManagement {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;
	CreateAssetsManagement csm;

	@FindBy(xpath = "//*[@id=\"demo-2\"]/input")
	private WebElement searchInput;

	@FindBy(id = "addAssets")
	private WebElement addAssets;
	
	@FindBy(id = "assetTuple")
	private List<WebElement> assetTuples;

	public AssetsManagement(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
		csm = new CreateAssetsManagement(driver,aw);
	}

	public void searchInAssetsMgmt(String key) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchInput.clear();
		searchInput.sendKeys(key);
		searchInput.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
	}
	
	public void createAndValidateAssetMgmt() {
		addAssets.click();
		csm.createAssetMgmt();
		//TODO Validate the assets.
	}
}
