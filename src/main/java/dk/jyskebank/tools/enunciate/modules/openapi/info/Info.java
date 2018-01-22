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
package dk.jyskebank.tools.enunciate.modules.openapi.info;

import static dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper.safeYamlString;

import com.webcohesion.enunciate.EnunciateConfiguration;
import com.webcohesion.enunciate.EnunciateConfiguration.Contact;
import com.webcohesion.enunciate.EnunciateConfiguration.License;
import com.webcohesion.enunciate.EnunciateContext;
import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.javac.javadoc.DefaultJavaDocTagHandler;

public class Info {
  @SuppressWarnings("unused") private EnunciateLogger logger;
  private EnunciateConfiguration configuration;
  private EnunciateContext context;

  public Info(EnunciateLogger logger, EnunciateConfiguration configuration, EnunciateContext context) {
    this.logger = logger;
    this.configuration = configuration;
    this.context = context;
  }
  
  public String getTitle() {
    return safeYamlString(configuration.getTitle());
  }
  
  public String getVersion() {
    String version = configuration.getVersion();
    if (version == null) {
      version = "undef";
    }
    return safeYamlString(version);
  }
  
  public String getDescription() {
    String projectDescription = configuration.readDescription(context, true, DefaultJavaDocTagHandler.INSTANCE);
    return safeYamlString(projectDescription == null ? "" : projectDescription);
  }
  
  public String getTermsOfService() {
    return safeYamlString(configuration.getTerms());
  }
  
  public boolean getHasContact() {
    return !configuration.getContacts().isEmpty() && getFirstContact().getName() != null;
  }
  
  public String getContactName() {
    return safeYamlString(getFirstContact().getName());
  }
  
  public String getContactEmail() {
    return safeYamlString(getFirstContact().getEmail());
  }

  public String getContactUrl() {
    return safeYamlString(getFirstContact().getUrl());
  }

  private Contact getFirstContact() {
    return configuration.getContacts().iterator().next();
  }
  
  public boolean getHasLicense() {
    License license = configuration.getApiLicense();
    return license != null && license.getName() != null;
  }
  
  public String getLicenseName() {
    return safeYamlString(configuration.getApiLicense().getName());
  }

  public String getLicenseUrl() {
    return safeYamlString(configuration.getApiLicense().getUrl());
  }
}
