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

import java.util.ArrayList;
import java.util.Set;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.resources.Parameter;

import dk.jyskebank.tools.enunciate.modules.freemarker.Typed1ArgTemplateMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndentationPrinter;

public class ParameterRenderer extends Typed1ArgTemplateMethod<String, String> {
  @SuppressWarnings("unused") private final EnunciateLogger logger;
  private final Parameter parameter;
  private final DataTypeReferenceRenderer dataTypeReferenceRenderer;
  private final ObjectTypeRenderer objectTypeRenderer;

  public ParameterRenderer(EnunciateLogger logger, DataTypeReferenceRenderer dataTypeReferenceRenderer, ObjectTypeRenderer objectTypeRenderer, Parameter parameter) {
    super(String.class);
    this.logger = logger;
    this.dataTypeReferenceRenderer = dataTypeReferenceRenderer;
    this.objectTypeRenderer = objectTypeRenderer;
    this.parameter = parameter;
  }

  @Override
  protected String exec(String nextLineIndent) {
    IndentationPrinter ip = new IndentationPrinter(nextLineIndent, dataTypeReferenceRenderer.doRemoveObjectPrefix());

    addOptionalEnum(ip);
    addType(ip);

    return ip.toString();
  }

  private void addType(IndentationPrinter ip) {
    if (parameter.isMultivalued()) {
      ip.add("type: array");
      ip.add("items:");
      ip.nextLevel();
      dataTypeReferenceRenderer.renderType(ip, parameter);
      ip.prevLevel();
    } else {
      dataTypeReferenceRenderer.renderType(ip, parameter);
    }
  }

  private void addOptionalEnum(IndentationPrinter ip) {
    Set<String> constraintValues = parameter.getConstraintValues();
    if (constraintValues != null && !constraintValues.isEmpty()) {
      objectTypeRenderer.renderEnum(ip, new ArrayList<>(constraintValues));
    }
  }
}
