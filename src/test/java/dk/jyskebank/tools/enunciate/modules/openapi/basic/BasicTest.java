package dk.jyskebank.tools.enunciate.modules.openapi.basic;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import dk.jyskebank.tools.enunciate.modules.openapi.EnunciateExec;

@RunWith(Parameterized.class)
public class BasicTest {
  @Parameters
  public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
        {"dummy"}
      });
    }
  
  public BasicTest(String a) {
  }

  @Test
  public void test() throws IOException {
    new EnunciateExec("basic").test();
  }
}
