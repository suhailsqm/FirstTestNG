package app.com.vilcart.app.customer;

import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import pom.com.vilcart.pom.customer.CustomerList;
import pom.com.vilcart.pom.customer.NewCustomer;
import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.Login;

import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class Customer_001 {
	private WebDriver driver;
	private AngularWait aw;
	private Login loginObj;
	private NewCustomer nc;
	private CustomerList cl;
	private String phoneNumber;

	@Test(priority = 1)
	public void createCustomer() {
		phoneNumber = nc.createCustomer();
	}

	@Test(priority = 2)
	public void verifyCustomerList() {
		cl.verifyCustomer(phoneNumber);
	}

	@Test(priority = 3)
	public void deleteCustomer() {
		cl.deleteCustomer(phoneNumber);
	}

	@BeforeClass
	public void beforeClass() throws IOException {
		aw = new AngularWait(driver);
		loginObj = new Login(driver, aw);
		nc = new NewCustomer(driver, aw);
		cl = new CustomerList(driver, aw);
		loginObj = new Login(driver, aw);
		loginObj.login();
		
	}

	@AfterClass
	public void afterClass() {
	}

	@BeforeSuite
	public void beforeSuite() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("http://localhost:4200");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	}

	@AfterSuite
	public void afterSuite() {
		driver.quit();
	}

}
