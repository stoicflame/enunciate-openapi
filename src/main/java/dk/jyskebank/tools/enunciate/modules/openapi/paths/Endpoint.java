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
import java.util.List;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.PathSummary;
import com.webcohesion.enunciate.api.resources.Method;
import com.webcohesion.enunciate.api.resources.Resource;
import com.webcohesion.enunciate.api.resources.ResourceApi;
import com.webcohesion.enunciate.api.resources.ResourceGroup;

import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;

public class Endpoint {
  private ResourceGroup resourceGroup;
  private PathSummary pathSummary;
  private List<Operation> operations = new ArrayList<>();

  public Endpoint(EnunciateLogger logger, DataTypeReferenceRenderer dataTypeReferenceRenderer, OperationIds operationIds, List<Resource> resources, ResourceApi resourceApi, ResourceGroup resourceGroup, PathSummary pathSummary) {
    this.resourceGroup = resourceGroup;
    this.pathSummary = pathSummary;
    
    for (Resource resource : resources) {
      for (Method m : resource.getMethods()) {
        operations.add(new Operation(logger, dataTypeReferenceRenderer, operationIds, m, resourceGroup));
      }
    }    
  }

  public String getPath() {
    return safeYamlString(pathSummary.getPath());
  }
  
  public String getResourceGroupTag() {
    return safeYamlString(resourceGroup.getLabel());
  }
  
  public boolean getResourceGroupDeprecated() {
    return resourceGroup.getDeprecated() != null;
  }
  
  public List<Operation> getOperations() {
    return operations;
  }
}
