package com.styletag.functional_lib;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;

public class Utils {
	public WebDriver webdriver ;
	String url;
	public void launchStyletag(String url){
		System.setProperty("webdriver.chrome.driver","//home//styletag//Documents//chromedriver");
		webdriver = new ChromeDriver();
		webdriver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
		webdriver.get(url);
		webdriver.manage().window().maximize();
		}
	

}
