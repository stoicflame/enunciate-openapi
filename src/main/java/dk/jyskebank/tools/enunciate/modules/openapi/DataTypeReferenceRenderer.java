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

import java.util.List;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.BaseTypeFormat;
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.DataTypeReference.ContainerType;
import com.webcohesion.enunciate.modules.jaxb.model.types.KnownXmlType;

import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;

public class DataTypeReferenceRenderer {
  @SuppressWarnings("unused")
  private final EnunciateLogger logger;
  
  public DataTypeReferenceRenderer(EnunciateLogger logger) {
    this.logger = logger;
  }
  
  public void render(IndententationPrinter ip, DataTypeReference dtr, String description) {
    if (dtr == null) {
      throw new IllegalStateException("Cannot render null data type");
    }
    
    if (description != null && !description.isEmpty()) {
      ip.add("description: ", description);
    }

    DataType value = dtr.getValue();
    List<ContainerType> containers = dtr.getContainers();
    if (value != null) {
      if (containers != null && !containers.isEmpty()) {
        for (ContainerType ct : containers) {
          if (!ct.isMap()) {
            ip.add("type: array");
            ip.add("items:");
          } else {
            ip.add("type: object");
            ip.add("additionalProperties:");
          }
          ip.nextLevel();
          addSchemaRef(ip, value);
          ip.prevLevel();
        }
      } else {
        addSchemaRef(ip, value);
      }
    } else {
      if (containers != null && !containers.isEmpty()) {
        for (ContainerType ct : containers) {
          if (!ct.isMap()) {
            ip.add("type: array");
            ip.add("items:");
          } else {
            ip.add("type: object");
            ip.add("additionalProperties:");
          }
          ip.nextLevel();
          ip.add("type: ", getBaseType(dtr));
          ip.prevLevel();
        }
      } else {
        renderSimpleType(ip, dtr);
      }
    }
  }

  private void renderSimpleType(IndententationPrinter ip, DataTypeReference dtr) {
    String baseType = getBaseType(dtr);
    String format = getFormatNameFor(dtr);
    
    if (format != null) {
      renderBaseTypeWithFormat(ip, baseType, format);
    } else {
      if (dtr.getBaseType() == BaseType.object) {
        renderObsoletedFileFormat(ip);
      } else {
        renderBaseType(ip, baseType);
      }
    }
  }

  public void renderBaseType(IndententationPrinter ip, String baseType) {
    ip.add("type: ", baseType);
  }

  public void renderBaseTypeWithOptFormat(IndententationPrinter ip, String baseType, BaseTypeFormat format) {
    if (format != null) {
      String fomatStr = BaseTypeToOpenApiType.toOpenApiFormat(format);
      renderBaseTypeWithFormat(ip, baseType, fomatStr);
    } else {
      renderBaseType(ip, baseType);
    }
  }

  private void renderBaseTypeWithFormat(IndententationPrinter ip, String baseType, String format) {
    renderBaseType(ip, baseType);
    ip.add("format: ", format);
  }

  public void renderObsoletedFileFormat(IndententationPrinter ip) {
    ip.add("type: string");
    ip.add("format: binary"); // TODO: Need to check type for base64/binary - assume binary for now
  }

  private static void addSchemaRef(IndententationPrinter ip, DataType value) {
    addSchemaSlugReference(ip, value.getSlug());
  }

  public void addSchemaRef(IndententationPrinter ip, DataTypeReference ref) {
    String slug = ref.getSlug();
    if (slug != null && !slug.isEmpty()) {
      addSchemaSlugReference(ip, slug);
    } else {
      renderObsoletedFileFormat(ip);
    }
  }

  private static void addSchemaSlugReference(IndententationPrinter ip, String slug) {
    ip.add("$ref: \"#/components/schemas/" + slug + "\"");
  }

  	private String getFormatNameFor(DataTypeReference dtr) {
  		String res =  BaseTypeToOpenApiType.toOpenApiFormat(dtr.getBaseTypeFormat());

  		// Overrides for XML types
  		// https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md#dataTypes
  		String label = dtr.getLabel();
		if (KnownXmlType.BASE64_BINARY.getName().equals(label)) {
  			res = "binary";
  		} else if (KnownXmlType.DATE_TIME.getName().equals(label)) {
  			res = "date-time";
  		} else if (KnownXmlType.DATE.getName().equals(label)) {
  			res = "date";
  		} else if (KnownXmlType.TIME.getName().equals(label)) {
  			res = "time";
  		}
  		logger.debug("getFormatName for " + dtr.getBaseType() + "/" + dtr.getBaseTypeFormat()  + " : " + label + " -> " + res);
  		return res;
  }

  private static String getBaseType(DataTypeReference dtr) {
    BaseType baseType = dtr.getBaseType();
    BaseTypeFormat format = dtr.getBaseTypeFormat();

    switch (baseType) {
      case bool:
        return "boolean";
      case number:
        if (BaseTypeFormat.INT32 == format || BaseTypeFormat.INT64 == format) {
          return "integer";
        } else {
          return "number";
        }
      case string:
        return "string";
      case object:
        return "object";
      default:
        throw new IllegalStateException("Called with unhandled type " + baseType);
    }
  }
}
