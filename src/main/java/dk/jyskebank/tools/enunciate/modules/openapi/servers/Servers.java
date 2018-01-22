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
package dk.jyskebank.tools.enunciate.modules.openapi.servers;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.configuration.HierarchicalConfiguration;

import com.webcohesion.enunciate.EnunciateConfiguration;
import com.webcohesion.enunciate.EnunciateLogger;

public class Servers {
  private final EnunciateLogger logger;
  private final List<Server> servers;
  
  public Servers(EnunciateLogger logger, EnunciateConfiguration enunciateConfig, HierarchicalConfiguration moduleConfig) {
    this.logger = logger;
    String basePath = getBasePath(enunciateConfig, moduleConfig);
    if (basePath != null) {
      Server defaultServer = new Server(basePath, Optional.empty(), Collections.emptyList());
      servers = Arrays.asList(defaultServer);
    } else {
      servers = Collections.emptyList();
    }
  }
  
  public boolean getIsEmpty() {
    return servers.isEmpty();
  }
  
  public List<Server> getServers() {
    return servers;
  }

  private String getBasePath(EnunciateConfiguration enunciateConfig, HierarchicalConfiguration moduleConfig) {
    String basePath = moduleConfig.getString("[@basePath]", null);
    
    logger.debug("@basePath %s",  basePath);

    if (basePath == null) {
      String root = enunciateConfig.getApplicationRoot();
      logger.debug("appRoot %s",  root);
      
      if (root != null) {
        try {
          URI uri = URI.create(root);
          basePath = uri.getPath();
        }
        catch (IllegalArgumentException e) {
          basePath = null;
        }
      }

      while (basePath != null && basePath.endsWith("/")) {
        basePath = basePath.substring(0, basePath.length() - 1);
      }
    }

    logger.debug("basePath out %s",  basePath);

    return basePath;
  }
}
