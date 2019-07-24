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

import static dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper.safeYamlString;

import java.util.List;
import java.util.Optional;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.Example;
import com.webcohesion.enunciate.api.resources.Parameter;

import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper;

public class Response {
  @SuppressWarnings("unused") private final EnunciateLogger logger;
  private final int code;
  private final String mediaType;
  private final DataTypeReference dataType;
  private final List<Parameter> headers;
  private final String description;
  private final ResponseDataTypeRenderer renderer;
  private final Optional<ExampleRenderer> exampleRenderer;

  public Response(EnunciateLogger logger, DataTypeReferenceRenderer dataTypeReferenceRenderer, int code, String mediaType, DataTypeReference dataType, List<Parameter> headers, String description, Optional<Example> optionalExample) {
    this.logger = logger;
    this.code = code;
    this.mediaType = mediaType;
    this.dataType = dataType;
    this.headers = headers;
    this.description = YamlHelper.safeYamlString(description);
    renderer = new ResponseDataTypeRenderer(logger, dataTypeReferenceRenderer, dataType, description);
    exampleRenderer = optionalExample.map(e -> new ExampleRenderer(logger, e, dataTypeReferenceRenderer.doRemoveObjectPrefix()));
    
    // TODO: Render headers
  }

  public String getCode() {
    return safeYamlString(Integer.toString(code));
  }

  public String getDescription() {
    return safeYamlString(description);
  }

  public String getMediaType() {
    return safeYamlString(mediaType);
  }
  
  public boolean getHasData() {
    return dataType != null;
  }
  
  public ResponseDataTypeRenderer getRenderDataType() {
    return renderer;
  }

  public ExampleRenderer getRenderExample() {
    return exampleRenderer.get();
  }

  public boolean getHasExample() {
    return exampleRenderer.isPresent();
  }
}
