import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class LaunchBrowser {

	public static void main(String[] args) {
		DesiredCapabilities capability = new DesiredCapabilities().android();
		capability.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
		capability.setCapability(MobileCapabilityType.PLATFORM,Platform.ANDROID);
		capability.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		capability.setCapability(MobileCapabilityType.DEVICE_NAME,"genymotion");
		capability.setCapability(MobileCapabilityType.VERSION, "5.1");
		capability.setCapability(MobileCapabilityType.NO_RESET,Boolean.TRUE);
		URL url;
		try {
			
			url= new URL("http://127.0.0.1:4723/wd/hub");
		
			WebDriver driver= new AndroidDriver(url, capability);
			driver.get("http://www.styletag.com");
			System.out.println("Title: "+driver.getTitle());
			
			//clicking on side bar
			Wait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#wrapper > div.navbar.navbar-app.navbar-fixed-top.has-navbar-top > div.btn-group.pull-left > div > i")));
			driver.findElement(By.cssSelector("#wrapper > div.navbar.navbar-app.navbar-fixed-top.has-navbar-top > div.btn-group.pull-left > div > i")).click();
			
			//clicking on login link
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#st-login")));
			driver.findElement(By.cssSelector("#st-login")).click();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			
			driver.findElement(By.cssSelector("#login_email")).clear();
			driver.findElement(By.cssSelector("#login_email")).sendKeys("nethravathi.tc@styletag.com");
			
			driver.findElement(By.cssSelector("#login_password")).clear();
			driver.findElement(By.cssSelector("#login_password")).sendKeys("123456");
			
			driver.findElement(By.cssSelector("#login-btn")).click();

		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		

	}

}
