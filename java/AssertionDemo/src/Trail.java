import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Trail {
	SoftAssert soft;
  @Test
  public void f() {
	  soft= new SoftAssert();
	  soft.assertEquals(0, 1, "Seertion Error Printing");
	  soft.assertAll();
  }
}
