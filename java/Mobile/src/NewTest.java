import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

public class NewTest {
  @Test
  public void f() {
	  System.out.println("Hai f()");
  }
  @BeforeTest
  public void beforeTest() {
	  System.out.println("Inside NewTest's Before test");
  }

}
