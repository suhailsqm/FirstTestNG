package app.com.vilcart.app.customer;

import pom.com.vilcart.pom.customer.CustomerList;
import pom.com.vilcart.pom.customer.NewCustomer;
import pom.com.vilcart.pom.menu.Menu;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.BaseSuiteMethods;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.Login;
import util.com.vilcart.util.ReadPropertiesFile;
import util.com.vilcart.util.TimeStamp;

import org.testng.annotations.Test;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.time.Duration;

public class Customer_001 extends BaseSuiteMethods {
	private AngularWait aw;
	private Login loginObj;
	private NewCustomer newCustomer;
	private CustomerList customerList;
	private Menu menu;
	private String phoneNumber;

	@Test(priority = 1)
	public void createCustomerFlow() throws InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToNewCustomer();
		phoneNumber = newCustomer.createCustomer();
	}

	@Test(priority = 2, dependsOnMethods = { "createCustomer" })
	public void verifyCustomerListFlow() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToCustomerList();
		customerList.verifyCustomer(phoneNumber);
	}

	@Test(priority = 3, dependsOnMethods = { "verifyCustomerList" })
	public void deleteCustomerFlow() {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		menu.goToCustomerList();
		customerList.deleteCustomer(phoneNumber);
	}

	@BeforeClass
	public void beforeClass() throws IOException {
		driver.get(ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.deployed.url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		newCustomer = new NewCustomer(driver, aw);
		customerList = new CustomerList(driver, aw);
		loginObj = new Login(driver, aw);
		menu = new Menu(driver, aw);
		loginObj.login();

	}

	@AfterClass
	public void afterClass() {
	}

}
