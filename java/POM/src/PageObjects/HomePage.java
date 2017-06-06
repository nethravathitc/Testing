package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
	WebDriver driver;
	Wait wait;
	By sliderMenu= By.cssSelector("#wrapper > div.navbar.navbar-app.navbar-fixed-top.has-navbar-top > div.btn-group.pull-left > div > i");
	By loginLink= By.cssSelector("#st-login");
	
	
	public HomePage(WebDriver driver){
		this.driver= driver;
		wait= new WebDriverWait(driver, 10);
		
	}
	
	public void clickSliderMenu(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(sliderMenu));
		driver.findElement(sliderMenu).click();
	}
	
	public void clickLoginLink(){
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(loginLink));
		driver.findElement(loginLink).click();
	}

}
