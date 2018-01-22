package dk.jyskebank.tools.enunciate.modules.openapi.simple;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dk.jyskebank.tools.enunciate.modules.openapi.EnunciateExec;

@RunWith(Parameterized.class)
public class SimpleTest {
  @Parameters
  public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
        {"dummy"}
      });
    }
  
  public SimpleTest(String a) {
  }
  
  @Test
  public void test() throws IOException {
    new EnunciateExec("simple").test();
  }
}
