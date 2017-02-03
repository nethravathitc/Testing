package com.styletag.scripts;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.styletag.functionalLib.*;
import com.styletag.uiobjects.UIobjects;

public class BusinessAction {
	WebDriverWait wait;
	public WebDriver webdriver;
	Actions act;
	String pd_product_name ,ct_product_name,orderNo;
	int sort_flag;
	ExcelRead xl;
	String msg;
	ExcelWrite write;
	DateFormat df;
	Date dateobj;
	String date;
	public BusinessAction(ExcelWrite write1){
		xl=new ExcelRead("..//Hybrid//src//com//styletag//testcases//InputData.xlsx");
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
		System.setProperty("webdriver.chrome.driver","../Hybrid//chromedriver");
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
			Thread.sleep(1000);
			msg="trying to click on login link";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			
			try {
				WebElement login_name= webdriver.findElement(By.cssSelector(UIobjects.login_name_css));
				act = new Actions(webdriver);
				System.out.println("calling perform function");
				//Thread.sleep(5);
				act.moveToElement(login_name).build().perform();
				webdriver.findElement(By.cssSelector(UIobjects.login_link_css)).click();
			} catch (Exception e) {
				e.printStackTrace();
				Driver.FLAG=0;
				write.writeReports("Log", "FAIL",Driver.column);
				msg="couldn't find login link";
				//write.writeReports("Log", msg,Driver.column);
				write.writeReports("Error", msg, Driver.column);
				printException(e);
			}
			msg="entering login details";
			System.out.println(msg);
			write.writeReports("Log", msg, Driver.column);
			
			//ExcelRead xl= new ExcelRead("//home//styletag//java_test//Test Framework//src//com//styletag//test_cases//InputData.xlsx");
			int no =xl.rowCountInSheet(1);
			//System.out.println("total count "+no);
			//System.out.println(xl.read(2, 0));
			//System.out.println(xl.read(2, 1));
			String emailid=xl.read(2, 0);
			String pwd = xl.read(2, 1);
			webdriver.findElement(By.cssSelector(UIobjects.login_email_css)).sendKeys(emailid);
			webdriver.findElement(By.cssSelector(UIobjects.login_pass_css)).sendKeys(pwd);
			msg= "emaild: "+emailid+" password: "+pwd;
			
			WebElement login_button=webdriver.findElement(By.cssSelector(UIobjects.login_btn_css));
			if (login_button.isEnabled())
			{	
				msg="clicking on login button";
				System.out.println(msg);
				write.writeReports("Log", msg,Driver.column);
				webdriver.findElement(By.cssSelector(UIobjects.login_btn_css)).click();
				WebElement login_flash_msg=webdriver.findElement(By.cssSelector(UIobjects.login_flash_msg_css));
				
				if(login_flash_msg.isDisplayed())
				{
					System.out.println("inside if(login_flash_msg)");
					
					String text=login_flash_msg.getText();
					System.out.println(text);
					msg="flash message: "+text+" - displayed";
					//if(text.contains("Successfully logged in "))
					{
						System.out.println("inside if(contains)");
						System.out.println(msg);
						write.writeReports("Log", msg,Driver.column);
						Thread.sleep(1000);
						WebElement acc_mem_name=webdriver.findElement(By.cssSelector(UIobjects.acc_name_css));
						if(acc_mem_name.isDisplayed())
						{
							msg=acc_mem_name.getText()+" - displayed";
							System.out.println(msg);
							write.writeReports("Log",msg,Driver.column);
							Driver.FLAG++;
							write.writeReports("Log", "PASS", Driver.column);
							
						}
						else
						{
							Driver.FLAG=0;
							msg="account name is not being displayed";
							write.writeReports("Log","FAIL",Driver.column);
							write.writeReports("Error", msg,Driver.column);
						}
						
						
					}
					if(text.contains("Sorry! Invalid email/password combination. Please try again."))
					{
						System.out.println(msg);
						write.writeReports("Log","FAIL",Driver.column);
						write.writeReports("Error", msg,Driver.column);
						Driver.FLAG=0;
					}
					
				}
				

			}
		} catch (Exception e) {
			
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log", "FAIL", Driver.column);
			printException(e);
		}
			
	}
	
	public void logout() throws InterruptedException{
		//Thread.sleep(10);
		System.out.println("User logging out ");
		wait = new WebDriverWait(webdriver,50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(UIobjects.acc_mem_name_id)));
		WebElement menu = webdriver.findElement(By.id(UIobjects.acc_mem_name_id));
		new Actions(webdriver).moveToElement(menu).build().perform();
		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.inner_logout_css)));
		webdriver.findElement(By.cssSelector(UIobjects.inner_logout_css)).click();
		//Thread.sleep(5000);
		webdriver.quit();
	}
	
	public void search()
	{	try {
		Thread.sleep(2500); // this is required in case of search tab is overlapped with any flash msg ex: in product detail page after adding the product to cart, flash msg overlaps the seacrh tab
	} catch (InterruptedException e2) {
		
		e2.printStackTrace();
	}
		String search_keyword,sort_value;
	    int count=1,sort_value_int;
		ExcelRead xl=new ExcelRead("..//Hybrid//src//com//styletag//testcases//InputData.xlsx");
		xl.rowCountInSheet(2);
		search_keyword=xl.read(1,0);
		sort_value=xl.read(1,1);
		
		sort_value=sort_value.replaceAll("[^0-9]", "");
		sort_value_int=Integer.parseInt(sort_value);
		System.out.println("sort value: "+sort_value_int);
		
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
		/*wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.page_title_css)));
		String title = webdriver.findElement(By.cssSelector(UIobjects.page_title_css)).getText();
		msg=" Search page title: "+title;
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		*/
		wait=new WebDriverWait(webdriver,20);
		WebElement product;
		try {
			product = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.product_css)));
			msg="Products are found in the search page";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			sort(sort_value_int);
			if(Driver.FLAG!=0)
			{	Driver.FLAG++;
				System.out.println("Driver.FLAG Value inside search function "+Driver.FLAG);
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
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
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
					FileUtils.copyFile(scrFile, new File("//home//styletag//sanity_reports//product_price_mismatch"+date+".png"));
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
		
		msg="selecting product";
		System.out.println(msg);
		write.writeReports("Log",msg,Driver.column);
		//write.writeReports("Log", "clicking on product", Driver.column);
		
		wait= new WebDriverWait(webdriver,10);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.product_css)));
			webdriver.findElement(By.cssSelector(UIobjects.product_css)).click();
			msg="clicked on product";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
			//get list of all tab browser
			Set<String> allBrowser = webdriver.getWindowHandles();
			for (String eachBrower:allBrowser){
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
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.product_title_css)));
			pd_product_name = webdriver.findElement(By.cssSelector(UIobjects.product_title_css)).getText().toLowerCase();//product Name
			msg="Product Name: "+pd_product_name;
			System.out.println(msg);
			write.writeReports("log",msg,Driver.column);
			
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
				write.writeReports("log",msg,Driver.column);
				
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
				WebElement flash_msg= webdriver.findElement(By.cssSelector(UIobjects.flash_msg_css));
				if (flash_msg.isDisplayed())
				{	msg="Product added to cart ,'Added to Cart' msg displayed";
					System.out.println(msg);
					write.writeReports("Log", msg,Driver.column);
					
				}
				Driver.FLAG++;
				write.writeReports("Log","PASS",Driver.column);
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
					msg="Product is already added to cart";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					write.writeReports("Log", "PASS",Driver.column);
					Driver.FLAG++;
				}
				
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
				
				String path="//home//styletag//sanity_report//screen_shots//ProductDetailPage"+date+".png";
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
			Thread.sleep(2000);
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
			/*msg="clicking on Kuta_kutis";
			System.out.println(msg);
			write.writeReports("Log", msg);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.kurta_kurti_css)));
			webdriver.findElement(By.cssSelector(UIobjects.kurta_kurti_css)).click();
			*///waitForSpinner();
			
			//anarkalis
			msg="clicking on Anarkalis";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(UIobjects.anarkalis)));
			webdriver.findElement(By.cssSelector(UIobjects.anarkalis)).click();
			
			//this is to overcome the menu bars drop down
			System.out.println("scrolling down");
			JavascriptExecutor js = (JavascriptExecutor)webdriver;
			js.executeScript("window.scrollBy(0,100)","");
			
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sort(2);//high_low
					try {
						webdriver.findElement(By.cssSelector(".scrollup")).click();
					} catch (Exception e) {
						// TODO Auto-generated catch block
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
			
			try{
				ct_product_name= webdriver.findElement(By.cssSelector(UIobjects.cartProduct1_css)).getText().toLowerCase();
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
			}
		} catch (Exception e) {// control comes here if it couldn't find ct_product_name
			
			e.printStackTrace();
			write.writeReports("Log","FAIL",Driver.column);
			printException(e);
			Driver.FLAG=0;
		}
		
		
	}	
	
	public void checkout() {
		//try
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
			//Thread.sleep(4000);
			
			String user_logged_in_email=webdriver.findElement(By.cssSelector(UIobjects.user_loggedin_emailid)).getText();
			if(!(user_logged_in_email.equals("")))
			{
				
				System.out.println("Error!! user Id is not displayed");
				msg="Error!! user Id is not displayed";
				write.writeReports("Log", msg,Driver.column);
				write.writeReports("Log","FAIL",Driver.column);
				write.writeReports("Error", msg,Driver.column);
				
				Driver.FLAG=0;
				return;
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
			
			
			msg="selecting address";
			System.out.println(msg);
			write.writeReports("Log", msg,Driver.column);
			
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
			int add_select_flag=0;
			int i;
			for(i=2;i<=10;i++)
			{	System.out.println("inside selecting add for loop");
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > address:nth-child("+i+") > a.overflow-address.text-capitalize.col-dark-grey.ng-binding")));
				webdriver.findElement(By.cssSelector("#address-body > div > div.checkout-addresses.ng-scope > div > div > address:nth-child("+i+") > a.overflow-address.text-capitalize.col-dark-grey.ng-binding")).click();
				
				try {
					Thread.sleep(1500);
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
			if(i>10 && add_select_flag==0) // couldnt select any addesses
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
			}
			
			
		/*	wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.select_add1_css)));
			WebElement continue_button=webdriver.findElement(By.cssSelector(UIobjects.continue_add_css));
			int i=0;
			while(!continue_button.isEnabled())
			{
				i++;
				webdriver.findElement(By.cssSelector("#address-body a.overflow-address :nth-child("+i+")")).click();
				Thread.sleep(1000);
				
			}*/
			
			
			//Thread.sleep(2000);
			
			msg="clicking PROCEED TO PAY button";
			System.out.println(msg);
			write.writeReports("Log",msg,Driver.column);
			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(UIobjects.proceed_to_pay_css)));
			webdriver.findElement(By.cssSelector(UIobjects.proceed_to_pay_css)).click();
			//Thread.sleep(10000);
			
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
			int cod_flag=0;
			if (webdriver.findElement(By.cssSelector("#codButton")).isDisplayed())
				{
					msg="clicking on place order button";
					System.out.println(msg);
					write.writeReports("Log",msg,Driver.column);
					webdriver.findElement(By.cssSelector("#codButton")).click();
					cod_flag=1;
				}
			else
				System.out.println("COD not available");
			
			try {
				Thread.sleep(250000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(cod_flag==1)
			{
				orderNo= webdriver.findElement(By.cssSelector("#order-cancel > div > section > p:nth-child(2) > span")).getText();
				System.out.println(orderNo);
				msg= "Order Number: "+orderNo;
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
			}
		} /*catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Driver.FLAG=0;
			write.writeReports("Log","FAIL",Driver.column);
			
		}*/
				
	}
	
	public void emailReport()
	{
		final String username = "tabs@styletag.com";
	    final String password = "styletag321";

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
	                InternetAddress.parse("nethravathi.tc@styletag.com,prashanth.hn@styletag.com "));//to address
	        message.setSubject("Sanity Report");
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
