package com.styletag.scripts;

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

public class Login {
	WebDriver driver;
	Logger log;
	SoftAssert softassert;
	String actual,expected;
	String home;
	
	@BeforeTest
	public void setDriver()
	{
		LaunchBrowser obj= new LaunchBrowser();
		driver=obj.webdriver;
		log=obj.log;
		softassert=new SoftAssert();
		home=obj.HOME;
	}
	
	//@AfterTest
	public void closeDriver()
	{
		log.info("closing the driver");
		driver.close();
	}
	
	  @Test (priority=0)
	  public void sliderMenu()
	  {
		  //Home Icon, Cross Button, L1 Category Menu, L2 Category Menu // Login link click
		
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
				
				//clicking on login link
				log.info("clicking on login link");
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#st-login")));
				driver.findElement(By.cssSelector("#st-login")).click();
				
			
		  	
		  	} catch (Error e) {
				
				e.printStackTrace();
				Assert.fail();
			}catch (Exception e)
		  	{
				e.printStackTrace();
				Assert.fail();
		  	}
					
	  }
	  
	  @Test (priority=1)
	  public void loginPageTest()
	  {
		  try {
			// checking URL
			  expected=home+"/login#ref=%2F%3FuiSidebarLeft";
			  actual=driver.getCurrentUrl();
			  
			  softassert.assertEquals(actual, expected, "Login URL is incorrect");
			 
			
				driver.findElement(By.cssSelector("#login_email")).clear();
				driver.findElement(By.cssSelector("#login_email")).sendKeys("nethravathi.tc@styletag.com");
				
				driver.findElement(By.cssSelector("#login_password")).clear();
				driver.findElement(By.cssSelector("#login_password")).sendKeys("123456");
				
				driver.findElement(By.cssSelector("#login-btn")).click();
		} catch (Exception e) {
			
			e.printStackTrace();
			Assert.fail();
		}catch (Error e){
			e.printStackTrace();
			Assert.fail();
			
		  }
			
			
	  }
	  
	
}
