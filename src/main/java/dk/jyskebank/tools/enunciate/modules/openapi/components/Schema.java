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

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.DataType;

import dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer;

public class Schema {
  private SchemaRenderer renderer;

  public Schema(EnunciateLogger logger, ObjectTypeRenderer objectTypeRenderer, DataType datatype, boolean syntaxIsJson) {
    this.renderer = new SchemaRenderer(logger, objectTypeRenderer, datatype, syntaxIsJson);
  }

  public SchemaRenderer getRender() {
    return renderer;
  }
}
