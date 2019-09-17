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
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.resources.Method;
import com.webcohesion.enunciate.api.resources.Parameter;
import com.webcohesion.enunciate.api.resources.Resource;

import dk.jyskebank.tools.enunciate.modules.openapi.LocalEnunciateModel;

/**
 * Provides unique operationIds to methods.
 * 
 * Uses a method's developer label if unique, otherwise
 * appends parameter names.
*/
public class OperationIds {
	private final EnunciateLogger logger;
	private final Map<Method, String> uidToOperationid;
	private final LocalEnunciateModel model;

	public OperationIds(EnunciateLogger logger, LocalEnunciateModel model) {
		this.logger = logger;
		this.model = model;
		
		uidToOperationid = preComputeOperationIds();
	}
	
	public String getOperationId(Method m) {
		String id = uidToOperationid.get(m);
		logger.debug("Operation ID %s -> %s", Objects.toString(m), id);
		return id;
	}

	private Map<Method, String> preComputeOperationIds() {
		Set<Method> allMethods = model.streamResourceGroups()
				.flatMap(rg -> rg.getResources().stream())
				.flatMap(r -> r.getMethods().stream())
				.collect(toSet());

		Map<String, List<Method>> preferredLabels = allMethods.stream()
				.collect(groupingBy(Method::getDeveloperLabel));

		return allMethods.stream()
			.collect(toMap(m -> m, m -> makeOperationId(preferredLabels, m)));
	}

	// Try to keep operationId simple, but add more info if required
	// Fall-back is to use label+hash(path)
	private String makeOperationId(Map<String, List<Method>> preferredLabels, Method m) {
		String wantedLabel = m.getDeveloperLabel();

		List<Method> contenders = preferredLabels.get(wantedLabel);
		if (contenders.size() == 1) {
			return wantedLabel;
		}

		String assignedLabel;
		String idWithArgNames = argumentNames(m);
		String idWithArgNameTypes = argumentNameTypes(m);

		if (isNameUnique(contenders, this::argumentNames, idWithArgNames)) {
			assignedLabel = wantedLabel + "_" + idWithArgNames;
		} else if (isNameUnique(contenders, this::argumentNameTypes, idWithArgNameTypes)) {
			assignedLabel = wantedLabel + "_" + idWithArgNameTypes;
		} else {
			assignedLabel = wantedLabel + "_" + getFallbackId(m);
		}
		logger.debug("Contention on method : %s:%s => %s", m.getSlug(), m.getDeveloperLabel(), assignedLabel);
		
		return assignedLabel;
	}

	private String getFallbackId(Method m) {
		Resource r = model.findParentResource(m);
		String uniq = r.getPath() + m.getHttpMethod();
		return Integer.toHexString(uniq.hashCode());
	}

	private boolean isNameUnique(List<Method> contenders, Function<Method, String> strify, String wantedId) {
		return contenders.stream()
			.map(strify)
			.filter(wantedId::equals)
			.count() == 1;
	}
	
	private String argumentNames(Method m) {
		return m.getParameters().stream()
				.map(Parameter::getName)
				.collect(joining("_"));
	}

	private String argumentNameTypes(Method m) {
		return m.getParameters().stream()
				.map(p -> p.getName() + "_" + p.getTypeName())
				.collect(joining("_"));
	}
}
