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

import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.Example;
import com.webcohesion.enunciate.api.resources.Parameter;
import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper.safeYamlString;

public class Response {
    @SuppressWarnings("unused")
    private final EnunciateLogger logger;
    private final DataTypeReferenceRenderer dataTypeReferenceRenderer;
    private final int code;
    private final List<Parameter> headers;
    private final String description;
    private final List<MediaTypeExample> mediaTypeExampleList = new ArrayList<>();

    public Response(EnunciateLogger logger, DataTypeReferenceRenderer dataTypeReferenceRenderer, int code, List<Parameter> headers, String description) {
        this.logger = logger;
        this.dataTypeReferenceRenderer = dataTypeReferenceRenderer;
        this.code = code;
        this.headers = headers;
        this.description = description;

        // TODO: Render headers
    }

    public String getCode() {
        return safeYamlString(Integer.toString(code));
    }

    public String getDescription() {
        return safeYamlString(description);
    }

    public List<MediaTypeExample> getMediaTypeExampleList() {
        return mediaTypeExampleList;
    }

    public boolean getHasData() {
        return !mediaTypeExampleList.isEmpty();
    }

    public class MediaTypeExample {
        private final String mediaType;
        private final ResponseDataTypeRenderer renderer;
        private final Optional<ExampleRenderer> exampleRenderer;

        public MediaTypeExample(String mediaType, DataTypeReference dataType, Optional<Example> optionalExample) {
            this.mediaType = mediaType;
            this.renderer = new ResponseDataTypeRenderer(logger, dataTypeReferenceRenderer, dataType, description);
            this.exampleRenderer = optionalExample.map(e -> new ExampleRenderer(logger, e, dataTypeReferenceRenderer.doRemoveObjectPrefix()));
        }

        public String getMediaType() {
            return safeYamlString(mediaType);
        }

        public boolean getHasExample() {
            return exampleRenderer.isPresent();
        }

        public ResponseDataTypeRenderer getRenderDataType() {
            return renderer;
        }

        public ExampleRenderer getRenderExample() {
            return exampleRenderer.orElse(null);
        }
    }
}
