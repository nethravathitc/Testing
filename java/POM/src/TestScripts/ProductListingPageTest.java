package TestScripts;

import org.testng.annotations.Test;

import Library.LaunchSite;
import PageObjects.HomePage;
import PageObjects.MenuPage;
import PageObjects.ProductListingPage;

import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;

public class ProductListingPageTest {
	WebDriver driver;
	HomePage HomePage;
	String home;
	MenuPage menu;
	
 
  @BeforeMethod
  public void setUp() {
	  driver=LaunchSite.startBrowser();
	  home=LaunchSite.HOME;
	  HomePage = new HomePage(driver);
	 menu= new MenuPage(driver);
  }

  @AfterClass
  public void afterClass() {
	  System.out.println("Quiting driver");
  }
  
  @Test
  public void gotoProductListing() {
	  HomePage.clickSliderMenu();
	  menu.clickC1EthnicWear();
	  menu.clickC2kurtakuti();
	  menu.clickC3kurta();
	  
	  
  }

}
