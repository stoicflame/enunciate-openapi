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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.resources.Method;
import com.webcohesion.enunciate.api.resources.Parameter;
import com.webcohesion.enunciate.api.resources.ResourceApi;

/**
 * Provides unique operationIds to methods.
 * 
 * Uses a method's developer label if unique, otherwise
 * appends parameter names.
*/
public class OperationIds {
	private final EnunciateLogger logger;
	private final Map<String, String> uidToOperationid;

	public OperationIds(EnunciateLogger logger, List<ResourceApi> resourceApis) {
		this.logger = logger;
		
		uidToOperationid = preComputeOperationIds(resourceApis);
	}

	public String getOperationId(Method m) {
		String uId = toUniqueId(m);
		String id = uidToOperationid.get(uId);
		logger.debug("Operation ID %s -> %s", uId, id);
		return id;
	}

	private Map<String, String> preComputeOperationIds(List<ResourceApi> resourceApis) {
		Set<Method> allMethods = resourceApis.stream()
				.flatMap(ra -> ra.getResourceGroups().stream())
				.flatMap(rg -> rg.getResources().stream())
				.flatMap(r -> r.getMethods().stream())
				.collect(toSet());
		
		Map<String, List<Method>> preferredLabels = allMethods.stream()
				.collect(groupingBy(Method::getDeveloperLabel));

		return allMethods.stream()
			.collect(toMap(this::toUniqueId, m -> makeOperationId(preferredLabels, m)));
	}
	
	private String makeOperationId(Map<String, List<Method>> preferredLabels, Method m) {
		String assignedLabel = m.getDeveloperLabel();
		List<? extends Parameter> parameters = m.getParameters();

		List<Method> contenders = preferredLabels.get(assignedLabel);
		if (contenders.size() == 1) {
			return assignedLabel;
		}
		
		if (!parameters.isEmpty()) {
			String args = parameters.stream()
				.map(Parameter::getName)
				.collect(joining("_"));
			assignedLabel =  assignedLabel + "_" + args;
		}

		if (m.getRequestEntity() != null) {
			assignedLabel = assignedLabel + "_dto";
		}
		
		logger.debug("Contention on method : %s:%s => %s", m.getSlug(), m.getDeveloperLabel(), assignedLabel);
		
		return assignedLabel;
	}

	/**
	 * A unique id based on slug and parameter names.
	 * 
	 * Necessary since different Method instances are passed around,
	 * representing the same method.
	 */
	private String toUniqueId(Method m) {
		String args = m.getParameters().stream()
				.map(Parameter::getName)
				.collect(joining("_"));
		return m.getSlug() + "_" + args;
	}
}
