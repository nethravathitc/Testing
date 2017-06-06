package TestScripts;

import org.testng.annotations.Test;

import Library.LaunchSite;
import PageObjects.HomePage;
import PageObjects.MenuPage;
import PageObjects.ProductDetailPagePF;
import PageObjects.ProductListingPage;

import org.testng.annotations.BeforeTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;

public class ProductDetailPageTest {
	WebDriver driver;
	HomePage HomePage;
	String home;
	MenuPage menu;
	ProductListingPage PL;
	ProductDetailPagePF PD;
	
 
  @BeforeTest
  public void setUP() {
	  driver=LaunchSite.startBrowser();
	  home=LaunchSite.HOME;
	  HomePage = new HomePage(driver);
	  menu=new MenuPage(driver);
	  PD= PageFactory.initElements(driver, ProductDetailPagePF.class);
	  PL= new ProductListingPage(driver);
  }

  @AfterTest
  public void afterTest() {
	  System.out.println("closing driver");
	  driver.close();
  }

  @Test
  public void gotoProductDetailPage() {
	 System.out.println("clicking on the slider");
	  HomePage.clickSliderMenu();
	  
	  System.out.println("clicking categories");
	  menu.clickC1Bags();
	  menu.clickC2HandbagsTotes();
	  menu.clickC3Handbags();
	  
	  System.out.println("clicking on product");
     PL.clickProduct();
	  
	  System.out.println("clicking on ADD_TO_CART button");
	  PD.clickAddToCartBtn();
  
  
  }
}
