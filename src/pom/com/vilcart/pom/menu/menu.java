package pom.com.vilcart.pom.menu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import util.com.vilcart.util.AngularWait;
import org.openqa.selenium.support.FindBy;

public class menu {
	private WebDriver driver;
	private AngularWait aw;
	
	@FindBy(xpath = "//*[@id=\"main-menu-content\"]/div[1]/input")
	private WebElement menuSearch;
}
