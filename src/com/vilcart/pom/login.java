package com.vilcart.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

public class login {
WebDriver driver;
	
	@CacheLookup
	@FindBy(css = "input[ng-reflect-name=email]")
	WebElement email;
	
	@CacheLookup
	@FindBy(css = "input[ng-reflect-name=password]")
	WebElement pass;
	
	@CacheLookup
	@FindBy(tagName = "button")
	WebElement login;
	
	public login(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	public void loginWithUserCrendentials(String username, String password){

		Reporter.log("username = "+username);
		Reporter.log("password = " + password);
		
		email.clear();
		email.sendKeys(username);
		
		pass.clear();
		pass.sendKeys(password);
		
		login.click();
	}
	
	

}
