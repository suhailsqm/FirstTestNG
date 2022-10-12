package pom.com.vilcart.pom.settlement;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

import static org.assertj.core.api.Assertions.assertThat;

public class Settlement {
	private WebDriver driver;
	private AngularWait aw;
	private JavascriptExecutor js;

	@FindBy(xpath = "//*[@id=\"startDate\"]")
	private WebElement startDate;

	@FindBy(xpath = "//*[@id=\"endDate\"]")
	private WebElement endDate;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement submit;

	@FindBy(xpath = "//*[@id=\"settlementTuple\"]")
	private List<WebElement> settlementTuples;

	@FindBy(xpath = "//*[@id=\"totalTuple\"]")
	private WebElement totalTuple;

	@FindBy(xpath = "//*[@id=\"paginationDiv\"]/pagination-controls/pagination-template/ul/li[4]")
	private WebElement next;

	public Settlement(WebDriver driver, AngularWait aw) {
		this.driver = driver;
		this.aw = aw;
		this.js = ((JavascriptExecutor) this.driver);
		PageFactory.initElements(driver, this);
	}

	public void getTuplesForCurrentDate() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				startDate);

		js.executeScript("arguments[0].value = '" + getDate() + "';arguments[0].dispatchEvent(new Event('input'))",
				endDate);
		submit.click();
		aw.waitAllRequest();
	}

	private String getDate() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime indiaDateTime = now.withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
		return dtf.format(indiaDateTime);
	}

	public void tallySettlement() {
		Reporter.log("==>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		int cumInvoiceAmt = 0;
		int cumCashReceived = 0;
		int cumAmtReceived = 0;

		for (int i = 0; i < settlementTuples.size(); i++) {
			String temp = settlementTuples.get(i).findElement(By.xpath("//td[4]")).getText().trim();
			cumInvoiceAmt += Integer.parseInt(temp);
			temp = settlementTuples.get(i).findElement(By.xpath("//td[8]")).getText().trim();
			cumCashReceived += Integer.parseInt(temp);
			temp = settlementTuples.get(i).findElement(By.xpath("//td[11]")).getText().trim();
			cumAmtReceived += Integer.parseInt(temp);
		}

		for (; next.isEnabled(); next.click()) {
			for (int i = 0; i < settlementTuples.size(); i++) {
				String temp = settlementTuples.get(i).findElement(By.xpath("//td[4]")).getText().trim();
				cumInvoiceAmt += Integer.parseInt(temp);
				temp = settlementTuples.get(i).findElement(By.xpath("//td[8]")).getText().trim();
				cumCashReceived += Integer.parseInt(temp);
				temp = settlementTuples.get(i).findElement(By.xpath("//td[11]")).getText().trim();
				cumAmtReceived += Integer.parseInt(temp);
			}
		}

		assertThat(cumInvoiceAmt).withFailMessage("Invoice Amt Doesn't tally in settlement")
				.isEqualTo(totalTuple.findElement(By.xpath("//td[4]")));
		assertThat(cumCashReceived).withFailMessage("cash received Doesn't tally in settlement")
				.isEqualTo(totalTuple.findElement(By.xpath("//td[8]")));
		assertThat(cumAmtReceived).withFailMessage("Amount received Doesn't tally in settlement")
				.isEqualTo(totalTuple.findElement(By.xpath("//td[11]")));
	}
}
