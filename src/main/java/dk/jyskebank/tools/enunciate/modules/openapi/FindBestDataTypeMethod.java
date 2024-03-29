/**
 * Copyright © 2006-2016 Web Cohesion (info@webcohesion.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.jyskebank.tools.enunciate.modules.openapi;

import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.Example;
import com.webcohesion.enunciate.api.resources.Entity;
import com.webcohesion.enunciate.api.resources.MediaTypeDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Heaton
 */
public class FindBestDataTypeMethod {
    private FindBestDataTypeMethod() {
    }

    public static DataTypeReference findBestDataType(Entity entity) {
        if (entity == null) {
            return null;
        }

        return findBestDataType(entity.getMediaTypes());
    }

    public static MediaAndType findBestMediaAndType(Entity entity) {
        if (entity == null) {
            return null;
        }

        return findBestMediaAndType(entity.getMediaTypes());
    }

    private static DataTypeReference findBestDataType(List<? extends MediaTypeDescriptor> mediaTypes) {
        MediaAndType bestMediaType = findBestMediaAndType(mediaTypes);
        if (bestMediaType == null) {
            return null;
        }

        return bestMediaType.type;
    }

    public static MediaAndType findBestMediaAndType(List<? extends MediaTypeDescriptor> mediaTypes) {
        if (mediaTypes == null || mediaTypes.isEmpty()) {
            return null;
        }

        float highestQuality = Float.MIN_VALUE;
        for (MediaTypeDescriptor mediaTypeDescriptor : mediaTypes) {
            highestQuality = Math.max(highestQuality, mediaTypeDescriptor.getQualityOfSourceFactor());
        }

        //first select media types of higher quality
        List<MediaTypeDescriptor> highQualityMediaTypes = new ArrayList<>();
        for (MediaTypeDescriptor mtd : mediaTypes) {
            if (mtd.getQualityOfSourceFactor() >= highestQuality) {
                highQualityMediaTypes.add(mtd);
            }
        }

        return selectMediaTypeByType(highQualityMediaTypes);
    }

    public static class MediaAndType {
        public final MediaTypeDescriptor media;
        public final DataTypeReference type;

        MediaAndType(MediaTypeDescriptor media, DataTypeReference type) {
            this.media = media;
            this.type = type;
        }
    }

    private static MediaAndType selectMediaTypeByType(List<MediaTypeDescriptor> highQualityMediaTypes) {
        //return the first JSON-based media type.
        for (MediaTypeDescriptor mediaTypeDescriptor : highQualityMediaTypes) {
            if (mediaTypeDescriptor.getSyntax() != null && mediaTypeDescriptor.getSyntax().toLowerCase().contains("json")) {
                return new MediaAndType(mediaTypeDescriptor, mediaTypeDescriptor.getDataType());
            }
        }

        //return the first text-based media type.
        for (MediaTypeDescriptor mediaTypeDescriptor : highQualityMediaTypes) {
            String mt = mediaTypeDescriptor.getMediaType();
            if (mt != null) {
                mt = mt.toLowerCase();
                if (mt.startsWith("text") || mt.endsWith("json") || mt.endsWith("xml")) {
                    DataTypeReference dataType = mediaTypeDescriptor.getDataType();
                    if (dataType == null || dataType.getValue() == null) {
                        dataType = GENERIC_STRING_BASED_DATATYPE_REFERENCE;
                    }
                    return new MediaAndType(mediaTypeDescriptor, dataType);
                }
            }
        }

        //didn't find any text-based media types; try any other media types.
        for (MediaTypeDescriptor mediaTypeDescriptor : highQualityMediaTypes) {
            if (mediaTypeDescriptor.getDataType() != null) {
                return new MediaAndType(mediaTypeDescriptor, mediaTypeDescriptor.getDataType());
            }
        }

        return null;
    }

    static final DataTypeReference GENERIC_STRING_BASED_DATATYPE_REFERENCE = new DataTypeReference() {
        @Override
        public String getLabel() {
            return null;
        }

        @Override
        public String getSlug() {
            return null;
        }

        @Override
        public List<ContainerType> getContainers() {
            return null;
        }

        @Override
        public DataType getValue() {
            return null;
        }

        @Override
        public BaseType getBaseType() {
            return BaseType.string;
        }

        @Override
        public Example getExample() {
            return null;
        }

        @Override
        public String getBaseTypeFormat() {
            return null;
        }
    };

}