package util.com.vilcart.util;

import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeSuite;

import static io.github.bonigarcia.wdm.WebDriverManager.isDockerAvailable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;

public class BaseSuiteMethods {
	protected WebDriver driver;
	protected WebDriver driver1;
	protected WebDriverManager wdm;

//  @Test
//  public void f() {
//  }
	@BeforeSuite
	public void beforeSuite(ITestContext context) throws IOException {
		//http://makeseleniumeasy.com/2020/01/06/testng-tutorials-68-sharing-data-between-tests-in-a-suite-using-isuite-itestcontext/
		//https://stackoverflow.com/questions/50347922/how-to-get-the-current-class-driver-in-itestlistener
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		if (ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.docker.enabled").contentEquals("true")) {
			WebDriverManager.chromedriver().setup();
			driver1 = new ChromeDriver();
			wdm = WebDriverManager.chromedriver().browserInDocker().enableVnc();
			wdm.dockerShmSize("2g");
			assumeThat(isDockerAvailable()).isTrue();
			driver = wdm.create();

			URL noVncUrl = wdm.getDockerNoVncUrl();
			assertThat(noVncUrl).isNotNull();
			Reporter.log(noVncUrl + "", true);

			driver1.get(noVncUrl + "");
			context.setAttribute("WebDriver", driver);
		} else {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			context.setAttribute("WebDriver", driver);
		}
	}

	@AfterSuite
	public void afterSuite() throws IOException, InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp(), true);
		Thread.sleep(1000);

		driver.quit();
		if (ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.docker.enabled").contentEquals("true")) {
			driver1.quit();
			wdm.quit();
		}
	}

}
