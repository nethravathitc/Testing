import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SecondTest {
	public static int a;
	SecondTest()
	{
		a=300;
	}
	
  @Test
  public void function1() {
	  System.out.println("SeconTest class: function1");
  }
  @Test
  @Parameters("value")
  public void function2(String x)
  {
	  System.out.println("Second class: function2");
	  System.out.println("paramaters value is: "+x);
	  func();
	  
  }
  public void func()
  {
	  a=300;
  }
}
