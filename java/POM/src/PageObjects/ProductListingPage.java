package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductListingPage {
	WebDriver driver;
	Wait wait;
	By product=By.cssSelector("#products-wrap > product-list:nth-child(2) > div:nth-child(1) > div.product-image.pos-rel > a > img");
	
	
	public ProductListingPage(WebDriver driver){
		this.driver=driver;
		wait=new WebDriverWait(driver, 10);
	}
	
	public void clickProduct(){
		wait.until(ExpectedConditions.visibilityOfElementLocated(product));
		driver.findElement(product).click();
	}
	
	

}
