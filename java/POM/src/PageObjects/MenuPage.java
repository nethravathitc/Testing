package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MenuPage {
	WebDriver driver;
	Wait wait;
	By C1ethnicwear= By.cssSelector("body > nav > div > div > div:nth-child(3) > div:nth-child(2) > a");
	By C2kurtakurti= By.cssSelector("body > nav > div > div > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > div > a");
	By C3Kurta= By.cssSelector("body > nav > div > div > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > div > div:nth-child(2) > div:nth-child(1) > a");
	
	By C1bags= By.cssSelector("body > nav > div > div > div:nth-child(3) > div:nth-child(6) > a");
	By C2HandbagsTotes = By.cssSelector("body > nav > div > div > div:nth-child(3) > div:nth-child(6) > div:nth-child(2) > div > a");
	By C3handbags= By.cssSelector("body > nav > div > div > div:nth-child(3) > div:nth-child(6) > div:nth-child(2) > div > div:nth-child(2) > div:nth-child(1) > a");
	
	
	public MenuPage(WebDriver driver){
		this.driver=driver;
		wait=new WebDriverWait(driver, 10);
	}
	
	public void clickC1EthnicWear(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(C1ethnicwear));
		driver.findElement(C1ethnicwear).click();
		
	}
	
	public void clickC2kurtakuti(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(C2kurtakurti));
		driver.findElement(C2kurtakurti).click();
		
	}
	
	public void clickC3kurta(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(C3Kurta));
		driver.findElement(C3Kurta).click();
		
	}
	public void clickC1Bags(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(C1bags));
		driver.findElement(C1bags).click();
		
	}
	
	public void clickC2HandbagsTotes(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(C2HandbagsTotes));
		driver.findElement(C2HandbagsTotes).click();
		
	}
	
	public void clickC3Handbags(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(C3handbags));
		driver.findElement(C3handbags).click();
		
	}
	
	

}
