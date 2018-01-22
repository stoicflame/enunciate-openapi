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
package dk.jyskebank.tools.enunciate.modules.openapi;

import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.modules.jaxb.api.impl.ComplexDataTypeImpl;

/**
 * Provides access to methods in DataType implementations in other modules.
 */
public class AccessorDataType {
  private AccessorDataType() {}
  
  public static String getXmlName(DataType dt) {
    if (dt instanceof ComplexDataTypeImpl) {
      return ((ComplexDataTypeImpl)dt).getXmlName();
    }
    return null;
  }
}
