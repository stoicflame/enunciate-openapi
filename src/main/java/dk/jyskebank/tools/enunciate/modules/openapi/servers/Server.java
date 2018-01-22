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
package dk.jyskebank.tools.enunciate.modules.openapi.servers;

import static dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper.safeYamlString;

import java.util.List;
import java.util.Optional;

public class Server {
  private final String url;
  private final Optional<String> description;
  private final List<Variable> variables;

  public Server(String url, Optional<String> description, List<Variable> variables) {
    this.url = url;
    this.description = description;
    this.variables = variables;
  }
  
  public boolean getHasDescription() {
    return description.isPresent();
  }
  
  public String getDescription() {
    return safeYamlString(description.orElse(""));
  }
  
  public String getUrl() {
    return safeYamlString(url);
  }
  
  public boolean getHasVariables() {
    return !variables.isEmpty();
  }
}
