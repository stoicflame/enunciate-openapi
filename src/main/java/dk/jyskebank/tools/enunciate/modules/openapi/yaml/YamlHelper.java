/*
 * Copyright © 2017-2018 Jyske Bank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.jyskebank.tools.enunciate.modules.openapi.yaml;

import java.util.Arrays;
import java.util.List;

public class YamlHelper {
  private YamlHelper() {}
  // as per http://www.yaml.org/spec/1.2/spec.html#id2776092
  private static final List<Character> VALID_CHAR_AFTER_ESCAPE = Arrays.asList('0', 'a', 'b', 't', '\t', 'n', 'v', 'f', 'r', 'e', ' ', '"', '/', '\\', 'N', '_', 'L', 'P', 'x', 'u', 'U');
  
  public static final String safeYamlString(String str) {
    if (str == null) {
      return null;
    }
    String woNewlines = str.replace("\r\n", "\\n").replace("\n", "\\n");
    verifyStringIsValidYaml(woNewlines);
    
    if (woNewlines.startsWith("\"") && woNewlines.endsWith("\"")) {
      return woNewlines;
    }
    return '"' + woNewlines.replace("\"", "\\\"") + '"';
  }

  private static void verifyStringIsValidYaml(String str) {
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (c == '\\') {
        if (i+1 == str.length()) {
          throw new IllegalStateException("String ends in escape character: " + str);
        }
        char second = str.charAt(i+1);
        if (!VALID_CHAR_AFTER_ESCAPE.contains(second)) {
          throw new IllegalStateException("String has bad character (" + second + ") after escape: " + str);
        }
      }
    }
  }
}
