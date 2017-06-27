package com.styletag.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class LaunchBrowser {
	public static WebDriver webdriver;
	public static Logger log;
	URL url;
	public static String HOME;
	DesiredCapabilities capability;
	Properties property;
	
	
	
	LaunchBrowser()
	{
		// creating log object
		 log= Logger.getLogger("newLog");
		 PropertyConfigurator.configure("logj4.properties");
		 
		 
		 
	 try {
		 
		File file= new File(System.getProperty("user.dir")+"//ApplicationConfig.xml");
		FileInputStream fileinput= new FileInputStream(file);
		property = new Properties();
		property.loadFromXML(fileinput);
		fileinput.close();
		
		String BrowserName= property.getProperty("BrowserName");
		String Platforms= property.getProperty("Platforms");// this has to be platforms atherwise it will contradict with "platform.ANDROID" variable
		String PlatformName = property.getProperty("PlatformName");
		String DeviceName= property.getProperty("DeviceName");
		String Version= property.getProperty("Version");
		
		log.info("Setting capabilities");
		capability = new DesiredCapabilities().android();
		capability.setCapability(MobileCapabilityType.BROWSER_NAME,BrowserName);
		capability.setCapability(MobileCapabilityType.PLATFORM,Platforms);
		capability.setCapability(MobileCapabilityType.PLATFORM_NAME,PlatformName );
		capability.setCapability(MobileCapabilityType.DEVICE_NAME,DeviceName);
		capability.setCapability(MobileCapabilityType.VERSION, Version);
		capability.setCapability(MobileCapabilityType.NO_RESET,Boolean.TRUE);
	 
		HOME=property.getProperty("HOME_URL");
		url= new URL("http://127.0.0.1:4723/wd/hub");
		webdriver= new AndroidDriver(url, capability);
		
		log.info("Launching "+HOME);
		webdriver.get(HOME);
		webdriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		log.info("Current url in launch Browser"+webdriver.getCurrentUrl());
		//System.out.println("Current url in launch Browser"+webdriver.getCurrentUrl());
		log.info("Title: "+webdriver.getTitle());
		//System.out.println("Title: "+webdriver.getTitle());
		
	  } catch (Exception e) {
		System.out.println("couldnot launch browser");
		e.printStackTrace();
	  }
	  
  }
}
