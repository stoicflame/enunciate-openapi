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
package dk.jyskebank.tools.enunciate.modules.openapi.security;

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.configuration.HierarchicalConfiguration;

import com.webcohesion.enunciate.EnunciateConfiguration;
import com.webcohesion.enunciate.EnunciateLogger;

import dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer;

public class SecurityRequirement {

	private final EnunciateLogger logger;
	private final List<SecurityScheme> schemes;

	public SecurityRequirement(EnunciateLogger logger, EnunciateConfiguration enunciateConfig,
			ObjectTypeRenderer objectTypeRenderer, HierarchicalConfiguration moduleConfig) {
		this.logger = logger;

		schemes = new ArrayList<>();
		for (String securityScheme : getSecuritySchemes(moduleConfig)) {
			schemes.add(getSecuritySchema(securityScheme, objectTypeRenderer, moduleConfig));
		}
	}

	private SecurityScheme getSecuritySchema(String securityScheme, ObjectTypeRenderer objectTypeRenderer,
			HierarchicalConfiguration moduleConfig) {
		// FIXME: this is just very very basic implementation! here we have to think about how to configure and initialize different security schemes!
		if ("basicAuth".equals(securityScheme)) {
			return new SecurityScheme(logger, objectTypeRenderer, "basicAuth", null, Optional.empty(), "http", null,
					"basic", null, null, null);
		} else if ("bearerAuth".equals(securityScheme)) {
			return new SecurityScheme(logger, objectTypeRenderer, "bearerAuth", null, Optional.empty(), "http", null,
					"bearer", null, null, null);
		}
		return null;
	}

	public boolean getIsEmpty() {
		return schemes.isEmpty();
	}

	public List<SecurityScheme> getSchemes() {
		return schemes;
	}

	private Set<String> getSecuritySchemes(HierarchicalConfiguration config) {
		String arg = config.getString("[@securitySchemes]", "");
		return Stream.of(arg.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(toSet());
	}

}
