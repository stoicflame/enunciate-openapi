package dk.jyskebank.tools.enunciate.modules.openapi.returns;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dk.jyskebank.tools.enunciate.modules.openapi.EnunciateExec;

@RunWith(Parameterized.class)
public class ReturnsTest {
  @Parameters
  public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
        {"dummy"}
      });
    }
  
  public ReturnsTest(String a) {
  }
  
  @Test
  public void test() throws IOException {
    new EnunciateExec("returns").test();
  }
}
