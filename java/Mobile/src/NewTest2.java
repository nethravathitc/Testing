import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

public class NewTest2 {
  @Test
  public void f() {
	  System.out.println("Bye ");
  }
  @BeforeTest
  public void beforeTest() {
	  System.out.println("inside NewTest2's before()");
  }

}
