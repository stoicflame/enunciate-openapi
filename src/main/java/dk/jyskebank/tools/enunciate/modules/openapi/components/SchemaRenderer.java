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
package dk.jyskebank.tools.enunciate.modules.openapi.components;

import static dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper.safeYamlString;

import java.util.regex.Pattern;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.DataType;

import dk.jyskebank.tools.enunciate.modules.freemarker.Typed1ArgTemplateMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;

public class SchemaRenderer extends Typed1ArgTemplateMethod<String, String> {
  private static final Pattern VALID_REF_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9\\.\\-_]+$");
  
  @SuppressWarnings("unused") private final EnunciateLogger logger;
  private final DataType datatype;

  private boolean syntaxIsJson;
  private final ObjectTypeRenderer objectTypeRenderer;

  public SchemaRenderer(EnunciateLogger logger, ObjectTypeRenderer objectTypeRenderer, DataType datatype, boolean syntaxIsJson) {
    super(String.class);
    this.logger = logger;
    this.objectTypeRenderer = objectTypeRenderer;
    this.datatype = datatype;
    this.syntaxIsJson = syntaxIsJson;
  }

  @Override
  protected String exec(String nextLineIndent) {
    IndententationPrinter ip = new IndententationPrinter(nextLineIndent);
    renderLines(ip);
    return ip.toString();
  }
  
  private void renderLines(IndententationPrinter ip) {
    ip.add(getRefId() + ":");
    objectTypeRenderer.render(ip, datatype, syntaxIsJson);
  }

  private String getRefId() {
    String slug = datatype.getSlug();
    if (!VALID_REF_ID_PATTERN.matcher(slug).matches()) {
      throw new IllegalStateException("Invalid reference id '" + slug + "' for datatype " + datatype);
    }
    return safeYamlString(slug);
  }
}
