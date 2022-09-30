/**
 * 
 */
package pom.com.vilcart.pom.customer;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import util.com.vilcart.util.AngularWait;
import util.com.vilcart.util.CurrentMethod;
import util.com.vilcart.util.LineNumber;
import util.com.vilcart.util.TimeStamp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author win10
 *
 */
public class CustomerList {
	private WebDriver driver;
	private AngularWait aw;

	@FindBy(id = "searchBtn")
	private WebElement searchBtn;

	@FindBy(xpath = "//*[@id=\"customerTuple\"]")
	private List<WebElement> customerTuples;

	@FindBy(xpath = "(//button[normalize-space()='Delete'])[1]")
	private WebElement delete;

	@FindBy(xpath = "(//button[normalize-space()='OK'])[1]")
	private WebElement ok;

	public CustomerList(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		PageFactory.initElements(this.driver, this);
	}

	public void searchCustomer(String phoneNumber) {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		searchBtn.clear();
		searchBtn.sendKeys(phoneNumber);
	}

	public void deleteCustomer(String phoneNumber) {
		for (int i = 0; i < customerTuples.size() && i < 1; i++) {
			WebElement temp = customerTuples.get(i).findElement(By.xpath("//td[3]/span"));
			Reporter.log(LineNumber.getLineNumber() + " " + "" + temp.getText());
			WebElement temp1 = customerTuples.get(i).findElement(By.xpath("//td[7]"));
			Reporter.log(LineNumber.getLineNumber() + " " + "" + temp1.getText());
			assertThat(temp1.getText()).withFailMessage("More Than one entry for this phone Number " + phoneNumber)
					.isEqualTo(phoneNumber);
			String xpath = "//td[10]/div/button[2]/i";
			WebElement btn = customerTuples.get(i).findElement(By.xpath(xpath));
			btn.click();
			aw.waitAllRequest();
			delete.click();
			ok.click();
		}
	}
}
