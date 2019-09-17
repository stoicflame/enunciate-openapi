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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.Example;
import com.webcohesion.enunciate.api.resources.Entity;
import com.webcohesion.enunciate.api.resources.MediaTypeDescriptor;
import com.webcohesion.enunciate.api.resources.Method;
import com.webcohesion.enunciate.api.resources.Parameter;
import com.webcohesion.enunciate.api.resources.ResourceGroup;
import com.webcohesion.enunciate.api.resources.StatusCode;

import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.FindBestDataTypeMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.FindBestDataTypeMethod.MediaAndType;

public class Operation {
  private static final String FALLBACK_SUCCESS_MEDIA_TYPE = "*/*";
  private static final String UNDECLARED_SUCCESS_MEDIA_TYPE = "*/*";
  private static final Collection<String> DEFAULT_201_METHODS = Collections.singletonList("POST");
  private static final Collection<String> DEFAULT_204_METHODS = Arrays.asList("PATCH", "PUT", "DELETE");

  private final ResourceGroup resourceGroup;
  private final Method method;
  private final List<Param> parameters = new ArrayList<>();
  private final List<Response> responses = new ArrayList<>();
  private final OperationIds operationIds;
  private final RequestEntityRenderer entityRenderer;
  private final DataTypeReferenceRenderer dataTypeReferenceRenderer;

  public Operation(EnunciateLogger logger, DataTypeReferenceRenderer dataTypeReferenceRenderer, ObjectTypeRenderer objectTypeRenderer, OperationIds operationIds, Method method, ResourceGroup resourceGroup) {
    this.dataTypeReferenceRenderer = dataTypeReferenceRenderer;
    this.operationIds = operationIds;
    this.method = method;
    this.resourceGroup = resourceGroup;
    entityRenderer = new RequestEntityRenderer(dataTypeReferenceRenderer, method);
    
    for (Parameter parameter : method.getParameters()) {
      parameters.add(new Param(logger, dataTypeReferenceRenderer, objectTypeRenderer, parameter));
    }
    
    computeResponses(logger);
  }

  public String getHttpMethod() {
    return method.getHttpMethod().toLowerCase();
  }
  
  public String getDescription() {
    return safeYamlString(method.getDescription());
  }
  
  public String getSummary() {
    String summary = method.getDescription().replaceAll("(?s)[.].*", ".");
    return safeYamlString(summary);
  }
  
  public String getDeprecated() {
    return Boolean.toString(method.getDeprecated() != null || resourceGroup.getDeprecated() != null);
  }
  
  public String getOperationId() {
    return operationIds.getOperationId(method);
  }
  
  public boolean getHasParameters() {
    return !parameters.isEmpty();
  }
  
  public List<Param> getParameters() {
    return parameters;
  }
  
  public boolean getHasEntity() {
	
	  return httpMethodAllowsEntity()
			  && method.getRequestEntity() != null;
  }
  
  private boolean httpMethodAllowsEntity() {
	  String httpMethod = getHttpMethod();
	  return !"delete".equals(httpMethod) && !"get".equals(httpMethod);
  }

  public String getIsEntityRequired() {
    // TODO: this is probably a bad assumption
    return Boolean.TRUE.toString();
  }
  
  public String getEntityDescription() {
    String description = method.getRequestEntity().getDescription();
    return safeYamlString(description == null ? "" : description);
  }
  
  public RequestEntityRenderer getRenderEntity() {
    return entityRenderer;
  }
  
  public List<Response> getResponses() {
    return responses;
  }

  private void computeResponses(EnunciateLogger logger) {
    @SuppressWarnings("unchecked")
    List<Parameter> successHeaders = (List<Parameter>)method.getResponseHeaders();
    Entity responseEntity = method.getResponseEntity();
    DataTypeReference successDataType = FindBestDataTypeMethod.findBestDataType(responseEntity);
    boolean successResponseFound = false;
    if (method.getResponseCodes() != null) {
      for (StatusCode code : method.getResponseCodes()) {
        boolean successResponse = code.getCode() >= 200 && code.getCode() < 300;
        
        MediaAndType mediaAndType = FindBestDataTypeMethod.findBestMediaAndType(code.getMediaTypes());
        
        DataTypeReference dataType = mediaAndType == null ? null : mediaAndType.type;
        dataType = dataType == null && successResponse ? successDataType : dataType;
        List<Parameter> headers = successResponse ? successHeaders : Collections.<Parameter>emptyList();
        
        String mediaType = getResponseMediaType(successResponse, mediaAndType, responseEntity);
        Optional<Example> optionalExample = getExampleForMediaType(responseEntity, mediaType);
        
        responses.add(new Response(logger, dataTypeReferenceRenderer, code.getCode(), mediaType, dataType, headers, code.getCondition(), optionalExample));
        successResponseFound |= successResponse;
      }
    }

    if (!successResponseFound) {
      int code = DEFAULT_201_METHODS.contains(method.getHttpMethod().toUpperCase()) ? 201 : DEFAULT_204_METHODS.contains(method.getHttpMethod().toUpperCase()) ? 204 : 200;
      String description = responseEntity != null ? responseEntity.getDescription() : "Success";
      responses.add(new Response(logger, dataTypeReferenceRenderer, code, FALLBACK_SUCCESS_MEDIA_TYPE, successDataType, successHeaders, description, Optional.empty()));
    }
  }
  
  private String getResponseMediaType(boolean successResponse, MediaAndType mediaAndType, Entity responseEntity) {
    if (mediaAndType != null) {
      return mediaAndType.media.getMediaType();
    }
    
    if (successResponse && responseEntity != null) {
      List<? extends MediaTypeDescriptor> mediaTypes = responseEntity.getMediaTypes();
      if (!mediaTypes.isEmpty()) {
        return mediaTypes.iterator().next().getMediaType();
      }
    }
    return UNDECLARED_SUCCESS_MEDIA_TYPE;
  }

  private Optional<Example> getExampleForMediaType(Entity entity, String mediaType) {
    if (entity == null) {
      return Optional.empty();
    }
    List<? extends MediaTypeDescriptor> mts = entity.getMediaTypes();
    if (mts != null) {
      for (MediaTypeDescriptor mt : mts) {
        if (mediaType.equals(mt.getMediaType())) {
          DataTypeReference dataType = mt.getDataType();
          if (dataType != null && dataType.getBaseType() == BaseType.object) {
            DataType value = dataType.getValue();
            if (value != null) {
              Example example = value.getExample();
              if (example != null) {
                return Optional.of(example);
              }
            }
          }
        }
      }
    }
    return Optional.empty();
  }
}
