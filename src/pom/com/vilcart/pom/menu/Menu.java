package pom.com.vilcart.pom.menu;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.TimeStamp;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

public class Menu {
	private WebDriver driver;
	private AngularWait aw;

	@FindBy(xpath = "//*[@id=\"main-menu-content\"]/div[1]/input")
	private WebElement menuSearch;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a")
	private WebElement customerMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	private WebElement newCustomer;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	private WebElement customerList;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a/span")
	private WebElement placeOrder;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a")
	private WebElement ordersMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	private WebElement packing;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	private WebElement invoice;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[3]/a")
	private WebElement dispatch;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[4]/a")
	private WebElement delivery;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[5]/a")
	private WebElement complete;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a/span")
	private WebElement skuMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a")
	private WebElement purchaseMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[6]/a")
	private WebElement autoPO;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[5]/a")
	private WebElement requestItem;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[4]/a")
	private WebElement poRequest;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[3]/a")
	private WebElement poApproved;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	private WebElement purchaseReturnList;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	private WebElement purchaseList;
	
	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a/span")
	private WebElement inventory;

	public Menu(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		PageFactory.initElements(this.driver, this);
	}

	public void goToNewCustomer() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Customer");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		newCustomer.click();
		aw.waitAllRequest();
		menuSearch.click();
		aw.waitAllRequest();
	}

	public void goToCustomerList() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Customer");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		customerList.click();
		aw.waitAllRequest();
	}

	public void goToPlaceOrder() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Place Order");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		placeOrder.click();
		aw.waitAllRequest();
	}

	public void goToPacking() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Orders");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
		packing.click();
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
	}

	public void goToInvoice() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Orders");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
		invoice.click();
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
	}

	public void goToDispatch() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Orders");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
		dispatch.click();
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
	}

	public void goToDelivery() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Orders");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
		delivery.click();
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
	}

	public void goToComplete() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Orders");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
		complete.click();
		aw.waitAllRequest();
		ordersMenu.click();
		aw.waitAllRequest();
	}

	public void goToSKU() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("SKU");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		skuMenu.click();
		aw.waitAllRequest();
	}

	public void goToAutoPo() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Purchase");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
		autoPO.click();
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
	}

	public void goToRequestItem() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Purchase");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
		requestItem.click();
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
	}

	public void goToPoRequest() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Purchase");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
		poRequest.click();
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
	}

	public void goToPoApproved() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Purchase");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
		poApproved.click();
		aw.waitAllRequest();
		purchaseMenu.click();
	}

	public void goToPurchaseReturnList() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Purchase");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
		purchaseReturnList.click();
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
	}

	public void goToPurchaseList() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Purchase");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
		purchaseList.click();
		aw.waitAllRequest();
		purchaseMenu.click();
		aw.waitAllRequest();
	}
	
	public void goToInventory() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Inventory");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		inventory.click();
		aw.waitAllRequest();
	}
}
