package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	By email = By.cssSelector("#login_email");
	By pwd= By.cssSelector("#login_password");
	By loginBtn= By.cssSelector("#login-btn");
	
	WebDriver driver;
	
	public LoginPage(WebDriver driver)
	{
		this.driver=driver;
	}
	
	public void  putEmailID(String id){
	
		driver.findElement(email).clear();
		driver.findElement(email).sendKeys(id);
	}
	
	public void putPwd(String pass){
		driver.findElement(pwd).clear();
		driver.findElement(pwd).sendKeys(pass);
	}
	
	public void clickLoginbtn(){
		driver.findElement(loginBtn).click();
	}

}
