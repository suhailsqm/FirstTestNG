package pom.com.vilcart.pom.order;

import static org.assertj.core.api.Assertions.assertThat;

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

public class Order {
	private WebDriver driver;
	private JavascriptExecutor js;
	private AngularWait aw;
	
	@FindBy(xpath = "//*[@id=\"orderNumber\"]")
	private WebElement orderNumberElement;

	@FindBy(xpath = "//*[@id=\"orderTuple\"]")
	private List<WebElement> listTuples;

	@FindBy(xpath = "//*[@id=\"originalQuantity\"]")
	private List<WebElement> originalQuantity;
	
	@FindBy(xpath = "//*[@id=\"totalQuantity\"]")
	private WebElement totalQuantity;
	
	@FindBy(xpath = "//*[@id=\"OriginalPrice\"]")
	private List<WebElement> OriginalPrice;
	
	@FindBy(xpath = "//*[@id=\"totalPrice\"]")
	private WebElement totalPrice;
	
	@FindBy(xpath = "//*[@id=\"moveToNext\"]")
	private WebElement moveToInvoice;

	public Order(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
	}
	
	public String getOrderNumber() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		String orderNumber = orderNumberElement.getText().trim().replaceAll("^.|.$", "");
		Reporter.log("orderNumber: " + orderNumber, true);

		if (orderNumber.isEmpty()) {
			assertThat(orderNumber).withFailMessage("Order Number is empty").isNotEmpty();
			return null;
		}
		return orderNumber;
	}
	
	public void evaluateOrder() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		assertThat(listTuples.size()).withFailMessage("No Tuples in Packing").isGreaterThan(0);
		for (int i = 0; i < listTuples.size(); i++) {
			WebElement tuple = listTuples.get(i);
			WebElement orderRowBtn = tuple.findElement(By.xpath("//td[12]/div/ui-switch/button"));
			String value = orderRowBtn.getAttribute("aria-checked");
			if (value.contains("false")) {
				WebElement test = tuple.findElement(By.xpath("//td[12]/div/ui-switch"));
				test.click();
			}
		}

		int quantity = 0;
		for (int i = 0; i < originalQuantity.size(); i++) {
			WebElement temp = originalQuantity.get(i);
			quantity += Integer.parseInt(temp.getText());
		}
		assertThat(totalQuantity.getText().split(":")[1].trim())
				.withFailMessage("Total Quantity doesn't tally").isEqualTo("" + quantity);
		
		
		int price = 0;
		for (int i = 0; i < OriginalPrice.size(); i++) {
			WebElement temp4 = OriginalPrice.get(i);
			price += Integer.parseInt(temp4.getText());
		}
		assertThat(totalPrice.getText().split("\\u20B9")[1].trim())
				.withFailMessage("Total Price not tallying.").isEqualTo("" + price);
		
		js.executeScript("arguments[0].scrollIntoViewIfNeeded();", moveToInvoice);
		moveToInvoice.click();
		aw.waitAllRequest();
	}
}
