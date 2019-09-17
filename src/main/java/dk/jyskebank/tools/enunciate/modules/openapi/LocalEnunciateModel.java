package dk.jyskebank.tools.enunciate.modules.openapi;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.webcohesion.enunciate.EnunciateContext;
import com.webcohesion.enunciate.api.ApiRegistrationContext;
import com.webcohesion.enunciate.api.ApiRegistry;
import com.webcohesion.enunciate.api.DefaultRegistrationContext;
import com.webcohesion.enunciate.api.resources.Method;
import com.webcohesion.enunciate.api.resources.Resource;
import com.webcohesion.enunciate.api.resources.ResourceApi;
import com.webcohesion.enunciate.api.resources.ResourceGroup;

/**
 * A coherent enunciate model.
 * 
 * JaxrsResourceApi.getResourceGroups returns new objects per call.
 * This makes it really hard to make assumptions and caching.
 * 
 * So let this object be the single interface of the OpenApi plugin
 * to Enunciate model data.
 */
public class LocalEnunciateModel {
	private final List<ResourceApi> resourceApis;
	private final Map<ResourceApi, List<ResourceGroup>> apiToResourceGroups;
	private ApiRegistrationContext registrationContext;

	public LocalEnunciateModel(EnunciateContext context, ApiRegistry apiRegistry) {
		registrationContext = new DefaultRegistrationContext(context);

		resourceApis = apiRegistry.getResourceApis(registrationContext);
		
		apiToResourceGroups = resourceApis.stream()
			.collect(toMap(ra -> ra, ResourceApi::getResourceGroups));
	}
	
	public Stream<ResourceGroup> streamResourceGroups() {
		return resourceApis.stream()
				.flatMap(ra -> getResourceGroups(ra).stream());
	}
	
	public List<ResourceGroup> getResourceGroups(ResourceApi api) {
		List<ResourceGroup> rg = apiToResourceGroups.get(api);
		if (rg == null) {
			throw new IllegalStateException("Did not find entry for " + Objects.toString(api));
		}
		return rg;
	}

	public Resource findParentResource(Method m) {
		return streamResourceGroups()
			.flatMap(rg -> rg.getResources().stream())
			.filter(r -> r.getMethods().contains(m))
			.findFirst()
			.orElseThrow(IllegalStateException::new);
	}
	
	public List<ResourceApi> getResourceApis() {
		return resourceApis;
	}

	public ApiRegistrationContext getRegistrationContext() {
		return registrationContext;
	}
}
