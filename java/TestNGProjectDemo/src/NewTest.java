import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class NewTest {
	int x;
	/*
  @Test
  public void fun1() {
	  System.out.println("This is first testNG program");
	  System.out.println("x value is "+x);
	  SecondTest obj= new SecondTest();
	  System.out.println("The value of a variable in NewTest class: "+obj.a);
	  
  }
  @BeforeTest
  public void func2()
  {
	  SoftAssert soft= new SoftAssert();
	 String actual="abc";
	  String expected="xyz";
	  soft.assertEquals(actual, expected, "Assertion failed in my program");
	  System.out.println("This is func2: BeforeTest");
	  x=10;
	  System.out.println("x value in fun2: "+x);
	  
	 // writing into log file
	  
	 Logger log= Logger.getLogger("newLog");
	 PropertyConfigurator.configure("logj4.properties");
	 
	 log.info("inside func2 through logger");
	 log.info("New message");
	 soft.assertAll();
	 
	  
	  
  }
  @AfterTest
  public void func3()
  {
	  System.out.println("This is fun3: AfterTest");
	  System.out.println("x value in func3: "+x);
	  
  }*/
  @Test
  public void justTest()
  {
	  System.out.println("justTest function");
  }
  
  @Test(dataProvider="testdata",priority=1, dependsOnMethods={"justTest"},alwaysRun=true,enabled=true)
  public void dataTest(String data1, String data2)
  {
	  System.out.println("printing dataprovider data"+data1+" "+data2 );
  }
  @DataProvider(name="testdata")
  public Object[][] dataSupply()
  {
	  System.out.println("inside dataSupply function");
	  String[][] inputdata= new String[2][2];
	  inputdata[0][0]= new String(" My Name"); inputdata[0][1]= new String(" TestNG");
	  inputdata[1][0]= new String(" what"); inputdata[1][1]= new String(" your Name");
	  return inputdata;
  }
}
