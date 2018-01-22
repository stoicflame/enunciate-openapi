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
package dk.jyskebank.tools.enunciate.modules.openapi.paths;

import java.util.stream.Stream;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.Example;

import dk.jyskebank.tools.enunciate.modules.freemarker.Typed1ArgTemplateMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.JsonToYamlHelper;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper;

public class ExampleRenderer extends Typed1ArgTemplateMethod<String, String> {
  @SuppressWarnings("unused") private final EnunciateLogger logger;
  private final Example example;

  public ExampleRenderer(EnunciateLogger logger, Example example) {
    super(String.class);
    this.logger = logger;
    this.example = example;
  }

  @Override
  protected String exec(String nextLineIndent) {
    IndententationPrinter ip = new IndententationPrinter(nextLineIndent);

    if ("js".equals(example.getLang())) {
      String yaml = JsonToYamlHelper.jsonToYaml(example.getBody());
      Stream.of(yaml.split("\n"))
        .skip(1) // ----- header
        .forEach(ip::add);
    } else {
      ip.add(YamlHelper.safeYamlString(example.getBody()));
    }
    
    return ip.toString();
  }
}