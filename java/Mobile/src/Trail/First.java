package Trail;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;



public class First {
	WebDriver driver;
  @Test
  public void f() {
	  try {
		System.out.println("First test");
		  System.setProperty("webdriver.chrome.driver","/home/styletag/Desktop/automation/util/chromedriver");
		  driver = new ChromeDriver();
		  driver.get("http://www.styletag.com");
		  driver.quit();
	} catch (Exception e) {
		
		e.printStackTrace();
	}
  }
}
