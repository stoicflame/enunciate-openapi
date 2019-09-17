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
import java.util.List;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.PathSummary;
import com.webcohesion.enunciate.api.resources.Resource;
import com.webcohesion.enunciate.api.resources.ResourceGroup;

import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.LocalEnunciateModel;
import dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer;

public class Paths {
  @SuppressWarnings("unused") private EnunciateLogger logger;
  private List<Endpoint> endpoints = new ArrayList<>();

  public Paths(EnunciateLogger logger, DataTypeReferenceRenderer dataTypeReferenceRenderer, ObjectTypeRenderer objectTypeRenderer, OperationIds operationIds, LocalEnunciateModel model) {
    this.logger = logger;
    
    model.streamResourceGroups()
    	.forEach(resourceGroup -> {
        for (PathSummary pathSummary : resourceGroup.getPaths()) {
          List<Resource> resources = findResourcesForPath(resourceGroup, pathSummary.getPath());
          
          endpoints.add(new Endpoint(logger, dataTypeReferenceRenderer, objectTypeRenderer,  operationIds, resources, resourceGroup, pathSummary));
        }
      });
  }
  
  private List<Resource> findResourcesForPath(ResourceGroup group, String path) {
    List<Resource> resources = new ArrayList<>();
    for (Resource r : group.getResources()) {
      if (r.getPath().equals(path)) {
        resources.add(r);
      }
    }
    return resources;
  }

  public boolean getIsEmpty() {
    return endpoints.isEmpty();
  }
  
  public List<Endpoint> getEndpoints() {
    return endpoints;
  }
}
