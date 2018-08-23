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

import com.webcohesion.enunciate.api.resources.Method;

import dk.jyskebank.tools.enunciate.modules.freemarker.Typed1ArgTemplateMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.FindBestDataTypeMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.FindBestDataTypeMethod.MediaAndType;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;

public class RequestEntityRenderer extends Typed1ArgTemplateMethod<String, String> {
  private MediaAndType mediaAndType;
  private boolean hasMedia;
  private String mediaWithFallback;
  private DataTypeReferenceRenderer dataTypeReferenceRenderer;

  public RequestEntityRenderer(DataTypeReferenceRenderer dataTypeReferenceRenderer, Method method) {
    super(String.class);
    this.dataTypeReferenceRenderer = dataTypeReferenceRenderer;
    
    mediaAndType = FindBestDataTypeMethod.findBestMediaAndType(method.getRequestEntity());
    hasMedia = mediaAndType != null && mediaAndType.media != null;
    mediaWithFallback = hasMedia ? mediaAndType.media.getMediaType() : "*/*";
  }
  
  @Override
  protected String exec(String nextLineIndent) {
      IndententationPrinter ip = new IndententationPrinter(nextLineIndent);

      ip.add(safeYamlString(mediaWithFallback), ":");
      ip.nextLevel();
      ip.add("schema:");
      ip.nextLevel();
      if (hasMedia) {
        dataTypeReferenceRenderer.addSchemaRef(ip, mediaAndType.type);
      } else {
        dataTypeReferenceRenderer.renderObsoletedFileFormat(ip);
      }
      ip.prevLevel();
      
      ip.prevLevel();
      return ip.toString();
  }
}
