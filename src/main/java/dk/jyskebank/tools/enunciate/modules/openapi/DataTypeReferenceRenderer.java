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
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.DataTypeReference.ContainerType;
import com.webcohesion.enunciate.api.resources.Parameter;
import com.webcohesion.enunciate.modules.jaxb.api.impl.DataTypeReferenceImpl;
import com.webcohesion.enunciate.modules.jaxb.model.types.MapXmlType;
import com.webcohesion.enunciate.modules.jaxb.model.types.XmlType;

import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper;

public class DataTypeReferenceRenderer {
	private final EnunciateLogger logger;
  
	public DataTypeReferenceRenderer(EnunciateLogger logger) {
		this.logger = logger;
	}
  
  	public void render(IndententationPrinter ip, DataTypeReference dtr, String description) {
	    if (dtr == null) {
	      throw new IllegalStateException("Cannot render null data type");
	    }
    
	    logger.info("  render type for " + dtr.getContainers());
    
	    DataType value = dtr.getValue();
	    List<ContainerType> containers = dtr.getContainers();
	    boolean hasContainers = containers != null && !containers.isEmpty();
	    
	    if (TypeHelper.renderAsSimpleRef(dtr)) {
    		addSchemaRef(ip, value);
    		return;
	    }
	    
	    if (description != null && !description.isEmpty()) {
	      ip.add("description: ", YamlHelper.safeYamlString(description));
	    }

		if (value != null) {
    		for (ContainerType ct : containers) {
    			renderContainer(ip, ct.isMap(), () -> addSchemaRef(ip, value));
    		}
	    } else {
	    	if (hasContainers) {
	    		for (ContainerType ct : containers) {
	    			renderContainer(ip, ct.isMap(), () -> renderType(ip, dtr));
	    		}
	    	} else {
	    		// FIXME: Conversion to DataTypeReferenceImpl must be broken in Jaxb frontend
	    		boolean workaroundRendering = false;
	    		if (dtr instanceof DataTypeReferenceImpl) {
	    			XmlType xmlType = ((DataTypeReferenceImpl)dtr).getXmlType();
	    			if (xmlType instanceof MapXmlType) {
	    				MapXmlType mapXmlType = ((MapXmlType)xmlType);
	    				if (mapXmlType.getKeyType().isSimple() && mapXmlType.getValueType().isSimple()) {
	    					logger.info("Workaround rendering of simple map");
	    					renderContainer(ip, true, () -> ip.add("type: string"));
	    					workaroundRendering = true;
	    				} else {
	    					logger.info("Unhandled map with types " + mapXmlType.getKeyType() + " : " + mapXmlType.getValueType());
	    				}
	    			}
	    		}
	    		
	    		if (!workaroundRendering) {
	    			renderType(ip, dtr);
	    		}
	    	}
	    }
  	}

  private void renderContainer(IndententationPrinter ip, boolean isMap, Runnable valueTypeRendere) {
      if (isMap) {
    	  ip.add("type: object");
    	  ip.add("additionalProperties:");
        } else {
            ip.add("type: array");
            ip.add("items:");
        }
        ip.nextLevel();
        valueTypeRendere.run(); // NOSONAR
        ip.prevLevel();
  }

  public void renderType(IndententationPrinter ip, Parameter parameter) {
	  renderType(ip, OpenApiTypeFormat.from(parameter));
  }
  
  private void renderType(IndententationPrinter ip, DataTypeReference dtr) {
	  renderType(ip, OpenApiTypeFormat.from(dtr));
  }
  
  private void renderType(IndententationPrinter ip, OpenApiTypeFormat tf) {
	  ip.add("type: ", tf.getType());
	  tf.getFormat().ifPresent(f -> ip.add("format: ", f));
  }
  
  public void renderObsoletedFileFormat(IndententationPrinter ip) {
	  logger.info("CALLING obsoleted format");
	  //new Exception("bad code").printStackTrace();
	  renderType(ip, OpenApiTypeFormat.BINARY_STREAM_TYPE);
  }

  private static void addSchemaRef(IndententationPrinter ip, DataType value) {
    addSchemaSlugReference(ip, value.getSlug());
  }

  public void addSchemaRef(IndententationPrinter ip, DataTypeReference ref) {
	  logger.info("Looking at ref %s",  ref.getBaseType());
	  
    String slug = ref.getSlug();
    if (slug != null && !slug.isEmpty()) {
      addSchemaSlugReference(ip, slug);
    } else {
    	renderType(ip, ref);
    }
  }

  private static void addSchemaSlugReference(IndententationPrinter ip, String slug) {
    ip.add("$ref: \"#/components/schemas/" + slug + "\"");
  }
}
