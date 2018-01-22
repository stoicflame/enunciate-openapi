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

import java.util.Map;
import java.util.EnumMap;

import com.webcohesion.enunciate.api.datatype.BaseTypeFormat;

/**
 * Maps Enunciate base type format to OpenAPI format string.
 * See https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md#dataTypes
 */
public class BaseTypeToOpenApiType {
  private static final Map<BaseTypeFormat, String> baseformat2openapiformat = new EnumMap<>(BaseTypeFormat.class);
  static {
    baseformat2openapiformat.put(BaseTypeFormat.INT32, "int32");
    baseformat2openapiformat.put(BaseTypeFormat.INT64, "int64");
    baseformat2openapiformat.put(BaseTypeFormat.FLOAT, "float");
    baseformat2openapiformat.put(BaseTypeFormat.DOUBLE, "double");
  }

  private BaseTypeToOpenApiType() {}
  
  public static String toOpenApiFormat(BaseTypeFormat format) {
    return format == null ? null : baseformat2openapiformat.get(format);
  }
}
