/**
 * Copyright © 2017-2018 Jyske Bank
 * Copyright © 2006-2016 Web Cohesion (info@webcohesion.com)
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

import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.webcohesion.enunciate.EnunciateContext;
import com.webcohesion.enunciate.EnunciateException;
import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.ApiRegistrationContext;
import com.webcohesion.enunciate.api.ApiRegistry;
import com.webcohesion.enunciate.api.DefaultRegistrationContext;
import com.webcohesion.enunciate.api.InterfaceDescriptionFile;
import com.webcohesion.enunciate.api.datatype.Syntax;
import com.webcohesion.enunciate.api.resources.ResourceApi;
import com.webcohesion.enunciate.api.services.ServiceApi;
import com.webcohesion.enunciate.artifacts.FileArtifact;
import com.webcohesion.enunciate.module.ApiFeatureProviderModule;
import com.webcohesion.enunciate.module.ApiRegistryAwareModule;
import com.webcohesion.enunciate.module.ApiRegistryProviderModule;
import com.webcohesion.enunciate.module.BasicGeneratingModule;
import com.webcohesion.enunciate.module.DependencySpec;
import com.webcohesion.enunciate.module.DocumentationProviderModule;
import com.webcohesion.enunciate.module.EnunciateModule;
import com.webcohesion.enunciate.util.freemarker.FileDirective;

import dk.jyskebank.tools.enunciate.modules.openapi.components.Components;
import dk.jyskebank.tools.enunciate.modules.openapi.info.Info;
import dk.jyskebank.tools.enunciate.modules.openapi.paths.OperationIds;
import dk.jyskebank.tools.enunciate.modules.openapi.paths.Paths;
import dk.jyskebank.tools.enunciate.modules.openapi.servers.Servers;
import freemarker.cache.URLTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * <h1>OpenAPI Module</h1>
 * Based on Swagger Module by Ryan Heaton.
 */
public class OpenApiModule extends BasicGeneratingModule implements ApiFeatureProviderModule, ApiRegistryAwareModule, ApiRegistryProviderModule, DocumentationProviderModule {
  private static final String OPENAPI_MODULENAME = "openapi";

  private static final List<String> DEPENDENCY_MODULES = Arrays.asList("jackson", "jackson1", "jaxb", "jaxrs", "jaxws", "spring-web");

  private ApiRegistry apiRegistry;
  private File defaultDocsDir;
  private String defaultDocsSubdir = "ui";

  @Override
  public String getName() {
    return OPENAPI_MODULENAME;
  }

  @Override
  public void setApiRegistry(ApiRegistry registry) {
    this.apiRegistry = registry;
  }

  @Override
  public List<DependencySpec> getDependencySpecifications() {
    return Collections.singletonList(new DependencySpec() {
      @Override
      public boolean accept(EnunciateModule module) {
        return DEPENDENCY_MODULES.contains(module.getName());
      }

      @Override
      public boolean isFulfilled() {
        return true;
      }

      @Override
      public String toString() {
        return "all api registry provider modules";
      }
    });
  }

  /**
   * The URL to "openapi.fmt".
   *
   * @return The URL to "openapi.fmt".
   * @throws MalformedURLException if the template location could not be represented as an URL.
   */
  protected URL getTemplateURL() throws MalformedURLException {
    String template = getFreemarkerProcessingTemplate();
    if (template != null) {
      return enunciate.getConfiguration().resolveFile(template).toURI().toURL();
    }
    else {
      return OpenApiModule.class.getResource("openapi.fmt");
    }
  }

  @Override
  public void call(EnunciateContext context) {
    //no-op; work happens with the swagger interface description.

    File docsDir = getDocsDir();
    FileArtifact openapiArtifact = new FileArtifact(getName(), OPENAPI_MODULENAME, docsDir);
    openapiArtifact.setPublic(false);
    
    warn("Export artifact %s %s %s",  getName(), OPENAPI_MODULENAME, docsDir.toString());
    
    enunciate.addArtifact(openapiArtifact);
    
    try {
      ApiRegistrationContext registrationContext = new DefaultRegistrationContext(context);
      List<ResourceApi> resourceApis = apiRegistry.getResourceApis(registrationContext);
      new OpenApiInterfaceDescription(resourceApis, registrationContext).writeToFolder(docsDir);
    } catch (IOException e) {
      throw new EnunciateException(e);
    }
  }

  @Override
  public ApiRegistry getApiRegistry() {
    return new ApiRegistry() {
      @Override
      public List<ServiceApi> getServiceApis(ApiRegistrationContext context) {
        return Collections.emptyList();
      }

      @Override
      public List<ResourceApi> getResourceApis(ApiRegistrationContext context) {
        return Collections.emptyList();
      }

      @Override
      public Set<Syntax> getSyntaxes(ApiRegistrationContext context) {
        return Collections.emptySet();
      }

      @Override
      public InterfaceDescriptionFile getSwaggerUI() {
        ApiRegistrationContext registrationContext = new DefaultRegistrationContext(context);
        List<ResourceApi> resourceApis = apiRegistry.getResourceApis(registrationContext);

        if (resourceApis == null || resourceApis.isEmpty()) {
          info("No resource APIs registered: OpenApi UI will not be generated.");
        }

        return new OpenApiInterfaceDescription(resourceApis, registrationContext);
      }
    };
  }

  private class OpenApiInterfaceDescription implements InterfaceDescriptionFile {
    private final List<ResourceApi> resourceApis;
    private final ApiRegistrationContext apiRegistrationContext;

    public OpenApiInterfaceDescription(List<ResourceApi> resourceApis, ApiRegistrationContext context) {
      this.resourceApis = resourceApis;
      this.apiRegistrationContext = context;
    }

    @Override
    public String getHref() {
      return getDocsSubdir() + "/index.html";
    }

    @Override
    public void writeTo(File srcDir) throws IOException {
      String subdir = getDocsSubdir();
      writeToFolder(subdir != null ? new File(srcDir, subdir) : srcDir);
    }
    
    protected void writeToFolder(File dir) throws IOException {
      EnunciateLogger logger = enunciate.getLogger();
      DataTypeReferenceRenderer dataTypeReferenceRenderer = new DataTypeReferenceRenderer(logger, doRemoveObjectPrefix());
      ObjectTypeRenderer objectTypeRenderer = new ObjectTypeRenderer(logger, dataTypeReferenceRenderer, getPassThroughAnnotations(), doRemoveObjectPrefix(), excludeExamples());
      
      dir.mkdirs();
      Map<String, Object> model = new HashMap<>();
      model.put("info", new Info(logger, enunciate.getConfiguration(), context)); 
      model.put("paths", new Paths(logger, dataTypeReferenceRenderer, objectTypeRenderer, new OperationIds(), enunciate.getConfiguration(), context, resourceApis));
      model.put("servers", new Servers(logger, enunciate.getConfiguration(), config));
      Set<Syntax> syntaxes = apiRegistry.getSyntaxes(apiRegistrationContext);
      model.put("components", new Components(logger, objectTypeRenderer, syntaxes));
      model.put("file", new FileDirective(dir, logger));
      
      buildBase(dir);
      try {
        processTemplate(getTemplateURL(), model);
      }
      catch (TemplateException e) {
        throw new EnunciateException(e);
      }
    }

    private boolean excludeExamples() {
      return Boolean.parseBoolean(config.getString("[@excludeExamples]"));
    }

	  /**
	   * By default all model objects are prefixed with "json_" in output file openapi.yml.
	   * When this configuration property has value <code>true</code>, then this prefix is omitted.
	   *
	   * Having the prefix caused problems with client code generation.
	   */
	  private boolean doRemoveObjectPrefix() {
	    return Boolean.parseBoolean(config.getString("[@removeObjectPrefix]"));
	  }
  }

  protected String getHost() {
    String host = config.getString("[@host]", null);

    if (host == null) {
      String root = enunciate.getConfiguration().getApplicationRoot();
      if (root != null) {
        try {
          URI uri = URI.create(root);
          host = uri.getHost();
          if (uri.getPort() > 0) {
            host += ":" + uri.getPort();
          }
        }
        catch (IllegalArgumentException e) {
          host = null;
        }
      }
    }

    return host;
  }

  /**
   * Processes the specified template with the given model.
   *
   * @param templateURL The template URL.
   * @param model       The root model.
   * @return expanded template
   * @throws IOException if IO failed.
   * @throws TemplateException if template expansion failed.
   */
  public String processTemplate(URL templateURL, Object model) throws IOException, TemplateException {
    debug("Processing template %s.", templateURL);
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
    configuration.setLocale(new Locale("en", "US"));

    configuration.setTemplateLoader(new URLTemplateLoader() {
      protected URL getURL(String name) {
        try {
          return new URL(name);
        }
        catch (MalformedURLException e) {
          return null;
        }
      }
    });

    configuration.setTemplateExceptionHandler(new TemplateExceptionHandler() {
      public void handleTemplateException(TemplateException templateException, Environment environment, Writer writer) throws TemplateException {
        throw templateException;
      }
    });

    configuration.setLocalizedLookup(false);
    configuration.setDefaultEncoding("UTF-8");
    configuration.setObjectWrapper(new OpenApiUIObjectWrapper());
    Template template = configuration.getTemplate(templateURL.toString());
    StringWriter unhandledOutput = new StringWriter();
    template.process(model, unhandledOutput);
    unhandledOutput.close();
    return unhandledOutput.toString();
  }

  /**
   * Builds the base output directory.
   * 
   * @param buildDir directory to write the Swagger UI to.
   * 
   * @throws IOException if IO failed.
   */
  protected void buildBase(File buildDir) throws IOException {
    if (isSkipBase()) {
      debug("Not including base documentation.");
      return;
    }
    
    String base = getBase();
    if (base == null) {
      InputStream discoveredBase = OpenApiModule.class.getResourceAsStream("/META-INF/enunciate/openapi-base.zip");
      if (discoveredBase != null) {
        debug("Discovered documentation base at /META-INF/enunciate/openapi-base.zip");
        enunciate.unzip(discoveredBase, buildDir);
      } else {
        debug("Default base to be used for openapi base.");
        enunciate.unzip(loadDefaultBase(), buildDir);
      }
    }
    else {
      File baseFile = enunciate.getConfiguration().resolveFile(base);
      if (baseFile.isDirectory()) {
        debug("Directory %s to be used as the documentation base.", baseFile);
        enunciate.copyDir(baseFile, buildDir);
      }
      else {
        debug("Zip file %s to be extracted as the documentation base.", baseFile);
        enunciate.unzip(new FileInputStream(baseFile), buildDir);
      }
    }
  }

  /**
   * Loads the default base for the swagger ui.
   *
   * @return The default base for the swagger ui.
   */
  protected InputStream loadDefaultBase() {
    String resourceName = "/openapi-swagger-ui.zip";
    InputStream swaggerUiStream = OpenApiModule.class.getResourceAsStream(resourceName);
    if (swaggerUiStream == null) {
      throw new IllegalStateException("Did not find " + resourceName + " in classpath");
    }
    return swaggerUiStream;
  }

  /**
   * The cascading stylesheet to use instead of the default.  This is ignored if the 'base' is also set.
   *
   * @return The cascading stylesheet to use.
   */
  public String getCss() {
    return config.getString("[@css]", null);
  }

  public boolean isSkipBase() {
    return Boolean.parseBoolean(config.getString("[@skipBase]", Boolean.FALSE.toString()));
  }

  
  public String getFreemarkerProcessingTemplate() {
    return config.getString("[@freemarkerProcessingTemplate]", null);
  }

  /**
   * The OpenAPI "base".  The OpenAPI base is the initial contents of the directory
   * where the swagger-ui will be output.  Can be a zip file or a directory.
   *
   * @return The documentation "base".
   */
  public String getBase() {
    return config.getString("[@base]", null);
  }

  public Set<String> getPassThroughAnnotations() {
    String arg = config.getString("[@passThroughAnnotations]", "");
    return Stream.of(arg.split(","))
      .map(String::trim)
      .filter(s -> !s.isEmpty())
      .collect(toSet());
  }
  
  public Set<String> getFacetIncludes() {
    List<Object> includes = config.getList("facets.include[@name]");
    Set<String> facetIncludes = new TreeSet<>();
    for (Object include : includes) {
      facetIncludes.add(String.valueOf(include));
    }
    return facetIncludes;
  }

  public Set<String> getFacetExcludes() {
    List<Object> excludes = config.getList("facets.exclude[@name]");
    Set<String> facetExcludes = new TreeSet<>();
    for (Object exclude : excludes) {
      facetExcludes.add(String.valueOf(exclude));
    }
    return facetExcludes;
  }
  
  public File getDocsDir() {

    String docsDir = config.getString("[@docsDir]");
    if (docsDir != null) {
      return resolveFile(docsDir);
    }
    
    return defaultDocsDir != null ? defaultDocsDir : new File(enunciate.getBuildDir(), getName());
  }

  public String getDocsSubdir() {
    return config.getString("[@docsSubdir]", defaultDocsSubdir);
  }

  @Override
  public void setDefaultDocsDir(File docsDir) {
    this.defaultDocsDir = docsDir;
  }

  @Override
  public void setDefaultDocsSubdir(String defaultDocsSubdir) {
    this.defaultDocsSubdir = defaultDocsSubdir;
  }
}
