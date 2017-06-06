package PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductDetailPagePF {
	
	WebDriver driver;
	Wait wait;
	public ProductDetailPagePF(WebDriver driver){
		this.driver=driver;
		wait= new WebDriverWait(driver, 10);
	}
	
	@FindBy(how=How.CSS, using=("#add-to-cart-button")) 
	WebElement AddToCartBtn;

	public void clickAddToCartBtn(){
		wait.until(ExpectedConditions.visibilityOf(AddToCartBtn));
		AddToCartBtn.click();
		
	}
}
