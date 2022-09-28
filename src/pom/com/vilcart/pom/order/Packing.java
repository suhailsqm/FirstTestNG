package pom.com.vilcart.pom.order;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Packing {

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;
	
	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;
	
	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;
	
	
}
