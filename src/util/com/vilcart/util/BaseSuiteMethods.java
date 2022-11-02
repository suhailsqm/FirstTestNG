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
	public void beforeSuite() throws IOException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp());
		if (ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.docker.enabled").contentEquals("true")) {
			WebDriverManager.chromedriver().setup();
			driver1 = new ChromeDriver();
			wdm = WebDriverManager.chromedriver().browserInDocker().enableVnc();
			wdm.dockerShmSize("2g");
			assumeThat(isDockerAvailable()).isTrue();
			driver = wdm.create();

			URL noVncUrl = wdm.getDockerNoVncUrl();
			assertThat(noVncUrl).isNotNull();
			Reporter.log(noVncUrl + "");

			driver1.get(noVncUrl + "");
		} else {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
	}

	@AfterSuite
	public void afterSuite() throws IOException, InterruptedException {
		Reporter.log("=>" + CurrentMethod.methodName() + " " + TimeStamp.CurTimeStamp());
		Thread.sleep(1000);

		driver.quit();
		if (ReadPropertiesFile.readPropertiesFile().getProperty("vilcart.docker.enabled").contentEquals("true")) {
			driver1.quit();
			wdm.quit();
		}
	}

}
