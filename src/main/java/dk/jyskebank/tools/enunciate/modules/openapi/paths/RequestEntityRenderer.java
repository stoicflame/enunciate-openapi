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

import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.resources.MediaTypeDescriptor;
import com.webcohesion.enunciate.api.resources.Method;
import dk.jyskebank.tools.enunciate.modules.freemarker.Typed1ArgTemplateMethod;
import dk.jyskebank.tools.enunciate.modules.openapi.DataTypeReferenceRenderer;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndentationPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper.safeYamlString;

public class RequestEntityRenderer extends Typed1ArgTemplateMethod<String, String> {
    private final boolean hasMedia;
    private final String mediaWithFallback;
    private final DataTypeReferenceRenderer dataTypeReferenceRenderer;
    private final List<MediaType> mediaTypeList;

    public RequestEntityRenderer(DataTypeReferenceRenderer dataTypeReferenceRenderer, Method method) {
        super(String.class);
        this.dataTypeReferenceRenderer = dataTypeReferenceRenderer;

        mediaTypeList = new ArrayList<>();

        if (method.getRequestEntity() != null) {
            List<? extends MediaTypeDescriptor> mediaTypes = method.getRequestEntity().getMediaTypes();
            mediaTypeList.addAll(mediaTypes.stream()
                    .map(mediaTypeDescriptor -> new MediaType(mediaTypeDescriptor.getMediaType(), mediaTypeDescriptor.getDataType()))
                    .collect(Collectors.toList()));
        }

        hasMedia = !mediaTypeList.isEmpty();
        mediaWithFallback = "*/*";
    }

    @Override
    protected String exec(String nextLineIndent) {
        IndentationPrinter ip = new IndentationPrinter(nextLineIndent, dataTypeReferenceRenderer.doRemoveObjectPrefix());

        if (!hasMedia) {
            ip.add(safeYamlString(mediaWithFallback), ":");
            ip.nextLevel();
            ip.add("schema:");
            ip.nextLevel();
            dataTypeReferenceRenderer.renderObsoletedFileFormat(ip);
            ip.prevLevel();
            ip.prevLevel();
        } else {
            mediaTypeList.forEach(mediaType -> {
                ip.add(safeYamlString(mediaType.getMedia()), ":");
                ip.nextLevel();
                ip.add("schema:");
                ip.nextLevel();
                dataTypeReferenceRenderer.addSchemaRef(ip, mediaType.getType());
                ip.prevLevel();
                ip.prevLevel();
            });
        }
        return ip.toString();
    }

    public class MediaType {
        private final String media;
        private final DataTypeReference type;

        public MediaType(String media, DataTypeReference type) {
            this.media = media;
            this.type = type;
        }

        public String getMedia() {
            return media;
        }

        public DataTypeReference getType() {
            return type;
        }
    }
}
