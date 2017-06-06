package TestScripts;

import org.testng.annotations.Test;

import Library.LaunchSite;
import PageObjects.HomePage;
import PageObjects.LoginPage;

import org.testng.annotations.BeforeTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;

public class LoginPageTest {

	WebDriver driver;
	String home;
	LoginPage Login;
	HomePage HomePage;
  @BeforeTest
  public void setUp() {
	  
	  driver=LaunchSite.startBrowser();
	  home=LaunchSite.HOME;
	  Login= new LoginPage(driver);
	  HomePage= new HomePage(driver);
	  
  }

 @AfterTest
  public void afterTest() {
	 System.out.println("Closing the driver");
	  driver.close();
  }
  @Test
  public void loginIntoAccount() {
	  HomePage.clickSliderMenu();
	  HomePage.clickLoginLink();
	  Login.putEmailID("test123@mailinator.com");
	  Login.putPwd("123456");
	  Login.clickLoginbtn();
  }

}
