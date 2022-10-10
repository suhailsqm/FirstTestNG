package pom.com.vilcart.pom.placeorder;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

public class PlaceOrder {
	private WebDriver driver;
	private AngularWait aw;
	
	@FindBy(xpath = "//*[@id=\"iconLeft1\"]")
	private WebElement searchCustomer;
	
	@FindBy(xpath = "//*[@id=\"currCustomer\"]/h2")
	private WebElement currCustomer;
	
	@FindBy(xpath = "//*[@id=\"itemName\"]")
	private WebElement skuInput;
	
	@FindBy(id = "liList")
	private List<WebElement> liList;
	
	@FindBy(id = "addToCartButton")
	private List<WebElement> addToCartButton;
	
	@FindBy(id = "itemNameList")
	List<WebElement> itemNameList;
	
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
	}
	
	public void searchCustomer(String customerName) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchCustomer.clear();
		searchCustomer.sendKeys(customerName);
		aw.waitAllRequest();
	}
	
	public void clickFirstCustomer() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		currCustomer.click();
		aw.waitAllRequest();
	}
	
	public void searchItem(String skuName) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		skuInput.clear();
		skuInput.sendKeys(skuName);
		aw.waitAllRequest();
	}
	
	public void placeOrderFirstTwoTuple() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		for (int i1 = 0; i1 < liList.size() && i1 < 2; i1++) {
			List<WebElement> temp = liList.get(i1).findElements(By.xpath("//following-sibling::span"));
				
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
}
