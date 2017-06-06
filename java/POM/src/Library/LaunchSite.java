package Library;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class LaunchSite {
	public static WebDriver driver;
	//public static Logger log;
	public static String HOME;
	public static Properties property;
	static DesiredCapabilities capability;
	

	public static WebDriver startBrowser(){
		
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
				
				//log.info("Setting capabilities");
				System.out.println("Setting capabilities");
				
				capability = new DesiredCapabilities().android();
				capability.setCapability(MobileCapabilityType.BROWSER_NAME,BrowserName);
				capability.setCapability(MobileCapabilityType.PLATFORM,Platforms);
				capability.setCapability(MobileCapabilityType.PLATFORM_NAME,PlatformName );
				capability.setCapability(MobileCapabilityType.DEVICE_NAME,DeviceName);
				capability.setCapability(MobileCapabilityType.VERSION, Version);
				capability.setCapability(MobileCapabilityType.NO_RESET,Boolean.TRUE);
			 
				HOME=property.getProperty("HOME_URL");
				
				URL url;
				url= new URL("http://127.0.0.1:4723/wd/hub");
				driver= new AndroidDriver(url, capability);
				
			//	log.info("Launching "+HOME);
				driver.get(HOME);
				driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
				
			//	log.info("Current url in launch Browser"+webdriver.getCurrentUrl());
				//System.out.println("Current url in launch Browser"+webdriver.getCurrentUrl());
				//log.info("Title: "+webdriver.getTitle());
				//System.out.println("Title: "+webdriver.getTitle());
				
			  } catch (Exception e) {
				System.out.println("couldnot launch browser");
				e.printStackTrace();
			  }
		 return driver;
		
		
	}
	public static String HomePage(){
		
		HOME=property.getProperty("HOME_URL");
		return HOME;
	}
}
