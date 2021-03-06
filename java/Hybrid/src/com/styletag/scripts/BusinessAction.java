package com.styletag.scripts;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.styletag.functionalLib.*;
import com.styletag.uiobjects.UIobjects;

public class BusinessAction {
	WebDriverWait wait;
	public WebDriver webdriver;
	Actions act;
	String pd_product_name ,ct_product_name,orderNo;
	int sort_flag,COD_flag=0,CC_flag=0,DC_flag=0,NB_flag=0;
	ExcelRead xl;
	String msg;
	ExcelWrite write;
	DateFormat df;
	Date dateobj;
	String date;
	//CartItem[] cartitems;
	public List<CartItem> cart_item_list= new ArrayList<CartItem>(); // declaring ArrayList of cart item. needs reset for new senario - done in Driver class
	static int product_num=1;// 1 means first product in the listing page
	public String[] PD_product_name=new String[10];// declaring the size of PD_product_name array. needs reset for new senario - done in Driver class
	int ADD_TO_CART_BTN=1, BUY_NOW_BTN=0;
	
	
	public BusinessAction(ExcelWrite write1){
		xl=new ExcelRead(System.getProperty("user.dir")+Driver.properties.getProperty("InputDataFile"));
		write= write1;
		df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	    dateobj = new Date();
	    date =df.format(dateobj);
	    String sd[] = date.split("\\s");
	    System.out.println(sd[0]);
	    System.out.println(sd[1]);
	    sd[0]=sd[0].replaceAll("\\/","_");
	    sd[1]=sd[1].replaceAll(":","_");
	    date="_date_"+sd[0]+"_time_"+sd[1];
		
	}
	
	
	public void launchStyletag(String url){
		System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+Driver.properties.getProperty("ChromeDriver"));
		webdriver = new ChromeDriver();
		webdriver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
		webdriver.get(url);
		webdriver.manage().window().maximize();
		}
	public void printException(Exception e) // to write the e.printstacktrace to Error sheet
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		msg=sw.toString();
		write.writeReports("Error", "stacktrace o/p", Driver.column);
		write.writeReports("Error", msg, Driver.column);
	}
	
	public void login() {
		
		try {
			//System.out.println("maximing windows");
			//spinner();
			Thread.sleep(3500);//it is required to get rid of flash msg. ex: if login happen as soon as logout flash msg hides the required UI element
			String  URL1 = webdriver.getCurrentUrl();
			msg="current page is: "+"'"+URL1+"'";
			System.out.println(msg);
			//write.writeReports("Log",msg, Driver.column);
						
			wait= new WebDriverWait(webdriver,10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.login_name_css)));
			WebElement login_name= webdriver.findElement(By.cssSelector(UIobjects.login_name_css));
			act = new Actions(webdriver);
			System.out.println("calling perform function");
			//Thread.sleep(5);
			act.moveToElement(login_name).build().perform();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.login_link_css)));
			webdriver.findElement(By.cssSelector(UIobjects.login_link_css)).click();
			System.out.println("entering login details");
			
			int no =xl.rowCountInSheet(1);// to set the sheet as well
			//System.out.println("total count "+no);						
			int i=2; String emailid,pwd;
			String failed_data="";
			WebElement emailid_textbox=webdriver.findElement(By.cssSelector(UIobjects.login_email_css));
			WebElement pwd_textbox=webdriver.findElement(By.cssSelector(UIobjects.login_pass_css));
			WebElement login_button=webdriver.findElement(By.cssSelector(UIobjects.login_btn_css));
			
			// for INVALID DATA
			// Login Button should not be enabled for these data pattern
			msg="checking Login Button disability";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			while(!(xl.read(i,0)).equals("LoginButton enabled"))
			{	
				System.out.println("Inside first while loop");
				emailid=xl.read(i,0);
				pwd=xl.read(i, 1);
				System.out.println(i);
				System.out.println("\nemailid: "+emailid+"  password: "+pwd);
				
				//Clear the text boxes;
				emailid_textbox.clear();
				pwd_textbox.clear();
				
				//enter data 
				emailid_textbox.sendKeys(emailid);
				pwd_textbox.sendKeys(pwd);
				
				Thread.sleep(500);
				
				if((login_button.isEnabled()))// FAIL if login button is enabled
				{
					System.out.println("i value is "+i+i);
					msg="Login button enabled for following input";
					System.out.println(msg);
									
					Driver.FLAG=0;
					write.writeReports("Log","FAIL",Driver.column);
					write.writeReports("Error",msg,Driver.column);
					
					msg="emailid: "+emailid+" pwd: "+pwd+"- fail";
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Error", msg, Driver.column);
					System.out.println(msg);
										
				}
				if(!(login_button.isEnabled()))
				{
					System.out.println("Login Button is not enabled");
					msg="emaiid:"+emailid+" pwd:"+pwd+" - pass";
					write.writeReports("Log",msg,Driver.column);
					
				}
				i++;
				
			}
						
			i++; // to point to next row
			
			// Login Button will be enabled but login should not be successful
			// this is for checking non valid data ie compared with DB
			msg="Checking for Unsuccessful Login";
			write.writeReports("Log",msg,Driver.column);
			System.out.println(msg);
			while(!(xl.read(i,0)).equals("Valid data"))
			{	
				
				System.out.println("Inside second while");
				emailid=xl.read(i,0);
				pwd=xl.read(i, 1);
				System.out.println(i);
				System.out.println("\nemailid:  "+emailid+" password: "+pwd);
				
				//Clear the text boxes;
				emailid_textbox.clear();
				pwd_textbox.clear();
				
				//enter data 
				emailid_textbox.sendKeys(emailid);
				pwd_textbox.sendKeys(pwd);

				
				if(login_button.isEnabled())
				{
					System.out.println("\nLogin Button enabled and clicking on the button");
					login_button.click();
					Thread.sleep(2000);
					WebElement login_flash_msg=webdriver.findElement(By.cssSelector(UIobjects.login_flash_msg_css));
					if(login_flash_msg.isDisplayed())
					{	
						String text=login_flash_msg.getText();
						String[] flash_msg_array=text.split("\n");
					System.out.println("array value:"+flash_msg_array[0]+"end");
					//System.out.println(text);
						int flag=0;
						if(!(flash_msg_array[0].equals("Sorry! Invalid email/password combination. Please try again.")))// Fail if() condition is true
						{
							System.out.println("inside if");
						Driver.FLAG=0;
						write.writeReports("Log","FAIL",Driver.column);
						msg="emailid: "+emailid+" pwd: "+pwd;
						write.writeReports("Error", msg,Driver.column);
						System.out.println(msg);
						
						msg="Flash msg is: "+flash_msg_array[0];
						write.writeReports("Error",msg,Driver.column);
						System.out.println(msg);
						flag=1;
						}
						if (flag!=1) // only to write pass to Log
						{
							msg="emailid: "+emailid+" pwd: "+pwd+" - pass";
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						System.out.println("inside else");
						Driver.FLAG=0;
						write.writeReports("Log","FAIL",Driver.column);
						msg="emailid: "+emailid+" pwd: "+pwd+" - fail";
						write.writeReports("Error", msg,Driver.column);
						System.out.println(msg);
						
						msg="Flash msg 'Sorry! Invalid email/password combination. Please try again' is not displayed ";
						write.writeReports("Error",msg,Driver.column);
						System.out.println(msg);
						
					}
					
				
				}i++;
			}
			
			i++; // to point to next row
			
			// Checking for VALID DATA
			//Login should be successful
			msg="checking for Successful Login";
			write.writeReports("Log",msg,Driver.column);
			System.out.println(msg);
			emailid=xl.read(i,0);
			pwd=xl.read(i, 1);
			
			//Clear the text boxes;
			emailid_textbox.clear();
			pwd_textbox.clear();
			
			//enter data 
			emailid_textbox.sendKeys(emailid);
			pwd_textbox.sendKeys(pwd);
			
			if(login_button.isEnabled())
			{
				msg="emailid: "+emailid+" pwd"+pwd+" valid data";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				System.out.println("\nLogin Button enabled and clicking on the button");
				login_button.click();
				Thread.sleep(2000);
				
				WebElement login_flash_msg=webdriver.findElement(By.cssSelector(UIobjects.login_flash_msg_css));
				if(login_flash_msg.isDisplayed())
				{
					
					String text=login_flash_msg.getText();
					String[] array=text.split("\n");
					//System.out.println("array value: "+array[0]);
					msg="Flash is displayed ";
					write.writeReports("Log", msg,Driver.column);
					
							
					if((array[0].equals("Successfully logged in")))
					{
						msg="Flash msg is: "+array[0];
						write.writeReports("Log",msg,Driver.column);
						
						WebElement acc_mem_name=webdriver.findElement(By.cssSelector(UIobjects.acc_name_css));
						if(acc_mem_name.isDisplayed())
						{
							msg=acc_mem_name.getText()+" - displayed";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							Driver.FLAG++;
							write.writeReports("Log", "PASS", Driver.column);
						
						}else
						{
							Driver.FLAG=0;
							msg="account name is not being displayed";
							write.writeReports("Log","FAIL",Driver.column);
							write.writeReports("Error", msg,Driver.column);
						}
						
						Thread.sleep(3000);// this required for the URL to change if in case
						
						String URL2=webdriver.getCurrentUrl();
						msg="current page is: "+"'"+URL2+"'";
						System.out.println(msg);
						//write.writeReports("Log",msg, Driver.column);
						
						
						if(URL1.contains("email"))// checking login at checkout/email page
						{
							if(URL2.contains("address")) // should be redirected to ../address page
							{
								msg="Redirected to the correct page ";
								write.writeReports("Log",msg, Driver.column);
							}
							else
							{
								msg="Redirection didnt happened to correct page";
								write.writeReports("Log", msg, Driver.column);
								write.writeReports("Log","FAIL", Driver.column);
								write.writeReports("Error",msg,Driver.column);
								msg="URL before Login: '"+URL1+"'";
								write.writeReports("Error", msg, Driver.column);
								msg="URL after Login: '"+URL2+"'";
								write.writeReports("Error",msg, Driver.column);
							}
							
						}
						else
						{
							if(URL1.equals(URL2))
							{
								msg="Redirected to the correct page ";
								write.writeReports("Log",msg, Driver.column);
							}
							else
							{
								msg="Redirection didnt happened to correct page";
								write.writeReports("Log", msg, Driver.column);
								write.writeReports("Log","FAIL", Driver.column);
								write.writeReports("Error",msg,Driver.column);
								msg="URL before Login: '"+URL1+"'";
								write.writeReports("Error", msg, Driver.column);
								msg="URL after Login: '"+URL2+"'";
								write.writeReports("Error",msg, Driver.column);
							}
						}
						
						
						
					}else
					{
						msg="Flash msg is not equal to 'Successfully logged in'";
						Driver.FLAG=0;
						write.writeReports("Log","FAIL",Driver.column);
						write.writeReports("Error",msg,Driver.column);
					}
					
				}else
				{
					System.out.println("Flash msg is not displayed");
					
					Driver.FLAG=0;
				}
				
			}else
			{
				msg="Login Button is not enabled";
				Driver.FLAG=0;
				write.writeReports("Log", "FAIL",Driver.column);
				write.writeReports("Error",msg,Driver.column);
			}
			
			Thread.sleep(3500); // this is to get rid of flash msh after successful/ failure login
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log", "FAIL",Driver.column);
			write.writeReports("Error",msg,Driver.column);
			printException(e);
		}
	}
	
	
	
public void registerUser()
{
	try {
		Thread.sleep(3500);//it is required to get rid of flash msg. ex: if any
		int no =xl.rowCountInSheet(0);// to set sheet as well. Register data is at sheet 0.
		System.out.println("total no of rows: "+no);
		String name,emailid,pwd;
		//clicking on register link		
		wait= new WebDriverWait(webdriver,10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.login_name_css)));
		WebElement login_name= webdriver.findElement(By.cssSelector(UIobjects.login_name_css));
		act = new Actions(webdriver);
		System.out.println("calling perform function");
		//Thread.sleep(5);
		act.moveToElement(login_name).build().perform();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.register_link_css)));
		webdriver.findElement(By.cssSelector(UIobjects.register_link_css)).click();
					
		//checking for URL
		String current_url= webdriver.getCurrentUrl();
		String expected_url=Driver.HOME_URL+"/register#ref=%2F";
		if(current_url.equals(expected_url))
		{
			msg="Current URL is equal to Expected URL";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
		}
		else
		{
			msg="current URl is not equal to expected URL";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			write.writeReports("Log","FAIL",Driver.column);
			write.writeReports("Error",msg,Driver.column);
			
			msg="current URL: "+current_url;
			System.out.println(msg);
			write.writeReports("Error",msg,Driver.column);
			
		}
		WebElement register_btn=webdriver.findElement(By.cssSelector(UIobjects.register_btn_css));
		WebElement name_textbox=webdriver.findElement(By.cssSelector(UIobjects.regiter_name_css));
		WebElement emailid_textbox=webdriver.findElement(By.cssSelector(UIobjects.register_email_css));
		WebElement pwd_textbox=webdriver.findElement(By.cssSelector(UIobjects.register_pwd_css));
		
		WebElement name_errmsg;
		WebElement emailid_errmsg;
		WebElement pwd_errmsg;
		
		String name_err_text="Minimum 3 characters required.";
		String email_err_text="Enter a valid email address.";
		String pwd_err_text="Minimum 6 characters required.";
		
		//checking invalid name
		msg="Checking for invalid names";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		
		int i=2; // reading from row 2
		name= xl.read(i,0);
		
		do
		{
			
			name_textbox.clear();
			name=xl.read(i, 0);
			System.out.println(name);
			name_textbox.sendKeys(name);
			Thread.sleep(500);
			name_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_name_errmsg_css));
			if(name_errmsg.isDisplayed())
			{
			/*	msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(name_errmsg.getText()).equals(name_err_text))
				{
					msg="error mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="Name: "+name;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+name_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			i++;
			
		}while (!(xl.read(i,0).equals("Invalid email ID")));
		
		//checking invalid emailID
		name_textbox.clear();
		msg="Checking for invalid emailId";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		i++;//to point to next row
		
		do
		{
			emailid_textbox.clear();
			name= xl.read(i,0);
			System.out.println(name);
			emailid_textbox.sendKeys(name);
			Thread.sleep(500);
			emailid_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_emailid_errmsg_css));
			if(emailid_errmsg.isDisplayed())
			{
				/*msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(emailid_errmsg.getText()).equals(email_err_text))
				{
					msg="error mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="Email ID: "+name;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+emailid_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
				
				// checking for Register button enability, button should not be enabled
				if(register_btn.isEnabled())
				{
					msg="Register button enabled";
					System.out.println(msg);
					write.writeReports("Log", msg,Driver.column);
					write.writeReports("Error",msg, Driver.column);
					write.writeReports("Log","FAIL", Driver.column);
					
					msg="Email ID: "+name;
					System.out.println(msg);
					write.writeReports("Error",msg,Driver.column);
				}
				
				
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			i++;
			
		}while (!(xl.read(i, 0).equals("invalid password")));
		
		//checking for invalid password
		emailid_textbox.clear();
		msg="Checking for invalid password";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		i++;//to point to next row
		
		do
		{
			pwd_textbox.clear();
			name= xl.read(i,0);
			System.out.println(name);
			pwd_textbox.sendKeys(name);
			Thread.sleep(500);
			pwd_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_pwd_errmsg_css));
			if(pwd_errmsg.isDisplayed())
			{
				/*msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(pwd_errmsg.getText()).equals(pwd_err_text))
				{
					msg="error mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="Password: "+name;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+pwd_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
				
				// checking for Register button enability, button should not be enabled
				if(register_btn.isEnabled())
				{
					msg="Register button enabled";
					System.out.println(msg);
					write.writeReports("Log", msg,Driver.column);
					write.writeReports("Error",msg, Driver.column);
					write.writeReports("Log","FAIL", Driver.column);
					
					msg="Password: "+name;
					System.out.println(msg);
					write.writeReports("Error",msg,Driver.column);
				}
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			i++;
			
		}while (!(xl.read(i,0).equals("Invalid combination")));
		pwd_textbox.clear();
		
		//checking invalid combination
		//invalid name valid emailid, pwd.
		msg="checking combination: invalid name , valid emailID, valid password";
		System.out.println(msg);
		write.writeReports("Log",msg, Driver.column);
		i=i+3;// pointing to required row
		do{
			name=xl.read(i, 0);
			emailid=xl.read(i, 1);
			pwd=xl.read(i, 2);
			name_textbox.clear();
			emailid_textbox.clear();
			pwd_textbox.clear();
			
			name_textbox.sendKeys(name);
			emailid_textbox.sendKeys(emailid);
			pwd_textbox.sendKeys(pwd);
			
			// only name error should display
			name_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_name_errmsg_css));
			if(name_errmsg.isDisplayed())
			{
			/*	msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(name_errmsg.getText()).equals(name_err_text))
				{
					msg="error mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="Name: "+name;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+name_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			// checking for Register button enability, button should not be enabled
			if(register_btn.isEnabled())
			{
				msg="Register button enabled";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Error",msg, Driver.column);
				write.writeReports("Log","FAIL", Driver.column);
				
				msg="Name: "+name;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
						
				msg="EmailID: "+emailid;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
				
				msg="Password: "+pwd;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
			}
			/*
			// error messages for email and password should not be shown
			try {
				emailid_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_emailid_errmsg_css));
				// if control comes here it means error message is found for valid email id
				msg="Error message displayed forValid  Email ID'"+emailid+"'";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				Driver.FLAG=0;
			} catch (Exception e) {
				
				//e.printStackTrace();
				// if control comes here it means no error message , so no need to log
			}
			try {
				pwd_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_pwd_errmsg_css));
				// if control comes here it means error message is found for valid pwd
				msg="Error message displayed for valid pwd'"+pwd+"'";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				Driver.FLAG=0;
			} catch (Exception e) {
				
				//e.printStackTrace();
				// if control comes here it means no error message , so no need to log
			}
			*/
			i++;
		}while(!(xl.read(i, 0).equals("Invalid email ID")));
		
		// checking invalid email ID, valid name and valid password
		msg=" checking invalid email ID, valid name and valid password";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		i++;// to point to next row
		
		do{
			name=xl.read(i, 0);
			emailid=xl.read(i, 1);
			pwd=xl.read(i, 2);
			name_textbox.clear();
			emailid_textbox.clear();
			pwd_textbox.clear();
			
			name_textbox.sendKeys(name);
			emailid_textbox.sendKeys(emailid);
			pwd_textbox.sendKeys(pwd);
			
			// only name error should display
			emailid_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_emailid_errmsg_css));
			if(emailid_errmsg.isDisplayed())
			{
			/*	msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(emailid_errmsg.getText()).equals(email_err_text))
				{
					msg="error mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="Email ID: "+emailid;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+emailid_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			// checking for Register button enability, button should not be enabled
			if(register_btn.isEnabled())
			{
				msg="Register button enabled";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Error",msg, Driver.column);
				write.writeReports("Log","FAIL", Driver.column);
				
				msg="Name: "+name;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
						
				msg="EmailID: "+emailid;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
				
				msg="Password: "+pwd;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
			}
			
			i++;
		}while(!(xl.read(i, 0).equals("invalid password")));
		
		// checking invalid password, valid name and valid emailID
		msg=" checking invalid password, valid name and valid emailID";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		i++;// to point to next row
		
		do{
			name=xl.read(i, 0);
			emailid=xl.read(i, 1);
			pwd=xl.read(i, 2);
			name_textbox.clear();
			emailid_textbox.clear();
			pwd_textbox.clear();
			
			name_textbox.sendKeys(name);
			emailid_textbox.sendKeys(emailid);
			pwd_textbox.sendKeys(pwd);
			
			// only name error should display
			pwd_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_pwd_errmsg_css));
			if(pwd_errmsg.isDisplayed())
			{
			/*	msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(pwd_errmsg.getText()).equals(pwd_err_text))
				{
					msg="error message mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="Password: "+pwd;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+pwd_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			// checking for Register button enability, button should not be enabled
			if(register_btn.isEnabled())
			{
				msg="Register button enabled";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Error",msg, Driver.column);
				write.writeReports("Log","FAIL", Driver.column);
				
				msg="Name: "+name;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
						
				msg="EmailID: "+emailid;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
				
				msg="Password: "+pwd;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
			}
			
			i++;
		}while(!(xl.read(i, 0).equals("Only valid password")));
	
		// checking invalid email ID, invalid name and valid password
		msg=" checking invalid name, invalid emailID and valid password";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		i++;// to point to next row
		
		do{
			name=xl.read(i, 0);
			emailid=xl.read(i, 1);
			pwd=xl.read(i, 2);
			name_textbox.clear();
			emailid_textbox.clear();
			pwd_textbox.clear();
			
			name_textbox.sendKeys(name);
			emailid_textbox.sendKeys(emailid);
			pwd_textbox.sendKeys(pwd);
			
			// only name error should display
			emailid_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_emailid_errmsg_css));
			name_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_name_errmsg_css));
			
			if(emailid_errmsg.isDisplayed())
			{
			/*	msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(emailid_errmsg.getText()).equals(email_err_text))
				{
					msg="error message mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="EmailID: "+emailid;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+emailid_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			if(name_errmsg.isDisplayed())
			{
			/*	msg="Error msg displayed";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				*/
				if(!(name_errmsg.getText()).equals(name_err_text))
				{
					msg="error message mismatch";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					msg="Name: "+name;
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					msg="message: "+name_errmsg.getText();
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
				}
			}
			else
			{
				msg="Error message is not displayed";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			
			// checking for Register button enability, button should not be enabled
			if(register_btn.isEnabled())
			{
				msg="Register button enabled";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Error",msg, Driver.column);
				write.writeReports("Log","FAIL", Driver.column);
				
				msg="Name: "+name;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
						
				msg="EmailID: "+emailid;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
				
				msg="Password: "+pwd;
				System.out.println(msg);
				write.writeReports("Error",msg,Driver.column);
			}
			
			i++;
		}while(!(xl.read(i, 0).equals("only valid name")));
		
		// checking invalid email ID, valid name and invalid password
				msg="checking invalid email ID, valid name and invalid password";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				i++;// to point to next row
				
				do{
					name=xl.read(i, 0);
					emailid=xl.read(i, 1);
					pwd=xl.read(i, 2);
					name_textbox.clear();
					emailid_textbox.clear();
					pwd_textbox.clear();
					
					name_textbox.sendKeys(name);
					emailid_textbox.sendKeys(emailid);
					pwd_textbox.sendKeys(pwd);
					
					// only name error should display
					emailid_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_emailid_errmsg_css));
					pwd_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_pwd_errmsg_css));
					
					if(emailid_errmsg.isDisplayed())
					{
					/*	msg="Error msg displayed";
						System.out.println(msg);
						write.writeReports("Log", msg, Driver.column);
						*/
						if(!(emailid_errmsg.getText()).equals(email_err_text))
						{
							msg="error message mismatch";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							msg="EmailID: "+emailid;
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							
							msg="message: "+emailid_errmsg.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						msg="Error message is not displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
					}
					
					if(pwd_errmsg.isDisplayed())
					{
					/*	msg="Error msg displayed";
						System.out.println(msg);
						write.writeReports("Log", msg, Driver.column);
						*/
						if(!(pwd_errmsg.getText()).equals(pwd_err_text))
						{
							msg="error message mismatch";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							msg="Passord: "+pwd;
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							
							msg="message: "+name_errmsg.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						msg="Error message is not displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
					}
					
					
					// checking for Register button enability, button should not be enabled
					if(register_btn.isEnabled())
					{
						msg="Register button enabled";
						System.out.println(msg);
						write.writeReports("Log", msg,Driver.column);
						write.writeReports("Error",msg, Driver.column);
						write.writeReports("Log","FAIL", Driver.column);
						
						msg="Name: "+name;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
								
						msg="EmailID: "+emailid;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
						
						msg="Password: "+pwd;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
					}
					
					i++;
				}while(!(xl.read(i, 0).equals("only valid email ID")));
		
				// checking valid email ID, invalid name and invalid password
				msg="checking valid email ID, invalid name and invalid password";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				i++;// to point to next row
				
				do{
					name=xl.read(i, 0);
					emailid=xl.read(i, 1);
					pwd=xl.read(i, 2);
					name_textbox.clear();
					emailid_textbox.clear();
					pwd_textbox.clear();
					
					name_textbox.sendKeys(name);
					emailid_textbox.sendKeys(emailid);
					pwd_textbox.sendKeys(pwd);
					
					// only name error should display
					name_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_name_errmsg_css));
					pwd_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_pwd_errmsg_css));
					
					if(name_errmsg.isDisplayed())
					{
					/*	msg="Error msg displayed";
						System.out.println(msg);
						write.writeReports("Log", msg, Driver.column);
						*/
						if(!(name_errmsg.getText()).equals(name_err_text))
						{
							msg="error message mismatch";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							msg="Name: "+name;
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							
							msg="message: "+name_errmsg.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						msg="Error message is not displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
					}
					
					if(pwd_errmsg.isDisplayed())
					{
					/*	msg="Error msg displayed";
						System.out.println(msg);
						write.writeReports("Log", msg, Driver.column);
						*/
						if(!(pwd_errmsg.getText()).equals(pwd_err_text))
						{
							msg="error message mismatch";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							msg="Passord: "+pwd;
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							
							msg="message: "+name_errmsg.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						msg="Error message is not displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
					}
					
					
					// checking for Register button enability, button should not be enabled
					if(register_btn.isEnabled())
					{
						msg="Register button enabled";
						System.out.println(msg);
						write.writeReports("Log", msg,Driver.column);
						write.writeReports("Error",msg, Driver.column);
						write.writeReports("Log","FAIL", Driver.column);
						
						msg="Name: "+name;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
								
						msg="EmailID: "+emailid;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
						
						msg="Password: "+pwd;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
					}
					
					i++;
				}while(!(xl.read(i, 0).equals("all invalid")));
				
				// checking all invalid
				msg="checking all invalid";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				i++;// to point to next row
				
				do{
					//System.out.println("i value in all invalid: "+i);
					name=xl.read(i, 0);
					//System.out.println("Name: "+name);
					emailid=xl.read(i, 1);
					//System.out.println("emailid: "+emailid);

					pwd=xl.read(i, 2);
					//System.out.println("Password: "+pwd);
					name_textbox.clear();
					emailid_textbox.clear();
					pwd_textbox.clear();
					
					name_textbox.sendKeys(name);

					emailid_textbox.sendKeys(emailid);
					pwd_textbox.sendKeys(pwd);
					
					// only name error should display
					name_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_name_errmsg_css));
					emailid_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_emailid_errmsg_css));
					pwd_errmsg=webdriver.findElement(By.cssSelector(UIobjects.register_pwd_errmsg_css));
					
					if(name_errmsg.isDisplayed())
					{
					/*	msg="Error msg displayed";
						System.out.println(msg);
						write.writeReports("Log", msg, Driver.column);
						*/
						if(!(name_errmsg.getText()).equals(name_err_text))
						{
							msg="error message mismatch";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							msg="Name: "+name;
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							
							msg="message: "+name_errmsg.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						msg="Error message is not displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
					}
					
					if(emailid_errmsg.isDisplayed())
					{
					/*	msg="Error msg displayed";
						System.out.println(msg);
						write.writeReports("Log", msg, Driver.column);
						*/
						if(!(emailid_errmsg.getText()).equals(email_err_text))
						{
							msg="error message mismatch";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							msg="Email ID: "+emailid;
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							
							msg="message: "+emailid_errmsg.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						msg="Error message is not displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
					}
					
					if(pwd_errmsg.isDisplayed())
					{
					/*	msg="Error msg displayed";
						System.out.println(msg);
						write.writeReports("Log", msg, Driver.column);
						*/
						if(!(pwd_errmsg.getText()).equals(pwd_err_text))
						{
							msg="error message mismatch";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							msg="Passord: "+pwd;
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							
							msg="message: "+name_errmsg.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						msg="Error message is not displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
					}
					
					
					// checking for Register button enability, button should not be enabled
					if(register_btn.isEnabled())
					{
						msg="Register button enabled";
						System.out.println(msg);
						write.writeReports("Log", msg,Driver.column);
						write.writeReports("Error",msg, Driver.column);
						write.writeReports("Log","FAIL", Driver.column);
						
						msg="Name: "+name;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
								
						msg="EmailID: "+emailid;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
						
						msg="Password: "+pwd;
						System.out.println(msg);
						write.writeReports("Error",msg,Driver.column);
					}
					
					i++;
				}while(!(xl.read(i, 0).equals("valid data")));		
		
		//Registering with valid data
		 Random rand = new Random();
		 int max=100000,min=1;
		 int randomNum = rand.nextInt((max - min) + 1) + min;
		 //System.out.println("Random number: "+randomNum);
		name="Test Name"; 
		emailid="test"+randomNum+"@mailinator.com";
		pwd="styletag";
		
		msg="New user email id: "+emailid;
		System.out.println(msg);
		write.writeReports("Log", msg, Driver.column);
		
		msg="password: "+pwd;
		System.out.println(msg);
		write.writeReports("Log", msg, Driver.column);
		
		name_textbox.clear();
		emailid_textbox.clear();
		pwd_textbox.clear();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.regiter_name_css)));
		webdriver.findElement(By.cssSelector(UIobjects.regiter_name_css)).sendKeys(name);;
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.register_email_css)));
		webdriver.findElement(By.cssSelector(UIobjects.register_email_css)).sendKeys(emailid);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.register_pwd_css)));
		webdriver.findElement(By.cssSelector(UIobjects.register_pwd_css)).sendKeys(pwd);
		
		//checking for button enability
		if(register_btn.isEnabled())
		{
			msg="Register Button is enabled";
			System.out.println(msg);
			write.writeReports("Log",msg, Driver.column);
			
			register_btn.click();
			
			Thread.sleep(2000);
			
			WebElement login_flash_msg=webdriver.findElement(By.cssSelector(UIobjects.login_flash_msg_css));
			if(login_flash_msg.isDisplayed())
			{
				
				String text=login_flash_msg.getText();
				String[] array=text.split("\n");
				//System.out.println("array value: "+array[0]);
				msg="Flash is displayed ";
				write.writeReports("Log", msg,Driver.column);
				
						
				if((array[0].equals("Successfully logged in")))
				{
					msg="Flash msg is: "+array[0];
					write.writeReports("Log",msg,Driver.column);
					
					WebElement acc_mem_name=webdriver.findElement(By.cssSelector(UIobjects.acc_name_css));
					if(acc_mem_name.isDisplayed())
					{
						msg=acc_mem_name.getText()+" - displayed";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
						Driver.FLAG++;
						write.writeReports("Log", "PASS", Driver.column);
					
					}else
					{
						Driver.FLAG=0;
						msg="account name is not being displayed";
						write.writeReports("Log","FAIL",Driver.column);
						write.writeReports("Error", msg,Driver.column);
					}
					
					Thread.sleep(3000);// this required for the URL to change if in case
					
					String URL1=current_url;
					String URL2=webdriver.getCurrentUrl();
					msg="current page is: "+"'"+URL2+"'";
					System.out.println(msg);
					//write.writeReports("Log",msg, Driver.column);
					
					
					if(URL1.contains("email"))// checking login at checkout/email page
					{
						if(URL2.contains("address")) // should be redirected to ../address page
						{
							msg="Redirected to the correct page ";
							write.writeReports("Log",msg, Driver.column);
						}
						else
						{
							msg="Redirection didnt happened to correct page";
							write.writeReports("Log", msg, Driver.column);
							write.writeReports("Log","FAIL", Driver.column);
							write.writeReports("Error",msg,Driver.column);
							msg="URL before Login: '"+URL1+"'";
							write.writeReports("Error", msg, Driver.column);
							msg="URL after Login: '"+URL2+"'";
							write.writeReports("Error",msg, Driver.column);
						}
						
					}
					else
					{
						if(URL1.equals(URL2))
						{
							msg="Redirected to the correct page ";
							write.writeReports("Log",msg, Driver.column);
						}
						else
						{
							msg="Redirection didnt happened to correct page";
							write.writeReports("Log", msg, Driver.column);
							write.writeReports("Log","FAIL", Driver.column);
							write.writeReports("Error",msg,Driver.column);
							msg="URL before Login: '"+URL1+"'";
							write.writeReports("Error", msg, Driver.column);
							msg="URL after Login: '"+URL2+"'";
							write.writeReports("Error",msg, Driver.column);
						}
					}
					
					
					
				}else
				{
					msg="Flash msg is not equal to 'Successfully logged in'";
					Driver.FLAG=0;
					write.writeReports("Log", msg, Driver.column);
					write.writeReports("Log","FAIL",Driver.column);
					write.writeReports("Error",msg,Driver.column);
					
					msg="Flash msg is: "+array[0];
					System.out.println(msg);
					write.writeReports("Error", msg, Driver.column);
				}
				
			}else
			{
				System.out.println("Flash msg is not displayed");
				
				Driver.FLAG=0;
			}
			
			
			
		}
		else
		{
			msg="Register button is not enabled";
			System.out.println(msg);
			write.writeReports("Log",msg, Driver.column);
			
			write.writeReports("Log", "FAIL", Driver.column);
			Driver.FLAG=0;
		}
	
		
		
		
	} catch (Exception e) {
		
		e.printStackTrace();
		Driver.FLAG=0;
		printException(e);
	}
}
	public void logout() throws InterruptedException{
		try {
			Thread.sleep(3500); // it is required to get rid of flash msg. ex: if logout happen as soon as login flash msg hides the required UI element.
			msg="User logging out ";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);

			wait = new WebDriverWait(webdriver,50);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(UIobjects.acc_mem_name_id)));
			WebElement menu = webdriver.findElement(By.id(UIobjects.acc_mem_name_id));
			new Actions(webdriver).moveToElement(menu).build().perform();
			System.out.println("calling perform function");
			Thread.sleep(2000);
			msg="clicking on logout link";
			System.out.println(msg);
			write.writeReports("Log", msg, Driver.column);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.inner_logout_css)));
			webdriver.findElement(By.cssSelector(UIobjects.inner_logout_css)).click();
			write.writeReports("Log","PASS", Driver.column);
			Thread.sleep(5000);// this is required for the wait till flash messages goes off
			String url=webdriver.getCurrentUrl();
			String url2=Driver.HOME_URL+"/";
			if(url.equals(url2))
			{
				msg="Redirected to HOME page";
				write.writeReports("Log",msg, Driver.column);
				System.out.println(msg);
				write.writeReports("Log", "PASS", Driver.column);
			}
			else
			{
				
				
				msg="Redirection didnot happen to HOME page";
				System.out.println(msg);
				write.writeReports("Log",msg, Driver.column);
				write.writeReports("Log","FAIL", Driver.column);
				write.writeReports("Error",msg, Driver.column);
				
				msg="Home url is: "+url2;
				System.out.println(msg);
				write.writeReports("Error",msg, Driver.column);
				msg="current URL: '"+url+"'";
				write.writeReports("Error", msg, Driver.column);
				System.out.println(msg);
				Driver.FLAG=0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log","FAIL", Driver.column);
			printException(e);
		}
	}
	
public void search()
{	
	try {
		Thread.sleep(3500); // this is required in case of search tab is overlapped with any flash msg ex: in product detail page after adding the product to cart, flash msg overlaps the seacrh tab
		String search_keyword,sort_value;
		int count=1,sort_value_int;
		
		//ExcelRead xl=new ExcelRead(Driver.properties.getProperty("InputDataFile"));
		wait=new WebDriverWait(webdriver, 5);
		int n=xl.rowCountInSheet(2);
		for(int i=1;i<=n;i++)	
		{	
			//System.out.println("i value is: "+i);
			search_keyword=xl.read(i,0);
			
												
			msg="Clicking on search tab";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			webdriver.findElement(By.cssSelector(UIobjects.search_field_css)).click();
			System.out.println("Clearing the field");
			webdriver.findElement(By.cssSelector(UIobjects.search_field_css)).clear();
			
			msg="Entering "+search_keyword+" in search tab";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			webdriver.findElement(By.cssSelector(UIobjects.search_field_css)).sendKeys(search_keyword);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				
				e2.printStackTrace();
			}
			
			//checking for page title
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.search_button_css)));
			webdriver.findElement(By.cssSelector(UIobjects.search_button_css)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.page_title_css)));
			String title = webdriver.findElement(By.cssSelector(UIobjects.page_title_css)).getText();
			msg="Search page title: "+title;
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
								
			//checking for products in the search page
			wait=new WebDriverWait(webdriver,20);
			WebElement product;
			try {
				product = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.product_css)));
				msg="Products are found in the search page";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				
				// checking for URL
				String current_url=webdriver.getCurrentUrl();
				String expected_url= Driver.HOME_URL+"/products?root=search&keywords="+search_keyword;
				if(current_url.equals(expected_url))
				{
					msg="search URL is correct";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					Driver.FLAG++;
					write.writeReports("Log", "PASS", Driver.column);
					System.out.println("PASS");
				}
				else
				{
					msg="Current URL is not equal to Expected URL";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Error",msg,Driver.column);
					
					msg="Current URL:  "+current_url;
					write.writeReports("Error", msg, Driver.column);
					
					System.out.println("FAIL");
					write.writeReports("Log", "FAIL", Driver.column);
				}
								
			} catch (Exception e1) {
				e1.printStackTrace();
				msg="Products are not found in the search page";
				System.out.println(msg);
				write.writeReports("Log", "FAIL", Driver.column);
				write.writeReports("Error", msg,Driver.column);
				printException(e1);
				Driver.FLAG=0;
			}
			
			Thread.sleep(4000);
		}	
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log","FAIL", Driver.column);
			printException(e);
		} 
}
	public void searchSort()
	{	
		try {
			Thread.sleep(3500); // this is required in case of search tab is overlapped with any flash msg ex: in product detail page after adding the product to cart, flash msg overlaps the seacrh tab
			wait= new WebDriverWait(webdriver, 5);
			String search_keyword,sort_value;
			int count=1,sort_value_int;
			//ExcelRead xl=new ExcelRead(Driver.properties.getProperty("InputDataFile"));
			xl.rowCountInSheet(2);
			search_keyword=xl.read(1,0);
									
			msg="Clicking on search tab";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			webdriver.findElement(By.cssSelector(UIobjects.search_field_css)).click();
			System.out.println("Clearing the field");
			webdriver.findElement(By.cssSelector(UIobjects.search_field_css)).clear();
			
			msg="Entering "+search_keyword+" in search tab";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			webdriver.findElement(By.cssSelector(UIobjects.search_field_css)).sendKeys(search_keyword);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e2) {
				
				e2.printStackTrace();
			}
			
			webdriver.findElement(By.cssSelector(UIobjects.search_button_css)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.page_title_css)));
			String title = webdriver.findElement(By.cssSelector(UIobjects.page_title_css)).getText();
			msg="Search page title: "+title;
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			wait=new WebDriverWait(webdriver,20);
			WebElement product;
			try {
				product = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.product_css)));
				msg="Products are found in the search page";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				
				// checking for URL
				String current_url=webdriver.getCurrentUrl();
				String expected_url= Driver.HOME_URL+"/products?root=search&keywords="+search_keyword;
				if(current_url.equals(expected_url))
				{
					msg="search URL is correct";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					
					Driver.FLAG++;
					write.writeReports("Log", "PASS", Driver.column);
					System.out.println("PASS");
				}
				else
				{
					msg="Current URL is not equal to Expected URL";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Error",msg,Driver.column);
					
					msg="Current URL:  "+current_url;
					write.writeReports("Error", msg, Driver.column);
					
					System.out.println("FAIL");
					write.writeReports("Log", "FAIL", Driver.column);
				}
				
				//low_high
				sort(1);
				if(Driver.FLAG!=0)
				{	Driver.FLAG++;
					System.out.println("Driver.FLAG Value inside search function "+Driver.FLAG);
					write.writeReports("Log", "PASS",Driver.column);
				}
				
				try {
					webdriver.findElement(By.cssSelector(".scrollup")).click();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				Thread.sleep(4000);
				sort(2);
				
				try {
					webdriver.findElement(By.cssSelector(".scrollup")).click();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				if(Driver.FLAG!=0)
				{	Driver.FLAG++;
					//System.out.println("Driver.FLAG Value inside search function "+Driver.FLAG);
					write.writeReports("Log", "PASS",Driver.column);
				}
								
			} catch (Exception e1) {
				e1.printStackTrace();
				msg="Products are not found in the search page";
				System.out.println(msg);
				write.writeReports("Log", "FAIL", Driver.column);
				write.writeReports("Error", msg,Driver.column);
				printException(e1);
				Driver.FLAG=0;
			}
			
				//Thread.sleep(4000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log","FAIL", Driver.column);
			printException(e);
		} 
}
	public void sort(int option){
		wait = new WebDriverWait(webdriver,5 );
		if (option==1){
		msg="clicking on Low-High";
		System.out.println("\nclicking on Low-High");
		write.writeReports("Log", msg,Driver.column);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.low_high_css)));
			webdriver.findElement(By.cssSelector(UIobjects.low_high_css)).click();
			
			Thread.sleep(2000);// this is required for slider value to update
			
			compare(option);
			if(Driver.FLAG!=0)
				Driver.FLAG++;
			
			} catch (Exception e) {
			Driver.FLAG=0;
			write.writeReports("Log","FAIL inside sort function ", Driver.column);
			write.writeReports("Error","inside sort function",Driver.column);
			printException(e);
			e.printStackTrace();
			}
				
		}
		if (option == 2){
			msg="\nclicking on High-Low";
			System.out.println("\nclicking on High-Low");
			write.writeReports("Log", msg,Driver.column);
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.high_low_css)));
				webdriver.findElement(By.cssSelector(UIobjects.high_low_css)).click();
				
				Thread.sleep(2000);// this is required for slider value to update
				
				compare(option);
				if(Driver.FLAG!=0)
					Driver.FLAG++;
				
			} catch (Exception e) {
				Driver.FLAG=0;
				write.writeReports("Log","FAIL inside sort function ", Driver.column);
				write.writeReports("Error","inside sort function",Driver.column);
				printException(e);
				e.printStackTrace();
			}
			
		}
		
		
		
	}
	public void compare(int option)
	{   
		//try
		int count1=0;
		System.out.println("inside compare");
		
		try {
			Thread.sleep(9);
			String value=webdriver.findElement(By.cssSelector(UIobjects.slider_value_css)).getText();
			String numberOnly= value.replaceAll("[^0-9]", "");
			count1 = Integer.parseInt(numberOnly);
			System.out.println("slider value is: "+count1);
			write.writeReports("Log","slider value is: "+count1,Driver.column );
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			Driver.FLAG=0;
			msg="coudn't parse/ get slider value";
			write.writeReports("Log",msg,Driver.column);
			write.writeReports("Error", msg,Driver.column);
			printException(e1);
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
			Driver.FLAG=0;
		}
		
		{
			String Sprice1,Sprice2;
			int Iprice1,Iprice2,j=1;
			sort_flag=0;
			wait = new WebDriverWait(webdriver,2);
          for (int i=1;i<count1;i++){
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
				//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#product-container > div.ng-isolate-scope > ul > li:nth-child("+i+"1) > div > div.product-Info > span.product-price > span.product-dmrp")));
				Sprice1= webdriver.findElement(By.cssSelector("#product-container > div.ng-isolate-scope > ul > li:nth-child("+i+") > div > div.product-Info > span.product-price > span.product-dmrp.col-orange.text-capitalize.ng-binding")).getText();
				//System.out.println("i value first "+i);
				i++;
				
				//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#product-container > div > ul > li:nth-child(" +i +") > div > div.product-Info > span.product-price.pull-right > span.col-orange.text-right.text-capitalize.ng-binding")));
				Sprice2=webdriver.findElement(By.cssSelector("#product-container > div.ng-isolate-scope > ul > li:nth-child("+i+") > div > div.product-Info > span.product-price > span.product-dmrp.col-orange.text-capitalize.ng-binding")).getText(); 
				//System.out.println("second i value " +i);
				
				//System.out.println("price1 and price2 before replace all"+Sprice1+Sprice2);
				Sprice1=Sprice1.replaceAll("[^0-9]" ,"");
				Sprice2=Sprice2.replaceAll("[^0-9]", "");
				Iprice1=Integer.parseInt(Sprice1);
			
				Iprice2=Integer.parseInt(Sprice2);
				
				//System.out.println(Iprice2);
				if(option==1){
					if(Iprice1>Iprice2)
					{	System.out.println("inside first if"); // in low_high, first product price is greater than second product
						msg="product"+(i-1)+" price: "+Iprice1+" product"+i+" price: "+Sprice2;
						System.out.println(msg);
						write.writeReports("Log","FAIL",Driver.column);
						write.writeReports("Error", msg,Driver.column);
											
						sort_flag=1;
						break;
					}
				}
				if (option==2 ){
					if(Iprice1<Iprice2)
					{	System.out.println("inside second if");// in high_low, first product price is lesser than second product
					    msg="product"+(i-1)+" price: "+Iprice1+" product"+i+" price: "+Sprice2;
						System.out.println(msg);
						write.writeReports("Log","FAIL",Driver.column);
						write.writeReports("Error", msg,Driver.column);
					    sort_flag=1;
						break;
					}
				}
				JavascriptExecutor jse= (JavascriptExecutor)webdriver;
				jse.executeScript("window.scrollBy(0,150)", "");
				
				if(i>=15) // DOM will load first 25 product in the begining, to make other products visible requires more scroll
				{
					j++;
						try {
							//System.out.println("j value is "+j);
							JavascriptExecutor jse1= (JavascriptExecutor)webdriver;
							jse1.executeScript("window.scrollBy(0,200)", "");
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Driver.FLAG=0;
					}
				}
			
			}
			if(sort_flag==0 && option==1)
			{	
				msg="products are acended_by_master_price";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				Driver.FLAG++;
				
			}
			else if(sort_flag==0 && option==2)
			{
				msg="products are decended_by_master_price";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				Driver.FLAG++;
				
			}
			else if(sort_flag==1)
			{	
				msg="products are not in order";
				System.out.println(msg);
				write.writeReports("Error", msg);
				Driver.FLAG=0;
				File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
				try {
					FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+Driver.properties.getProperty("ScreenSchot")+"product_price_mismatch"+date+".png"));
				} catch (IOException e) {
					e.printStackTrace();
					msg="Error while taking screenschot inside compare function";
					write.writeReports("Error", msg,Driver.column);
					//Driver.FLAG=0;
				}
				
			
			}
		}/* catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Driver.FLAG++;
	}
	
	public void clearCart() {
		int cart_flag=0;
		
		msg="clearing cart";
		System.out.println(msg);
		write.writeReports("Log", msg);
		//Thread.sleep(500);
		
		try {
			wait = new WebDriverWait(webdriver,3);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.minicart_css)));
			WebElement minicart = webdriver.findElement(By.cssSelector(UIobjects.minicart_css));
			
			new Actions(webdriver).moveToElement(minicart).build().perform();
			//Thread.sleep(1500);
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.empty_my_cart_css)));
				WebElement empty = webdriver.findElement(By.cssSelector(UIobjects.empty_my_cart_css));
				if (empty.isDisplayed())
				{	cart_flag=1;
					webdriver.findElement(By.cssSelector(UIobjects.empty_my_cart_css)).click();
					msg="cart cleared ";
					System.out.println(msg);
					write.writeReports("Log", msg);
					Driver.FLAG++;
				}
				//Thread.sleep(5);
			} catch (Exception e) {
				if(cart_flag==1)
				{	msg="'Empty_My_Cart' link is present but_couldn't clear the cart";
					System.out.println(msg);
					write.writeReports("Log", msg);
					write.writeReports("Error", msg);
					Driver.FLAG=0;
				
				}
				else
				{
					msg="cart is already empty ";
					System.out.println(msg);
					write.writeReports("Log", msg);
					Driver.FLAG++;
				}
				//e.printStackTrace();
			}
			
		} catch (Exception e) {
			Driver.FLAG=0;
			System.out.println("clear cart catch block");
			
			e.printStackTrace();
		}
				
	}
	
	public void productDetailPage()
	{
		//System.out.println("Driver.column num inside productCatalogPage "+Driver.column);
		int size_flag=0,size_presence_falg=0;
		String parentBrowser = webdriver.getWindowHandle();// capturing parent tab browser.
		
		msg="selecting the product"+product_num+" in listing page";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		//write.writeReports("Log", "clicking on product", Driver.column);
		
		wait= new WebDriverWait(webdriver,20);
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#product-container > div.ng-isolate-scope > ul > li:nth-child("+product_num+") > div > div.product-image > a > img")));
			//System.out.println("css of the selected product is "+"#product-container > div.ng-isolate-scope > ul > li:nth-child("+product_num+") > div > div.product-image > a > img");
			webdriver.findElement(By.cssSelector("#product-container > div.ng-isolate-scope > ul > li:nth-child("+product_num+") > div > div.product-image > a > img")).click();
			msg="clicked on product";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			//get list of all tab browser
			Set<String> allBrowser = webdriver.getWindowHandles();
			
			int n= allBrowser.size();
			
			System.out.println("the size of all Browser array is "+n);
			
			
			System.out.println("names of Browsers\n");
			String lastBrowser=null;
			for(String eachBrowser:allBrowser)
			{
				System.out.println(eachBrowser);
				lastBrowser=eachBrowser;
			}
			System.out.println("lastBrowser is: "+lastBrowser);
			webdriver.switchTo().window(lastBrowser);
			/*
			int j=0;
			System.out.println("switching browser");
			for (String eachBrower:allBrowser){
				//j++;
				//System.out.println("Indside eachBrowser i value is "+j);
				//System.out.println(eachBrower);
					if(!(eachBrower.equals(parentBrowser)))
					{
					//switching to child browser
						webdriver.switchTo().window(eachBrower);
						msg="moved to product detail page";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
						break;
					}
				}
			*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.product_title_css)));
			pd_product_name = webdriver.findElement(By.cssSelector(UIobjects.product_title_css)).getText().toLowerCase();//product Name
			msg="Product Name: "+pd_product_name;
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			msg="checking for size chart";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			// webdriver.findElement(By.cssSelector("#cartform > div > p.view-sizechart > a:nth-child(2)")).click();// clicking on size chart
			 //webdriver.findElement(By.cssSelector("#ngdialog4 > div.ngdialog-content > div")).click();// closing size chart
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.option_css)));
				WebElement options= webdriver.findElement(By.cssSelector(UIobjects.option_css));
				
				msg="Variants Exist and selecting the size";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				
				size_presence_falg=1;
				for(int i=0;i<=7;i++)
				{    
					try{
						//Thread.sleep(1000);
						WebElement size =webdriver.findElement(By.cssSelector(".in-stock:nth-child("+i +") div"));
						if (size.isEnabled())
						{
							size.click();
							msg="selected size: "+size.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							size_flag=1;
							break;
						}
						
				
						}
					catch (Exception e){
						
						msg="size " +i+" is not available";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
						
						}
				}				
				
			} catch (Exception e1) {
				e1.printStackTrace();
				msg="Product with no Variants type";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			msg="ckecking on ADD_TO_CART button enability";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			
			WebElement add_to_cart_button=webdriver.findElement(By.cssSelector("#add-to-cart-button"));
			if(add_to_cart_button.isEnabled())
			{
				msg="ADD_TO_CART button is enabled";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
							
				add_to_cart_button.click();
				Thread.sleep(2000);
				PD_product_name[product_num-1]=new String(pd_product_name); // adding the product name to the array
				WebElement flash_msg= webdriver.findElement(By.cssSelector(UIobjects.flash_msg_css));
				if (flash_msg.isDisplayed())
				{	msg="Product added to cart ,'Added to Cart' msg displayed";
					System.out.println(msg);
					write.writeReports("Log", msg,Driver.column);
					
				}
				Driver.FLAG++;
				write.writeReports("Log","PASS",Driver.column);
				product_num++;  // incrementing the product_num to point to next product int he listing page if this function is called again;
				System.out.println("product_num value "+product_num);
			}
			else
			{
				msg="ADD_TO_CART button is disabled";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				
				 
				if((size_presence_falg==1)&&(size_flag==0)) // size chart present and not selected
				{
					msg="size has not selected";
					System.out.println(msg);
					write.writeReports("Error",msg,Driver.column);
					write.writeReports("Log", "FAIL", Driver.column);
					Driver.FLAG=0;
					
				}
				else
				{
					PD_product_name[product_num-1]=new String(pd_product_name); // adding the product name to the array
					msg="Product is already added to cart";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Log", "PASS",Driver.column);
					Driver.FLAG++;
					product_num++; // incrementing the product_num to point to next product in the listing page if this function is called again;
					System.out.println("product_num value "+product_num);
				}
				
			}
			
			
			System.out.println("Products names in PD page");
			for(int j=0;j<product_num;j++)
			{
				System.out.println(PD_product_name[j]);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			write.writeReports("Log", "FAIL", Driver.column);
			write.writeReports("Error", "Following is the stack trace",Driver.column);
			printException(e);// this to write exception to Error file.
			Driver.FLAG=0;
			
		}finally{ 
			//screenshot is taken in case of pass or fail of the testcase
			File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
			try {
				
				String path=System.getProperty("user.dir")+Driver.properties.getProperty("ScreenSchot")+"ProductDetailPage"+date+".png";
				FileUtils.copyFile(scrFile, new File(path));
				msg="Screenshot taken";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
	public void productDetailPageBuyNow()
	{
		//System.out.println("Driver.column num inside productCatalogPage "+Driver.column);
		int size_flag=0,size_presence_falg=0;
		String parentBrowser = webdriver.getWindowHandle();// capturing parent tab browser.
		
		msg="selecting the product"+product_num+" in listing page";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		//write.writeReports("Log", "clicking on product", Driver.column);
		
		wait= new WebDriverWait(webdriver,20);
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#product-container > div.ng-isolate-scope > ul > li:nth-child("+product_num+") > div > div.product-image > a > img")));
			//System.out.println("css of the selected product is "+"#product-container > div.ng-isolate-scope > ul > li:nth-child("+product_num+") > div > div.product-image > a > img");
			webdriver.findElement(By.cssSelector("#product-container > div.ng-isolate-scope > ul > li:nth-child("+product_num+") > div > div.product-image > a > img")).click();
			msg="clicked on product";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			//get list of all tab browser
			Set<String> allBrowser = webdriver.getWindowHandles();
			
			int n= allBrowser.size();
			
			System.out.println("the size of all Browser array is "+n);
			
			
			System.out.println("names of Browsers\n");
			String lastBrowser=null;
			for(String eachBrowser:allBrowser)
			{
				System.out.println(eachBrowser);
				lastBrowser=eachBrowser;
			}
			System.out.println("lastBrowser is: "+lastBrowser);
			webdriver.switchTo().window(lastBrowser);
			/*
			int j=0;
			System.out.println("switching browser");
			for (String eachBrower:allBrowser){
				//j++;
				//System.out.println("Indside eachBrowser i value is "+j);
				//System.out.println(eachBrower);
					if(!(eachBrower.equals(parentBrowser)))
					{
					//switching to child browser
						webdriver.switchTo().window(eachBrower);
						msg="moved to product detail page";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
						break;
					}
				}
			*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.product_title_css)));
			pd_product_name = webdriver.findElement(By.cssSelector(UIobjects.product_title_css)).getText().toLowerCase();//product Name
			msg="Product Name: "+pd_product_name;
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			msg="checking for size chart";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			// webdriver.findElement(By.cssSelector("#cartform > div > p.view-sizechart > a:nth-child(2)")).click();// clicking on size chart
			 //webdriver.findElement(By.cssSelector("#ngdialog4 > div.ngdialog-content > div")).click();// closing size chart
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.option_css)));
				WebElement options= webdriver.findElement(By.cssSelector(UIobjects.option_css));
				
				msg="Variants Exist and selecting the size";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				
				size_presence_falg=1;
				for(int i=0;i<=7;i++)
				{    
					try{
						//Thread.sleep(1000);
						WebElement size =webdriver.findElement(By.cssSelector(".in-stock:nth-child("+i +") div"));
						if (size.isEnabled())
						{
							size.click();
							msg="selected size: "+size.getText();
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							size_flag=1;
							break;
						}
						
				
						}
					catch (Exception e){
						
						msg="size " +i+" is not available";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
						
						}
				}				
				
			} catch (Exception e1) {
				e1.printStackTrace();
				msg="Product with no Variants type";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			msg="ckecking on BUY_NOW_BUTTON enability";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			
			WebElement buy_now_btn=webdriver.findElement(By.cssSelector(UIobjects.buy_now_btn_css));
			if(buy_now_btn.isEnabled())
			{
				msg="'BUY NOW' button is enabled";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
							
				buy_now_btn.click();
				Thread.sleep(2000);
				PD_product_name[product_num-1]=new String(pd_product_name); // adding the product name to the array
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cart-container > p")));// wait till shoping cart appears
				
				//checking for redirection
				String current_url=webdriver.getCurrentUrl();
				String expected_url=Driver.HOME_URL+"/cart";
				if(current_url.equals(expected_url))
				{
					msg="redirected to CART page";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					Driver.FLAG++;
					write.writeReports("Log","PASS",Driver.column);
					
				}
				
				else{
					msg="FAIL: Re-directed to - "+current_url;
					System.out.println(msg);
					write.writeReports("Log", msg, Driver.column);
					Driver.FLAG=0;
				}
								
				product_num++;  // incrementing the product_num to point to next product int he listing page if this function is called again;
				System.out.println("product_num value "+product_num);
			}
			else
			{
				msg="BUY_NOW button is disabled";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				
				 
				if((size_presence_falg==1)&&(size_flag==0)) // size chart present and not selected
				{
					msg="size has not selected";
					System.out.println(msg);
					write.writeReports("Error",msg,Driver.column);
					write.writeReports("Log", "FAIL", Driver.column);
					Driver.FLAG=0;
					
				}
				else
				{
					PD_product_name[product_num-1]=new String(pd_product_name); // adding the product name to the array
					msg="Product is already added to cart";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Log", "PASS",Driver.column);
					Driver.FLAG++;
					product_num++; // incrementing the product_num to point to next product in the listing page if this function is called again;
					System.out.println("product_num value "+product_num);
				}
				
			}
			
			
			System.out.println("Products names in PD page");
			for(int j=0;j<product_num;j++)
			{
				System.out.println(PD_product_name[j]);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			write.writeReports("Log", "FAIL", Driver.column);
			write.writeReports("Error", "Following is the stack trace",Driver.column);
			printException(e);// this to write exception to Error file.
			Driver.FLAG=0;
			
		}finally{ 
			//screenshot is taken in case of pass or fail of the testcase
			File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
			try {
				
				String path=System.getProperty("user.dir")+Driver.properties.getProperty("ScreenSchot")+"ProductDetailPage"+date+".png";
				FileUtils.copyFile(scrFile, new File(path));
				msg="Screenshot taken";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	
	
	
	public void productCatalogPage(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		try {
				//System.out.println("Driver.column num inside productCatalogPage "+Driver.column);
			msg = "clicking on c1";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
					
			WebElement ethnicwear=	webdriver.findElement(By.id(UIobjects.ethnicwear_id));
			ethnicwear.click();
			//Thread.sleep(1000);
			act=new Actions(webdriver);
			act.moveToElement(ethnicwear).build().perform();
			
			msg="clicking on c2 or c3";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			wait =new  WebDriverWait(webdriver,60);
			
			//kurta_kurtis
		/*	msg="clicking on Kuta_kutis";
			System.out.println(msg);
			write.writeReports("Log", msg);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.kurta_kurti_css)));
			webdriver.findElement(By.cssSelector(UIobjects.kurta_kurti_css)).click();
			*///waitForSpinner();
			
			//anarkalis
			msg="clicking on Anarkalis";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.anarkali_new)));
			webdriver.findElement(By.cssSelector(UIobjects.anarkali_new)).click();
			
			
			//shoes
		/*	act=new Actions(webdriver);
			act.moveToElement(webdriver.findElement(By.id("shoes"))).build().perform();
			msg="clicking on shoes > boots";
			System.out.println(msg);
			write.writeReports("Log", msg, Driver.column);
			Thread.sleep(300);
			webdriver.findElement(By.cssSelector("#non-footer > navbar > header > div.grid-container > nav > div > ul > li:nth-child(3) > ul > li > ul > li > ul > li > ul > li:nth-child(1) > ul > li:nth-child(1) > a.col-dark-grey.c3_li_text.ng-binding")).click();
			*/
			
			
			
			//kurthique
		/*	WebElement kurtique= webdriver.findElement(By.cssSelector("#kurtique-kurtas-kurtis"));
			msg="clicking on kurique";
			System.out.println(msg);
			write.writeReports("Log", msg, Driver.column);
			kurtique.click();
			act=new Actions(webdriver);
			act.moveToElement(kurtique).build().perform();
			msg="moving on kutique";
			write.writeReports("Log", msg,Driver.column);
			System.out.println(msg);
			msg="moving on 'shop the botique > short'";
			write.writeReports("Log", msg, Driver.column);
			*/
			//webdriver.findElement(By.cssSelector("#non-footer > navbar > header > div.grid-container > nav > div > ul > li:nth-child(9) > ul > li > ul > li > ul > li.c2_items_wrap.ng-scope.c2_items_wrap_active > ul > li:nth-child(1) > ul > li:nth-child(1) > a.col-dark-grey.c3_li_text.ng-binding")).click();
			
			//this is to overcome the menu bars drop down
			
			System.out.println("scrolling down");
			JavascriptExecutor js = (JavascriptExecutor)webdriver;
			js.executeScript("window.scrollBy(0,100)","");
			Thread.sleep(1000);
			
			Driver.FLAG++;
			write.writeReports("Log", "PASS",Driver.column);
		} catch (Exception e) {
			Driver.FLAG=0;
			write.writeReports("Log", "FAIL",Driver.column);
			printException(e);
			e.printStackTrace();
		}
		
	}
	
	public void applyFilters()
	{  
		int length=1;
		String s1[]=null;
		// moving cursor to some filter type, to come out of drop down main menu- filter type - color
		new Actions(webdriver).moveToElement(webdriver.findElement(By.cssSelector(UIobjects.color_css))).build().perform();
			
		wait= new WebDriverWait(webdriver, 20);
		for(int j=2;j<=7;j++)// j=2 :starting from discount filters . 7 different filter types.
		{
			if(j==2||j==7)// j=2 - Discount and j=7 - Delivery type filters which are in collapsed mode
			{
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#shared-filter > div > div.sidebar > form > div:nth-child("+j+") > div.filter-box-heading.text-uppercase.text-bold > h2 > a")));
				webdriver.findElement(By.cssSelector("#shared-filter > div > div.sidebar > form > div:nth-child("+j+") > div.filter-box-heading.text-uppercase.text-bold > h2 > a")).click();
			}
			WebElement filtertype = webdriver.findElement(By.cssSelector("#shared-filter > div > div.sidebar > form > div:nth-child("+j+") > div.filter-box-heading.text-uppercase.text-bold > h2 > a"));
			System.out.println("\nFILTER TYPE: "+filtertype.getText().toLowerCase());
			String lowercase=filtertype.getText().toLowerCase().replaceAll("[^a-z]","_");//converting the filter attribute text to lower
			
			List<WebElement> filterattribute = webdriver.findElements(By.id(lowercase));	
			for(WebElement we :filterattribute )
			{
				String s=we.getText();
				System.out.println("FILTER ATTRIBUTES are: \n"+s);
				write.writeReports("Log", s);
				s1 = s.split("\\n");
				msg = "No of attributes in the selected Filter type is "+length;
				write.writeReports("Log", msg);
				length=s1.length; System.out.println(msg);
				
			}
			for(int i=1;i<=length;i++ ) // iterating filter attribute 
			{  
				msg="\nclicking on: "+s1[i-1];
				System.out.println(msg);
				write.writeReports("Log", msg);
				System.out.println("Filter attribute count value "+i);
				
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#"+lowercase+" > span:nth-child("+i+") > label")));
				webdriver.findElement(By.cssSelector("#"+lowercase+" > span:nth-child("+i+") > label")).click();
				//waitForSpinner();
				try {
					//System.out.println("thread.sleep AFTER filter click");
					Thread.sleep(3000);
					sort(1);//low_high
					try {
						webdriver.findElement(By.cssSelector(".scrollup")).click();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					sort(2);//high_low
					try {
						webdriver.findElement(By.cssSelector(".scrollup")).click();
					} catch (Exception e) {
						
						e.printStackTrace();
					} 
					if(Driver.FLAG!=0)
					{
						Driver.FLAG++;
						write.writeReports("Log","PASS",Driver.column);
					}
					else
					{
						write.writeReports("Log","FAIL",Driver.column);
						msg="Filter "+s1[i-1]+" - FAIL";
						write.writeReports("Error",msg,Driver.column);
					}
				
					Thread.sleep(3000);
					wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#"+lowercase+" > span:nth-child("+i+") > label")));
					webdriver.findElement(By.cssSelector("#"+lowercase+" > span:nth-child("+i+") > label")).click();
					Thread.sleep(3000);
					} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
				 //System.out.println("i value after increment"+i);
				
			}
			//to minimize the previously selected filter
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#shared-filter > div > div.sidebar > form > div:nth-child("+j+") > div.filter-box-heading.text-uppercase.text-bold > h2 > a")));
			webdriver.findElement(By.cssSelector("#shared-filter > div > div.sidebar > form > div:nth-child("+j+") > div.filter-box-heading.text-uppercase.text-bold > h2 > a")).click();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				
			}
		}
	}
	public void cartCheck() {
		
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			
			System.out.println("Checking cart");
			webdriver.findElement(By.cssSelector(UIobjects.minicart_css)).click();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			
			
			List<WebElement> order_table_items=webdriver.findElements(By.cssSelector(UIobjects.order_table_items_css));
			int order_table_items_count=order_table_items.size();
			
			msg="total no of line items in the cart is: "+order_table_items_count;
			write.writeReports("Log", msg, Driver.column);
			System.out.println(msg);
			
			//cartitems=new CartItem[order_table_items_count];// declaring the size of cartitems array
			CartItem item;
			System.out.println("order_table_items_count "+order_table_items_count);
			int i=1;
			for(WebElement eachElement:order_table_items)
			{	//cartitems[i-1]=new CartItem();// creating the cart ieam object
				System.out.println("inside for loop");
				item=new CartItem();
			
				String name=eachElement.findElement(By.cssSelector("#cart_product_"+i+" > div:nth-child(2) > p:nth-child(1) > a")).getText();//div:nth-child(2)- second column in the line-item row ie name of the product
				System.out.println("#cart_product_"+i+" Name is "+name);
				//cartitems[i-1].setName(name);
				item.setName(name);
				
				String size_s=eachElement.findElement(By.cssSelector("#cart_product_"+i+"> div:nth-child(2) > p.ng-binding")).getText();
				System.out.println("size"+size_s);
				item.setSize(size_s);
				
				//String quantity=eachElement.findElement(By.cssSelector(selector))
				Select se=new Select(webdriver.findElement(By.cssSelector("#cart_product_"+i+" > div:nth-child(3) > label > select")));
				WebElement option = se.getFirstSelectedOption();
				String qty_s=option.getText();
				int qty_i=Integer.parseInt(qty_s);
				System.out.println("quantity of product"+i+"is: "+qty_i);
				item.setQuantity(qty_i);
				
				String price_s=eachElement.findElement(By.cssSelector("#cart_product_"+i+" > div:nth-child(4) > p:nth-child(1) > span")).getText();
				price_s=price_s.replaceAll("[^0-9]","");
				int price_i=Integer.parseInt(price_s);
				price_i=price_i/100;
				System.out.println("Price is: "+price_i);
				//cartitems[i-1].setPrice(price_i);
				item.setPrice(price_i);
				
				
				String shipping_s=eachElement.findElement(By.cssSelector("#cart_product_"+i+" > div:nth-child(5) > p > span")).getText();
				shipping_s=shipping_s.replaceAll("[^0-9]","");
				int shipping_i= Integer.parseInt(shipping_s);
				shipping_i=shipping_i/100;
				System.out.println("Shipping charges: "+shipping_i);
				//cartitems[i-1].setShipping(shipping_i);
				item.setShipping(shipping_i);
				
				/*String total_s = eachElement.findElement(By.cssSelector("#cart_product_"+i+" > div:nth-child(6) > p")).getText();
				total_s=total_s.replaceAll("[^0-9]", "");
				int total_i=Integer.parseInt(total_s);
				total_i=total_i/100;
				System.out.println("Total: "+total_i);
				
				if(total_i==(price_i+shipping_i))
				{
					
				}*/
				
				cart_item_list.add(item);
				
				//System.out.println("shipping charges from cartitems: "+cartitems[i-1].getShipping());
				
				i++;
			}
			/*System.out.println("\n values from cartitems object");
			for(i=0;i<order_table_items_count;i++)
			{
				System.out.println("line item"+(i+1)+" Name: "+cartitems[i].getName()+", price: "+cartitems[i].getPrice()+", shipping: "+cartitems[i].getShipping());
				
			}*/
			System.out.println("cart_item_list size is "+cart_item_list.size());
			System.out.println("\nvalues from cartitems List");
			msg="Products added to cart are:";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			for(i=0;i<cart_item_list.size();i++)
			{
				System.out.println("i value is: "+i);
				msg="Product Name: "+(cart_item_list.get(i)).getName();
				write.writeReports("Log", msg, Driver.column);
				System.out.println(msg);
			}
			// comparing the names of product whether products all produtcs added from PD is present in cart
			System.out.println("\nprinting products in PD_product_name array");
			String name1=null,name2=null;
			int flag_added_item=0,j;
			for( i=0;i<order_table_items_count;i++)
			{
				System.out.println("i value is: "+i);
				flag_added_item=0;
				System.out.println("name in PD array "+PD_product_name[i].toLowerCase());
				name1=PD_product_name[i].toLowerCase().toLowerCase();
							
				for( j=0;i<order_table_items_count;i++)
				{
					System.out.println("j value is: "+j);
					name2=(cart_item_list.get(j)).getName().toLowerCase();
					if((name1.equals(name2))) // this happen when products name mismatch
					{
						flag_added_item=1; 
						msg=name1+" is added to cart";
						System.out.println(msg);
						write.writeReports("Log", msg,Driver.column);
						break;
					}
				}
				if((flag_added_item==0)&&(j>=order_table_items_count))
				{
					msg=name1+" is not present in the cart";
					System.out.println(msg);
					write.writeReports("Log", msg,Driver.column);
					write.writeReports("Log", "FAIL", Driver.column);
					Driver.FLAG=0;
				}
			}
				
			if(flag_added_item==0)
			{
				write.writeReports("Log","PASS",Driver.column);
				Driver.FLAG++;
			}
		/*	else
			{
				msg="product is not added to cart";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Log", "FAIL",Driver.column);
				msg="item level "+(i+1);
				write.writeReports("Error",msg,Driver.column);
				msg="cart_item name:"+name1+" Product detail item name: "+name2;
				write.writeReports("Error", msg,Driver.column);
				Driver.FLAG=0;
			}*/
			
			
			
			try{
			/*	ct_product_name= webdriver.findElement(By.cssSelector(UIobjects.cartProduct1_css)).getText().toLowerCase();
			if(pd_product_name.equals(ct_product_name))
			{
				msg="product is added to cart";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Log","PASS",Driver.column);
				Driver.FLAG++;
			}
			else
			{
				msg="product is not added to cart";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Log", "FAIL",Driver.column);
				Driver.FLAG=0;
			}*/
		} catch (Exception e) {// control comes here if it couldn't find ct_product_name
			
			e.printStackTrace();
			e.printStackTrace();
			write.writeReports("Log","FAIL",Driver.column);
			printException(e);
			Driver.FLAG=0;
		}
		
		
	}	
	public void checkoutLogin()
	{
		try {
			wait=new WebDriverWait(webdriver, 50);
			System.out.println("clicking on mini cart to proceed to check out");
			msg="clicking on mini cart to proceed to check out";
			write.writeReports("Log", msg,Driver.column);
			//System.out.println("proceed to check out");
			webdriver.findElement(By.cssSelector(UIobjects.minicart_css)).click();
			
			System.out.println();
			msg="clicking on 'proceed to checkout button";
			write.writeReports("Log", msg,Driver.column);
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.proceed_to_checkout_button_css)));
			webdriver.findElement(By.cssSelector(UIobjects.proceed_to_checkout_button_css)).click();
			
			wait.until(ExpectedConditions.visibilityOf(webdriver.findElement(By.cssSelector("#checkout-login_email"))));
			WebElement emailid_textbox=webdriver.findElement(By.cssSelector("#checkout-login_email"));
			WebElement pwd_textbox=webdriver.findElement(By.cssSelector("#checkout-login_password"));
			WebElement login_button=webdriver.findElement(By.cssSelector("#continue-login-btn"));
			
			String  URL1 = webdriver.getCurrentUrl();
			msg="current page is: "+"'"+URL1+"'";
			System.out.println(msg);
			
			
			// for INVALID DATA
			// Login Button should not be enabled for these data pattern
			msg="checking Login Button disability";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			int i=2; String emailid,pwd;
			while(!(xl.read(i,0)).equals("LoginButton enabled"))
			{	
				System.out.println("Inside first while loop");
				emailid=xl.read(i,0);
				pwd=xl.read(i, 1);
				System.out.println(i);
				System.out.println("\nemailid: "+emailid+"  password: "+pwd);
				
				//Clear the text boxes;
				emailid_textbox.clear();
				pwd_textbox.clear();
				
				//enter data 
				emailid_textbox.sendKeys(emailid);
				pwd_textbox.sendKeys(pwd);
				
				Thread.sleep(500);
				
				if((login_button.isEnabled()))// FAIL if login button is enabled
				{
					System.out.println("i value is "+i+i);
					msg="Login button enabled for following input";
					System.out.println(msg);
									
					Driver.FLAG=0;
					write.writeReports("Log","FAIL",Driver.column);
					write.writeReports("Error",msg,Driver.column);
					
					msg="emailid: "+emailid+" pwd: "+pwd+"- fail";
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Error", msg, Driver.column);
					System.out.println(msg);
										
				}
				if(!(login_button.isEnabled()))
				{
					System.out.println("Login Button is not enabled");
					msg="emaiid:"+emailid+" pwd:"+pwd+" - pass";
					write.writeReports("Log",msg,Driver.column);
					
				}
				i++;
				
			}
						
			i++; // to point to next row
			
			// Login Button will be enabled but login should not be successful
			// this is for checking non valid data ie compared with DB
			msg="Checking for Unsuccessful Login";
			write.writeReports("Log",msg,Driver.column);
			System.out.println(msg);
			while(!(xl.read(i,0)).equals("Valid data"))
			{	
				
				System.out.println("Inside second while");
				emailid=xl.read(i,0);
				pwd=xl.read(i, 1);
				System.out.println(i);
				System.out.println("\nemailid:  "+emailid+" password: "+pwd);
				
				//Clear the text boxes;
				emailid_textbox.clear();
				pwd_textbox.clear();
				
				//enter data 
				emailid_textbox.sendKeys(emailid);
				pwd_textbox.sendKeys(pwd);

				
				if(login_button.isEnabled())
				{
					System.out.println("\nLogin Button enabled and clicking on the button");
					login_button.click();
					Thread.sleep(2000);
					WebElement login_flash_msg=webdriver.findElement(By.cssSelector(UIobjects.login_flash_msg_css));
					if(login_flash_msg.isDisplayed())
					{	
						String text=login_flash_msg.getText();
						String[] flash_msg_array=text.split("\n");
					System.out.println("array value:"+flash_msg_array[0]+"end");
					//System.out.println(text);
						int flag=0;
						if(!(flash_msg_array[0].equals("Sorry! Invalid email/password combination. Please try again.")))// Fail if() condition is true
						{
							System.out.println("inside if");
						Driver.FLAG=0;
						write.writeReports("Log","FAIL",Driver.column);
						msg="emailid: "+emailid+" pwd: "+pwd;
						write.writeReports("Error", msg,Driver.column);
						System.out.println(msg);
						
						msg="Flash msg is: "+flash_msg_array[0];
						write.writeReports("Error",msg,Driver.column);
						System.out.println(msg);
						flag=1;
						}
						if (flag!=1) // only to write pass to Log
						{
							msg="emailid: "+emailid+" pwd: "+pwd+" - pass";
							write.writeReports("Log",msg,Driver.column);
						}
					}
					else
					{
						System.out.println("inside else");
						Driver.FLAG=0;
						write.writeReports("Log","FAIL",Driver.column);
						msg="emailid: "+emailid+" pwd: "+pwd+" - fail";
						write.writeReports("Error", msg,Driver.column);
						System.out.println(msg);
						
						msg="Flash msg 'Sorry! Invalid email/password combination. Please try again' is not displayed ";
						write.writeReports("Error",msg,Driver.column);
						System.out.println(msg);
						
					}
					
				
				}i++;
			}
			
			i++; // to point to next row
			
			// Checking for VALID DATA
			//Login should be successful
			msg="checking for Successful Login";
			write.writeReports("Log",msg,Driver.column);
			System.out.println(msg);
			emailid=xl.read(i,0);
			pwd=xl.read(i, 1);
			
			//Clear the text boxes;
			emailid_textbox.clear();
			pwd_textbox.clear();
			
			//enter data 
			emailid_textbox.sendKeys(emailid);
			pwd_textbox.sendKeys(pwd);
			
			if(login_button.isEnabled())
			{
				msg="emailid: "+emailid+" pwd"+pwd+" valid data";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				System.out.println("\nLogin Button enabled and clicking on the button");
				login_button.click();
				Thread.sleep(2000);
				
				WebElement login_flash_msg=webdriver.findElement(By.cssSelector(UIobjects.login_flash_msg_css));
				if(login_flash_msg.isDisplayed())
				{
					
					String text=login_flash_msg.getText();
					String[] array=text.split("\n");
					//System.out.println("array value: "+array[0]);
					msg="Flash is displayed ";
					write.writeReports("Log", msg,Driver.column);
					
							
					if((array[0].equals("Successfully logged in")))
					{
						msg="Flash msg is: "+array[0];
						write.writeReports("Log",msg,Driver.column);
						
						WebElement acc_mem_name=webdriver.findElement(By.cssSelector(UIobjects.acc_name_css));
						if(acc_mem_name.isDisplayed())
						{
							msg=acc_mem_name.getText()+" - displayed";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							Driver.FLAG++;
							write.writeReports("Log", "PASS", Driver.column);
						
						}else
						{
							Driver.FLAG=0;
							msg="account name is not being displayed";
							write.writeReports("Log","FAIL",Driver.column);
							write.writeReports("Error", msg,Driver.column);
						}
						
						Thread.sleep(3000);// this required for the URL to change if in case
						
						String URL2=webdriver.getCurrentUrl();
						msg="current page is: "+"'"+URL2+"'";
						System.out.println(msg);
						//write.writeReports("Log",msg, Driver.column);
						
						
						if(URL1.contains("email"))// checking login at checkout/email page
						{
							if(URL2.contains("address")) // should be redirected to ../address page
							{
								msg="Redirected to the correct page ";
								write.writeReports("Log",msg, Driver.column);
							}
							else
							{
								msg="Redirection didnt happened to correct page";
								write.writeReports("Log", msg, Driver.column);
								write.writeReports("Log","FAIL", Driver.column);
								write.writeReports("Error",msg,Driver.column);
								msg="URL before Login: '"+URL1+"'";
								write.writeReports("Error", msg, Driver.column);
								msg="URL after Login: '"+URL2+"'";
								write.writeReports("Error",msg, Driver.column);
							}
							
						}
						else
						{
							if(URL1.equals(URL2))
							{
								msg="Redirected to the correct page ";
								write.writeReports("Log",msg, Driver.column);
							}
							else
							{
								msg="Redirection didnt happened to correct page";
								write.writeReports("Log", msg, Driver.column);
								write.writeReports("Log","FAIL", Driver.column);
								write.writeReports("Error",msg,Driver.column);
								msg="URL before Login: '"+URL1+"'";
								write.writeReports("Error", msg, Driver.column);
								msg="URL after Login: '"+URL2+"'";
								write.writeReports("Error",msg, Driver.column);
							}
						}
						
						
						
					}else
					{
						msg="Flash msg is not equal to 'Successfully logged in'";
						Driver.FLAG=0;
						write.writeReports("Log","FAIL",Driver.column);
						write.writeReports("Error",msg,Driver.column);
					}
					
				}else
				{
					System.out.println("Flash msg is not displayed");
					
					Driver.FLAG=0;
				}
				
			}else
			{
				msg="Login Button is not enabled";
				Driver.FLAG=0;
				write.writeReports("Log", "FAIL",Driver.column);
				write.writeReports("Error",msg,Driver.column);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log","FAIL", Driver.column);
			printException(e);
		}
		
	
		
		
	}
	
	public void checkout() {
		try
		{
			
			wait=new WebDriverWait(webdriver, 50);
			System.out.println("clicking on mini cart to proceed to check out");
			msg="clicking on mini cart to proceed to check out";
			write.writeReports("Log", msg,Driver.column);
			//System.out.println("proceed to check out");
			webdriver.findElement(By.cssSelector(UIobjects.minicart_css)).click();
			
			System.out.println();
			msg="clicking on 'proceed to checkout button";
			write.writeReports("Log", msg,Driver.column);
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.proceed_to_checkout_button_css)));
			webdriver.findElement(By.cssSelector(UIobjects.proceed_to_checkout_button_css)).click();
			
			// check for the the URL
			String current_URL = webdriver.getCurrentUrl();
			String expected_URL = Driver.HOME_URL+"/checkout/address";
			System.out.println("current URL: "+current_URL);
			System.out.println("epected URL: "+expected_URL);
			if (expected_URL.equals(current_URL))
			{
				msg="Redirected to checkout/address page";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
			}
			else
			{
				msg="FAIL : Redirection didnt happen to checkout/address page";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				msg="Current URL: "+current_URL;
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
			}
			
			// checking for checkout/email page
			//Thread.sleep(4000);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.checkout_email_btn_css)));
			msg="chcking EmailID of the user";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			webdriver.findElement(By.cssSelector(UIobjects.checkout_email_btn_css)).click();
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.user_loggedin_emailid)));
			String user_logged_in_email=webdriver.findElement(By.cssSelector(UIobjects.user_loggedin_emailid)).getText();
			if((user_logged_in_email.equals("")))
			{				
				System.out.println("Error!! user Id is not displayed");
				msg="Error!! user ID is not displayed";
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Log","FAIL",Driver.column);
				write.writeReports("Error", msg,Driver.column);
				
				Driver.FLAG=0;
				
			}
			else
			{	
				msg="User logged in as:  "+user_logged_in_email;
				System.out.println();
				write.writeReports("Log",msg,Driver.column);
				
			}
			
			
			System.out.println("clicking on continue button");
			msg="clicking on continue button";
			write.writeReports("Log",msg,Driver.column);
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.continue_email_css)));
			webdriver.findElement(By.cssSelector(UIobjects.continue_email_css)).click();
			
			
			// chekout/address page
			msg="selecting address";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > label:nth-child(2) > p > input[type="+"radio"+"]")));
			WebElement default_add=webdriver.findElement(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > label:nth-child(2) > p > input[type="+"radio"+"]"));
			if(default_add.isSelected())
			{
				WebElement address=webdriver.findElement(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > label:nth-child(2) > address > span"));
				String checkout_address=address.getText();
				msg="Selected adress : "+checkout_address;
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				
				WebElement delivery_button=webdriver.findElement(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > label:nth-child(2) > div > button"));
				delivery_button.click();
			}
				
			// to check "no_address_found text is displaying"
			try {
				Thread.sleep(500);
			} catch (InterruptedException e2) {
				
				e2.printStackTrace();
			}
			
			/*String no_address=webdriver.findElement(By.cssSelector(UIobjects.no_address_text)).getText();
			if(no_address.equals("No address found")) //need to add address
			{
				System.out.println("clicking on the ADD ADDRESS");
				wait.until(ExpectedConditions.elementToBeSelected(By.cssSelector(UIobjects.add_address_button)));
				webdriver.findElement(By.cssSelector(UIobjects.add_address_button)).click();
				// need to fill the form
			}*/
		/*	int add_select_flag=0;
			int i;
			for(i=2;i<=10;i++)
			{	System.out.println("inside selecting address for-loop");
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > address:nth-child("+i+") > a.overflow-address.text-capitalize.col-dark-grey.ng-binding")));
				webdriver.findElement(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > address:nth-child("+i+") > a.overflow-address.text-capitalize.col-dark-grey.ng-binding")).click();
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				WebElement continue_button=webdriver.findElement(By.cssSelector(UIobjects.continue_add_css));
				if(continue_button.isEnabled())
				{
					msg="address"+(i-1)+" is selected";
					System.out.println(msg);
					continue_button.click();
					add_select_flag=1;
					break;
				}
				
			}
			if(i>10 && add_select_flag==0) // couldnt select any addresses
			{
				msg="Address is not selected";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Log", "FAIL",Driver.column);
				
				write.writeReports("Error",msg,Driver.column);
				msg="i value is "+i+" Exceeded selecting address";
				write.writeReports("Error",msg,Driver.column);
				Driver.FLAG=0;
				return;
			}*/
			
			
		/*	wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.select_add1_css)));
			WebElement continue_button=webdriver.findElement(By.cssSelector(UIobjects.continue_add_css));
			int i=0;
			while(!continue_button.isEnabled())
			{
				i++;
				webdriver.findElement(By.cssSelector("#address-body a.overflow-address :nth-child("+i+")")).click();
				Thread.sleep(1000);
				
			}*/
			
			
			Thread.sleep(2000);
			
			
			//checking order review
			int i;
			List<WebElement> itemcount_w=webdriver.findElements(By.cssSelector("#orders-information-table > div.orders-tbody > div"));
			int itemcount=itemcount_w.size();
			System.out.println("total no of items in the order review page is: "+itemcount);
			String product_names[]=new String[10];
			for(i=1;i<=itemcount;i++)
			{
				String product_name=webdriver.findElement(By.cssSelector("#orders-information-table > div.orders-tbody > div:nth-child("+i+") > div:nth-child(2) > p:nth-child(1)")).getText();
				product_names[i-1]=product_name; // array indexing starts form 0.
			}
			System.out.println("product_names array elements\n");
			for(i=0;i<itemcount;i++)
			{
				System.out.println("product"+(i)+": "+product_names[i]);
				
			}
			
			int cart_count=cart_item_list.size();
			int flag=0;
			if(cart_count==itemcount)
			{
				System.out.println("cart quantity =  order review page qty");
				for(i=0;i<itemcount;i++)
				{
					msg="line item"+i;
					System.out.println(msg);
					write.writeReports("Log", msg,Driver.column);
							
					String name1=(product_names[i]).toLowerCase();
					String name2=((cart_item_list.get(i)).getName()).toLowerCase();
					if(!(name1.equals(name2)))
					{
						flag=1;
						msg="cart- item name: "+name1+" order review- item name: "+name2;
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
						write.writeReports("Log","FAIL",Driver.column);
					}
				}
				if(flag!=1)
				{
					msg="All item in the cart are present in order_review page";
					write.writeReports("Log", msg,Driver.column);
				}
			}
			else
			{	
				msg="cart qty and order review qty mismatch";
				System.out.println(msg);
				write.writeReports("Log",msg, Driver.column);
				write.writeReports("Error", msg, Driver.column);
				
				msg="cart qty: "+cart_count+" order review qty: "+itemcount;
				System.out.println(msg);
				write.writeReports("Error", msg, Driver.column);
			}
						
			msg="clicking PROCEED TO PAY button";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.proceed_to_pay_css)));
			webdriver.findElement(By.cssSelector(UIobjects.proceed_to_pay_css)).click();
			//Thread.sleep(10000);
			
			/*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log","FAIL",Driver.column);
			
		}*/
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log","FAIL",Driver.column);
		}
				
	}
		
public void orderCOD()
{
	
	try {
		msg="selecting COD payment";
		System.out.println(msg);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.COD_btn_css)));
		webdriver.findElement(By.cssSelector(UIobjects.COD_btn_css)).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("COD payment");
		
		if (webdriver.findElement(By.cssSelector("#codButton")).isDisplayed())
			{
				msg="clicking on place order button";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				webdriver.findElement(By.cssSelector("#codButton")).click();
				COD_flag=1;
			}
		else
			System.out.println("COD not available");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(COD_flag==1)
		{
			orderNo= webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-inner-header > div.bit-2.padding-zero.pull-left.text-left > p.text-uppercase.margin-th-each > strong")).getText();
			System.out.println(orderNo);
			msg= "Order Number: "+orderNo;
			System.out.println(msg);
			write.writeReports("Log", msg, Driver.column);
			Driver.FLAG++;
			write.writeReports("Log","PASS",Driver.column);
		}
		else
		{
			msg="something went wrong!! could't place COD order";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			write.writeReports("Log","FAIL",Driver.column);
			write.writeReports("Error", msg,Driver.column);
			Driver.FLAG=0;
			//String text=webdriver.findElement(By.cssSelector("#cod-pp > div:nth-child(2) > p"))
		}
	} catch (Exception e) {
				e.printStackTrace();
	}

	
}
public void clearCartItems()
{
	try {
		Thread.sleep(500);
	} catch (InterruptedException e1) {
		
		e1.printStackTrace();
	}
	
	msg="Clicking on minicart";
	System.out.println(msg);
	write.writeReports("Log", msg,Driver.column);
	
	webdriver.findElement(By.cssSelector(UIobjects.minicart_css)).click();
	
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e1) {
		
		e1.printStackTrace();
	}
	
	
	List<WebElement> order_table_items=webdriver.findElements(By.cssSelector(UIobjects.order_table_items_css));
	int order_table_items_count=order_table_items.size();
	
	msg="total no of line items in the cart is: "+order_table_items_count;
	System.out.println(msg);
	write.writeReports("Log", msg, Driver.column);
	
	//cartitems=new CartItem[order_table_items_count];// declaring the size of cartitems array
	//CartItem item;
	//System.out.println("order_table_items_count "+order_table_items_count);
	
	if(order_table_items_count!=0)
	{
		int i=1;
	
		System.out.println("\nlist items");
		i=order_table_items_count;
		for(int j=1;j<=order_table_items_count;j++)
		{
			try {
				String name=(order_table_items.get(i-1)).findElement(By.cssSelector("#cart_product_"+i+" > div:nth-child(2) > p:nth-child(1) > a")).getText();
				msg="product name: "+name;
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				WebElement remove=(order_table_items.get(i-1)).findElement(By.cssSelector("#cart_product_"+i+" > div:nth-child(2) > div > a"));// i is required to access list element becoz list indexing starts from '0' 
				msg="clicking on remove button of line item"+i;
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				remove.click();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i--;
			} catch (Exception e) {
				e.printStackTrace();
				Driver.FLAG=0;
				write.writeReports("Log", "FAIL", Driver.column);
				printException(e);
			}
		}
			//System.out.println("last item in the list: "+(order_table_items.get(1)).getText());
		//removing the items from list
		cart_item_list.removeAll(cart_item_list);
		// clear the product names in the PD_
		PD_product_name=new String[10];
		product_num=1;// resetting the product count
		System.out.println("length of cart_item_list"+cart_item_list.size());
		Driver.FLAG++;
		write.writeReports("Log","PASS", Driver.column);
	}
	
}
public void confirmationPage()
{
	try {
		
		Thread.sleep(1000);
		String current_URL=webdriver.getCurrentUrl();
		msg="current URL"+current_URL;
		System.out.println(msg);
		write.writeReports("Log", msg,Driver.column);
		
		String URL_Rno=current_URL.replaceAll("[^0-9]", "");
		msg="URL Rnum: "+URL_Rno;
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		
		String[] URL_array=current_URL.split("/");
		System.out.println("ULR_array_size: "+URL_array.length);
		System.out.println("URL elements:");
		for(String s:URL_array)
		{
			System.out.println(s+"\n");
		}
		System.out.println("4th element in URL_array:"+URL_array[4]);
		
		String URL_array4 =URL_array[4].replaceAll("[^a-z]", " ");
		System.out.println("array4 elements are after replace with underscore: "+URL_array4);
		String[] array_split=URL_array4.split(" ");
		System.out.println("array_split elements are:");
		for(String s:array_split)
		{
			System.out.println("srray1:"+s);
		}
		String status=array_split[2];
		msg= "status is :"+status;
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		
		
		if(COD_flag==1)
		{
				if(status.equals("complete"))
				{
					msg="URL pattern is correct with 'status=complete'";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					try{
						String header_msg=webdriver.findElement(By.cssSelector("#thanku_header")).getText();
						msg="header msg "+header_msg;
						System.out.println(msg);
						write.writeReports("Log", msg,Driver.column);
						
						if(header_msg.equals("THANK YOU FOR YOUR ORDER"))
						{
							msg="cofirmation msg is correct: "+header_msg;
							System.out.println(msg);
							write.writeReports("Log", msg, Driver.column);
						}
					}
					catch(Exception e)
					{
						Driver.FLAG=0;
						msg="No Confirmation message";
						System.out.println(msg);
						write.writeReports("Log",msg,Driver.column);
						write.writeReports("Log","FAIL",Driver.column);
						printException(e);
					}
								
				}
				else
				{
					msg="order is successfull but Error in URL status";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Log","FAIL",Driver.column);
					msg="status is: "+status;
					System.out.println(msg);
					write.writeReports("Error", msg,Driver.column);
					Driver.FLAG=0;
				}
		}
		//order no below confirmation page
		String orderNo1=webdriver.findElement(By.cssSelector("#order-complete > div > section > p.font-14 > span")).getText();
		System.out.println("second order no is:"+orderNo1);
		orderNo1=orderNo1.replaceAll("[^0-9]","");
		System.out.println("orderNo1 after replace all"+orderNo1);
		
		
		// checking header in the confirmation order table page
		//comparing the order number b/w url vs tablelist vs head
		String orderNo2=webdriver.findElement(By.xpath("//*[@id='order-complete']/div/section/p[2]/span")).getText();
		//orderNo= webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-inner-header > div.bit-2.padding-zero.pull-left.text-left > p.text-uppercase.margin-th-each > strong")).getText();
		System.out.println("order no: "+orderNo2);
		orderNo2=orderNo2.replaceAll("[^0-9]", "");
		System.out.println("orderNo1 after replacement"+orderNo2);
		if(URL_Rno.equals(orderNo1))
		{
			if(orderNo1.equals(orderNo2))
			{
				msg="all Order Numbers are matching";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
			}
			else
			{
				msg="header order number doesnot match with table order number";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				write.writeReports("Log", msg,Driver.column);
				
				msg="orderNo1 = "+orderNo1+" orderNo2 = "+orderNo2;
				write.writeReports("Error",msg,Driver.column);
				Driver.FLAG=0;
			}
		}
		else
		{
			msg="URL_order no doesnt match header order number";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			write.writeReports("Log","FAIL", Driver.column);
			
			msg="URL_Rno = "+URL_Rno+" orderNo1 = "+orderNo1;
			write.writeReports("Error", msg,Driver.column);
			Driver.FLAG=0;
		}
		
		
		
		
		String item_qty_s=webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-inner-header > div.bit-2.padding-zero.pull-left.text-left > p:nth-child(2)")).getText();
		System.out.println("item_qty: "+item_qty_s);
		item_qty_s=item_qty_s.replaceAll("[^0-9]", "");
		int item_qty_i=Integer.parseInt(item_qty_s);
		System.out.println("item_qty_i"+item_qty_i);
		msg="item_qty: "+item_qty_i;
		write.writeReports("Log",msg,Driver.column);
		
		
		String order_date_s=webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-inner-header > div.bit-2.padding-zero.pull-left.text-left > p.margin-th-each.col-666.ng-binding")).getText();
		msg="order_date_s: "+order_date_s;
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		
		String total_amt_s=webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-inner-header > div.bit-2.padding-zero.pull-right.text-right > p.text-capitalize.margin-th-each > strong")).getText();
		msg="total amount_s: "+total_amt_s;
		System.out.println(msg);
		write.writeReports("Log",msg, Driver.column);
		
		String payment_mode=webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-inner-header > div.bit-2.padding-zero.pull-right.text-right > p.text-capitalize.payment-mode > strong")).getText();
		msg="Payment mode: "+payment_mode;
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		
		String product_name= webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-rows > div.thanku-row-wrap.ng-scope > div.ng-scope > div.order-image-details.bit-2-left-th.padding-zero.pull-left.text-left > div:nth-child(2) > p:nth-child(1) > a")).getText();
		msg="product name: "+product_name;
		System.out.println(msg);
		write.writeReports("Log", msg,Driver.column);
		
		String product_price=webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-rows > div.thanku-row-wrap.ng-scope > div.ng-scope > div.order-image-details.bit-2-left-th.padding-zero.pull-left.text-left > div:nth-child(2) > p:nth-child(3)")).getText();
		msg="product price:"+product_price;
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		
		//Extracting the items information
				
		List<WebElement> confirm_order_list = webdriver.findElements(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-rows>div"));
		int confirm_order_list_count = confirm_order_list.size();
		System.out.println("Confirm_order_list size"+confirm_order_list_count);
		int cart_item_count=cart_item_list.size();
		System.out.println("cart_item_count:"+cart_item_count);
		
		//comapring total items from cart with totalitems in confirmation page
		if((confirm_order_list_count-1)==cart_item_count) //(confirm_order-1)becoz list includes last row that has address details
		{
			
			msg="cart item count is equal to confirmation item count";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			if((confirm_order_list_count-1)==item_qty_i)
			{
				msg="'Item' attribute match with total no of items in the page";
				System.out.println(msg);
				write.writeReports("Log",msg, Driver.column);
			}
			else
			{
				msg="'Item' attribute doesnt not match with total no of items in the page";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				write.writeReports("Log", "FAIL",Driver.column);
				Driver.FLAG=0;
			}
		}
		else{
			msg="cart item count is not equal to confirmation page item count!!";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			write.writeReports("Log",msg,Driver.column);
			Driver.FLAG=0;
		}
						
		System.out.println("\n ");
		CartItem item;
		for(int i=1;i<confirm_order_list_count;i++)
		{	
			//fetching line item from cart_item_list list to compare with the items in the cart
			item=cart_item_list.get(i);
			
			msg="Line item"+i+" details";
			System.out.println(msg);
			write.writeReports("Log",msg, Driver.column);
			
			String product_name1=webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-rows > div:nth-child("+i+") > div.ng-scope > div.order-image-details.bit-2-left-th.padding-zero.pull-left.text-left > div:nth-child(2) > p:nth-child(1) > a")).getText();
			msg="product name: "+product_name1;
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			if(product_name1.equals(item.getName()))
			{
				msg="product name in the coformation page doesnot match with one in the cart";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				write.writeReports("Error","line item"+i, Driver.column);
				write.writeReports("Error",msg,Driver.column);
				msg="product name in confirmation page: "+product_name1+" product name in cart: "+item.getName();
				write.writeReports("Error",msg, Driver.column);
			}
				
			
			String size_s=webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-rows > div.thanku-row-wrap.ng-scope > div.ng-scope > div.order-image-details.bit-2-left-th.padding-zero.pull-left.text-left > div:nth-child(2) > p:nth-child(2)")).getText();
			System.out.println("size is: "+size_s);
			String[] size_array=size_s.split(" ");
			System.out.println("size array is:");
			for(String s:size_array)
			{
				System.out.println(s+"\n");
			}
			String size =size_array[2];
			String qty_s=size_array[6];
			int qty=Integer.parseInt(qty_s);
			
			msg="size: "+size;
			System.out.println(msg);
			write.writeReports("Log",msg, Driver.column);
			if(!size.equals(item.getSize()))
			{
				msg="product size in the coformation page doesnot match with one in the cart";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				write.writeReports("Error","line item"+i, Driver.column);
				write.writeReports("Error",msg,Driver.column);
				msg="product size in confirmation page: "+size+" product size in cart: "+item.getSize();
				write.writeReports("Error",msg, Driver.column);
			}
			
			msg="qty: "+qty;
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			if(qty!=item.getQuantity())
			{
				msg="product quantity in the cofirmation page doesnot match with one in the cart";
				System.out.println(msg);
				write.writeReports("Log",msg,Driver.column);
				write.writeReports("Error","line item"+i, Driver.column);
				write.writeReports("Error",msg,Driver.column);
				msg="product quantity in confirmation page: "+qty+" product qty in cart: "+item.getQuantity();
				write.writeReports("Error",msg, Driver.column);
			}
			
			String price = webdriver.findElement(By.cssSelector("#order-body > div.mainthanku-repeat-wrap > div.thanku-rows > div.thanku-row-wrap.ng-scope > div.ng-scope > div.order-image-details.bit-2-left-th.padding-zero.pull-left.text-left > div:nth-child(2) > p:nth-child(3)")).getText();
			System.out.println("price: "+price);
			price=price.replaceAll("[^0-9]", "");
			int price_i=Integer.parseInt(price);
			price_i=price_i/100;
			System.out.println("int price: "+price_i);
			msg="price: "+price_i;
			write.writeReports("Log",msg,Driver.column);
			
			
			
			//System.out.println("inside for loop, product name: "+product_name1);
			
			//String price=webdriver.findElement(By.cssSelector(""));
		}
		
		
		
	
	
	} catch (Exception e) {
		
		e.printStackTrace();
	}
	
}
public void orderCC()
{
	wait= new WebDriverWait(webdriver, 20);
	
	wait.until(ExpectedConditions.elementToBeClickable(By.id(UIobjects.credit_card_id)));
	//webelement;
	
}
public void breadCrums()
{
	try {
		Thread.sleep(3000);
		List<WebElement> page; WebElement breadcrum;
		//product catalog page
		page=webdriver.findElements(By.id("product-catalog")); // 'findElements' will return an empty list if no matching elements are found instead of an exception.
		if(page.size()!=0)// page- has element pointing to id:"product-catalog" ie it is a product listing page
		{
			msg="in product-catalog page";
			System.out.println(msg);
			write.writeReports("Log", msg, Driver.column);
			System.out.println("size of page list is"+page.size());
			WebElement product_listing=page.get(0);
			List <WebElement> section= product_listing.findElements(By.cssSelector("breadcrumbs-custom > section"));// breadcrums
			int length=section.size();
			msg="section list length: "+length;
			System.out.println(msg);
			write.writeReports("Log", msg, Driver.column);
			
			for(int i=(length-1);i>=2;i--) // clicking from last element and (length-1) last element is not clickable, i=1 is /home - its a static page not listing page
			{
				Thread.sleep(3000);
				
				System.out.println("i value is: "+i);
				breadcrum=webdriver.findElement(By.cssSelector("#product-catalog > breadcrumbs-custom > section:nth-child("+i+")"));// referencing from #product-catalog is required to avoid ERROR: "stale element reference".
				msg="clciking on: "+breadcrum.getText();
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				breadcrum.click();
				
				Thread.sleep(5000);
				sort(1);//low_high
				try {
					System.out.println("clciking on scrollup");
					webdriver.findElement(By.cssSelector(".scrollup")).click();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				sort(2);//high_low
				try {
					System.out.println("clicking on scrollup");
					webdriver.findElement(By.cssSelector(".scrollup")).click();
				} catch (Exception e) {
					
					e.printStackTrace();
				} 
				
				
			}
			Driver.FLAG++;
			write.writeReports("Log", "PASS", Driver.column);
		}
		else{// for product-view page
			
			page=webdriver.findElements(By.id("product-view-container"));
			if(page.size()!=0)// page- has element pointing to id:"product-catalog" ie it is a product listing page
			{
				msg="in product-view page";
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				System.out.println("size of page list is"+page.size());
				WebElement product_listing=page.get(0);
				List <WebElement> section= product_listing.findElements(By.cssSelector("breadcrumbs-custom > section"));// breadcrums
				int length=section.size();
				msg="section list length: "+length;
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				
				breadcrum=webdriver.findElement(By.cssSelector("#product-view-container > breadcrumbs-custom > section:nth-child("+(length-1)+")"));// becoz the last breadcrum id is #product-view-container
				// after clicking on the breadcrum the id will be changed to product-catalog
				msg="clciking on: "+breadcrum.getText();
				System.out.println(msg);
				write.writeReports("Log", msg, Driver.column);
				breadcrum.click();
				
				Thread.sleep(5000);
				sort(1);//low_high
				try {
					System.out.println("clciking on scrollup");
					webdriver.findElement(By.cssSelector(".scrollup")).click();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				sort(2);//high_low
				try {
					System.out.println("clicking on scrollup");
					webdriver.findElement(By.cssSelector(".scrollup")).click();
				} catch (Exception e) {
					
					e.printStackTrace();
				} 
				
				
				for(int i=(length-2);i>=2;i--) // clicking from last element and (length-2) last element is already clicked, i=1 is /home - its a static page not listing page
				{
					Thread.sleep(3000);
					
					System.out.println("i value is: "+i);
					breadcrum=webdriver.findElement(By.cssSelector("#product-catalog > breadcrumbs-custom > section:nth-child("+i+")"));// referencing from #product-catalog is required to avoid ERROR: "stale element reference".
					msg="clciking on: "+breadcrum.getText();
					System.out.println(msg);
					write.writeReports("Log", msg, Driver.column);
					breadcrum.click();
					
					Thread.sleep(5000);
					sort(1);//low_high
					try {
						System.out.println("clciking on scrollup");
						webdriver.findElement(By.cssSelector(".scrollup")).click();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					sort(2);//high_low
					try {
						System.out.println("clicking on scrollup");
						webdriver.findElement(By.cssSelector(".scrollup")).click();
					} catch (Exception e) {
						
						e.printStackTrace();
					} 
									
				}
				
			Driver.FLAG++;
			write.writeReports("Log", "PASS", Driver.column);
			}
			else
			{
				Driver.FLAG=0;
				msg="unable to find breadcrums in the given page (product-catalog/product-view)";
				write.writeReports("Log", msg, Driver.column);
				write.writeReports("Log", "FAIL", Driver.column);
			}
		}
	} catch (Exception e) {
	 	e.printStackTrace();
	 	Driver.FLAG=0;
	 	write.writeReports("Log", "FAIL", Driver.column);
	 	printException(e);
	}
}

public void myAccounts()
{
	
	try {
		
		msg="checking My Acounts";
		write.writeReports("Log", msg, Driver.column);
		wait = new WebDriverWait(webdriver,50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(UIobjects.acc_mem_name_id)));
		WebElement menu = webdriver.findElement(By.id(UIobjects.acc_mem_name_id));
		new Actions(webdriver).moveToElement(menu).build().perform();
		System.out.println("calling perform function");
		Thread.sleep(2000);
		
		msg="clicking on My Orders";
		System.out.println(msg);
		write.writeReports("Log", msg, Driver.column);
		webdriver.findElement(By.cssSelector(UIobjects.my_orders_css)).click();
		
		msg="clicking on recent 'ORDER DETAIL' button";
		write.writeReports("Log", msg, Driver.column);
		webdriver.findElement(By.cssSelector(UIobjects.recent_orderdetail_button_css)).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.order_detail_address_css)));
		WebElement address=webdriver.findElement(By.cssSelector(UIobjects.order_detail_address_css));
		String shipping_address=address.getText();
		msg="recent order Address : "+shipping_address;
		String[] default_shipping_address = new String[10];
		default_shipping_address = shipping_address.split(",");
		System.out.println("Default shipping address");
		for(String each:default_shipping_address)
		{
			System.out.println(each+"\n");
		}
		System.out.println(msg);
		write.writeReports("Log", msg, Driver.column);
	
	} catch (InterruptedException e) {
		
		e.printStackTrace();
		printException(e);
	}
}
	
	public void emailReport()
	{
		final String username = Driver.properties.getProperty("FromEmailID");
	    final String password = Driver.properties.getProperty("Password");

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });

	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("tabs@styletag.com"));//from address
	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(Driver.properties.getProperty("ToEmailID")));//to address
	        message.setSubject("Sanity Test Report");
	        message.setText("PFA for sanity reports");

	        MimeBodyPart messageBodyPart = new MimeBodyPart();

	        Multipart multipart = new MimeMultipart();

	        messageBodyPart = new MimeBodyPart();
	        String file = write.report_file_path;
	        String fileName = write.file_name;
	        DataSource source = new FileDataSource(file);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(fileName);
	        multipart.addBodyPart(messageBodyPart);

	        message.setContent(multipart);

	        System.out.println("Sending");

	        Transport.send(message);
	        Driver.FLAG++;

	        System.out.println("Done");

	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	 }
	
	
	
	public void testErrorpage1()
	{
		/*write.writeReports("Log","test1", Driver.column);
		write.writeReports("Log","test2", Driver.column);
		write.writeReports("Error","test3", Driver.column);
		write.writeReports("Error","test4", Driver.column);
		
		write.writeReports("Log","test5", Driver.column);
		write.writeReports("Error","test6", Driver.column);
		write.writeReports("Log","test7", Driver.column);
		write.writeReports("Error","test8", Driver.column);
		write.writeReports("Log", "test9", Driver.column);
		write.writeReports("Error", "test9",Driver.column);
		write.writeReports("Log", "test10", Driver.column);
		write.writeReports("Error", "test11",Driver.column);
		write.writeReports("Log", "test10", Driver.column);
		write.writeReports("Log", "test11", Driver.column);
		write.writeReports("Log", "test122", Driver.column);*/
		
		write.writeReports("Error", "test12",Driver.column);
		write.writeReports("Error", "test13",Driver.column);
		write.writeReports("Error", "test14",Driver.column);
		write.writeReports("Error", "test15",Driver.column);
		write.writeReports("Error", "test16",Driver.column);
		write.writeReports("Error", "test17",Driver.column);
		write.writeReports("Error", "test23",Driver.column);
		write.writeReports("Error", "test24",Driver.column);
		write.writeReports("Error", "test25",Driver.column);
		write.writeReports("Error", "test12",Driver.column);
		write.writeReports("Error", "test13",Driver.column);
		write.writeReports("Error", "test14",Driver.column);
		write.writeReports("Error", "test15",Driver.column);
		write.writeReports("Error", "test16",Driver.column);
		write.writeReports("Error", "test17",Driver.column);
		write.writeReports("Error", "test23",Driver.column);
		write.writeReports("Error", "test24",Driver.column);
		write.writeReports("Error", "test25",Driver.column);
		
		write.writeReports("Log", "test18",Driver.column);
		write.writeReports("Log", "test19",Driver.column);
		write.writeReports("Log", "test20",Driver.column);
		write.writeReports("Log", "test21",Driver.column);
		write.writeReports("Log", "test22",Driver.column);
		write.writeReports("Log", "test26",Driver.column);
		write.writeReports("Log", "test27",Driver.column);
		write.writeReports("Log", "test28",Driver.column);
		write.writeReports("Log", "test29",Driver.column);
		write.writeReports("Log", "test18",Driver.column);
		write.writeReports("Log", "test19",Driver.column);
		write.writeReports("Log", "test20",Driver.column);
		write.writeReports("Log", "test21",Driver.column);
		write.writeReports("Log", "test22",Driver.column);
		write.writeReports("Log", "test26",Driver.column);
		write.writeReports("Log", "test27",Driver.column);
		write.writeReports("Log", "test28",Driver.column);
		write.writeReports("Log", "test29",Driver.column);


	}
	public void test2()
	{
		write.writeReports("Log","test5 page2", Driver.column);
		write.writeReports("Error","test6 page2", Driver.column);
		write.writeReports("Log","test7 page2", Driver.column);
		write.writeReports("Error","test8 page2", Driver.column);

	}
	public void test3()
	{
		write.writeReports("Error", "test12",Driver.column);
		write.writeReports("Error", "test13",Driver.column);
		write.writeReports("Error", "test14",Driver.column);
		write.writeReports("Error", "test15",Driver.column);
		write.writeReports("Error", "test16",Driver.column);
		write.writeReports("Error", "test17",Driver.column);
		write.writeReports("Error", "test23",Driver.column);
		write.writeReports("Error", "test24",Driver.column);
		write.writeReports("Error", "test25",Driver.column);
		
		write.writeReports("Log", "test18",Driver.column);
		write.writeReports("Log", "test19",Driver.column);
		write.writeReports("Log", "test20",Driver.column);
		write.writeReports("Log", "test21",Driver.column);
		write.writeReports("Log", "test22",Driver.column);
		write.writeReports("Log", "test26",Driver.column);
		write.writeReports("Log", "test27",Driver.column);
		write.writeReports("Log", "test28",Driver.column);
		write.writeReports("Log", "test29",Driver.column);
		
	}
		
}
