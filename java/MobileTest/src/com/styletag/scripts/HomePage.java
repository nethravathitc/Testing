package com.styletag.scripts;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class HomePage {
	WebDriver driver;
	Logger log;
	String actual;
	String expected;
	SoftAssert softassert;
	public void printException(AssertionError e) // to write the e.printstacktrace to Error sheet
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String msg=sw.toString();
		log.info("inside printException function");
		log.info(msg);
		
	}
	@BeforeTest
	public void setDriver()
	{
		LaunchBrowser obj= new LaunchBrowser();
		driver=obj.webdriver;
		log=obj.log;
		softassert=new SoftAssert();
	}
	
  @Test
  public void urlTest() {
	  
		//Thread.sleep(500);
	  
		try
		{
			log.info("inside urlTest");
			actual= driver.getCurrentUrl();
			expected="http://m.styletag.com/";
			System.out.println("Current URL: "+actual);
			System.out.println("Expected URL "+expected);
			
			//softassert.assertTrue(false,"Assertion Failed");
			
			softassert.assertEquals(actual, expected, "Actual is not equal to expected");
			//Assert.assertEquals(actual, expected, "Actaul is not equal to expected");
			//Assert.assertEquals(actual, expected,"Assertion Failed");
			softassert.assertAll();
		} 
		catch (AssertionError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
			log.error("Error in the function", e);
			log.info("error in the function", e);
			printException(e);
		}
			  
  }
  
  @Test
  public void sliderMenu()
  {
	  //Home Icon, Cross Button, L1 Category Menu, L2 Category Menu
	
	  	try {
			//clicking on side bar
	  		log.info("clicking on the slider menu");
			Wait wait=new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#wrapper > div.navbar.navbar-app.navbar-fixed-top.has-navbar-top > div.btn-group.pull-left > div > i")));
			driver.findElement(By.cssSelector("#wrapper > div.navbar.navbar-app.navbar-fixed-top.has-navbar-top > div.btn-group.pull-left > div > i")).click();
			
			log.info("checking for the Shop By Category text");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body > nav > div > div > div:nth-child(2)")));
			WebElement ShopByCat = driver.findElement(By.cssSelector("body > nav > div > div > div:nth-child(2)"));
			actual= ShopByCat.getText();
			expected="SHOP BY CATEGORY";
			
			softassert.assertEquals(actual,expected,"Shop By Category text mismatch");
			
			log.info("checking for My Account");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body > nav > div > div > div:nth-child(4)")));
			WebElement MyAccount = driver.findElement(By.cssSelector("body > nav > div > div > div:nth-child(4)"));
			actual= MyAccount.getText();
		
	  	
	  	} catch (AssertionError e) {
			
			e.printStackTrace();
			Assert.fail();
		}
	  	catch (Exception e)
	  	{
	  		e.printStackTrace();
	  		Assert.fail();
	  	}
		
	
		
  }
  
  @AfterTest
  public void closeDriver()
  {
	  log.info("closing driver");
	  driver.quit();
  }
}
