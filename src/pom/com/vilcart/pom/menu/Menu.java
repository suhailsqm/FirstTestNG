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
	public WebElement menuSearch;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a")
	public WebElement customerMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	public WebElement newCustomer;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	public WebElement customerList;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a/span")
	public WebElement placeOrder;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a")
	public WebElement ordersMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	public WebElement packing;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	public WebElement invoice;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[3]/a")
	public WebElement dispatch;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[4]/a")
	public WebElement delivery;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[5]/a")
	public WebElement complete;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a/span")
	public WebElement skuMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a")
	public WebElement purchaseMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[6]/a")
	public WebElement autoPO;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[5]/a")
	public WebElement requestItem;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[4]/a")
	public WebElement poRequest;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[3]/a")
	public WebElement poApproved;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	public WebElement purchaseReturnList;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	public WebElement purchaseList;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a/span")
	public WebElement inventory;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a/span")
	public WebElement managementMenu;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	public WebElement assetsManagement;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	public WebElement routeManagement;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[3]/a")
	public WebElement masterManagement;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[4]/a")
	public WebElement categoryManagement;

	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/a")
	public WebElement goodsMenu;
	
	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[2]/a")
	public WebElement goodsReceive;
	
	@FindBy(xpath = "//*[@id=\"main-menu-navigation\"]/li/ul/li[1]/a")
	public WebElement goodsTransfer;
	
	
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
		customerMenu.click();
		aw.waitAllRequest();
		newCustomer.click();
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
//			Reporter.log("Packing is clicked", true);
		aw.waitAllRequest();
//		ordersMenu.click();
//		aw.waitAllRequest();
//			Reporter.log("go To Packing has ended", true);
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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

	public void goToAssetsManagement() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Management");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
		assetsManagement.click();
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
	}

	public void goToRouteManagement() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Management");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
		routeManagement.click();
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
	}

	public void goToMasterManagement() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Management");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
		masterManagement.click();
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
	}

	public void goToCategoryManagement() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Management");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
		categoryManagement.click();
		aw.waitAllRequest();
		managementMenu.click();
		aw.waitAllRequest();
	}

	public void goToGoodsReceive() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Goods");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		goodsMenu.click();
		aw.waitAllRequest();
		goodsReceive.click();
		aw.waitAllRequest();
		
	}

	public void goToGoodsTransfer() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menuSearch.clear();
		menuSearch.sendKeys("Goods");
		menuSearch.sendKeys(Keys.ENTER);
		aw.waitAllRequest();
		goodsMenu.click();
		aw.waitAllRequest();
		goodsTransfer.click();
		aw.waitAllRequest();
	}
}
