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
package dk.jyskebank.tools.enunciate.modules.openapi;

import java.util.List;

import com.google.common.collect.Lists;
import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.DataTypeReference.ContainerType;
import com.webcohesion.enunciate.api.resources.Parameter;
import com.webcohesion.enunciate.modules.jaxb.api.impl.DataTypeReferenceImpl;
import com.webcohesion.enunciate.modules.jaxb.model.types.MapXmlType;
import com.webcohesion.enunciate.modules.jaxb.model.types.XmlType;

import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndentationPrinter;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper;

public class DataTypeReferenceRenderer {
    private final EnunciateLogger logger;
    private boolean removeObjectPrefix;

    public DataTypeReferenceRenderer(EnunciateLogger logger, boolean removeObjectPrefix) {
        this.logger = logger;
        this.removeObjectPrefix = removeObjectPrefix;
    }

    public void render(IndentationPrinter ip, DataTypeReference dtr, String description) {
        if (dtr == null) {
            logger.debug("  no data type reference - void? (description: " + description + ")");
            return;
            //throw new IllegalStateException("Cannot render null data type");
        }

        logger.info("  render type for " + dtr.getContainers());

        DataType value = dtr.getValue();
        List<ContainerType> containers = dtr.getContainers();
        boolean hasContainers = containers != null && !containers.isEmpty();

        if (TypeHelper.renderAsSimpleRef(dtr)) {
            addSchemaRef(ip, value);
            return;
        }

        if (description != null && !description.isEmpty()) {
            ip.add("description: ", YamlHelper.safeYamlString(description));
        }

        if (value != null && hasContainers) {
            renderContainer(ip, Lists.newCopyOnWriteArrayList(containers), () -> addSchemaRef(ip, value));
        } else {
            if (hasContainers) {
                if (isMultiDimensionalCollection(containers)) {
                    renderNestedArrays(ip, dtr, containers.size());
                } else {
                    renderContainer(ip, containers, () -> renderType(ip, dtr));
                }
            } else {
                // FIXME: Conversion to DataTypeReferenceImpl must be broken in Jaxb frontend
                boolean workaroundRendering = false;
                if (dtr instanceof DataTypeReferenceImpl) {
                    XmlType xmlType = ((DataTypeReferenceImpl) dtr).getXmlType();
                    if (xmlType instanceof MapXmlType) {
                        MapXmlType mapXmlType = ((MapXmlType) xmlType);
                        if (mapXmlType.getKeyType().isSimple() && mapXmlType.getValueType().isSimple()) {
                            logger.info("Workaround rendering of simple map");
                            renderContainer(ip, true, () -> ip.add("type: string"));
                            ip.nextLevel();
                            ip.add("type: string");
                            ip.prevLevel();
                            workaroundRendering = true;
                        } else {
                            logger.info("Unhandled map with types " + mapXmlType.getKeyType() + " : " + mapXmlType.getValueType());
                        }
                    }
                }

                if (!workaroundRendering) {
                    renderType(ip, dtr);
                }
            }
        }
    }

    private boolean isMultiDimensionalCollection(List<ContainerType> containers) {
        return containers.stream().noneMatch(ContainerType::isMap);
    }

    private void renderNestedArrays(IndentationPrinter ip, DataTypeReference dtr, int dimensionSize) {
        if (dimensionSize == 0) {
            renderValue(ip, () -> renderType(ip, dtr));
        } else {
            renderArray(ip);
            ip.nextLevel();
            renderNestedArrays(ip, dtr, dimensionSize - 1);
            ip.prevLevel();
        }
    }

    private void renderValue(IndentationPrinter ip, Runnable valueTypeRenderer) {
        valueTypeRenderer.run(); // NOSONAR
    }

    private void renderContainer(IndentationPrinter ip, List<ContainerType> containers, Runnable valueTypeRenderer) {
        if(!containers.isEmpty()){
            ContainerType container = containers.get(0);
            containers.remove(container);
            renderContainer(ip, container.isMap(), valueTypeRenderer);
            if (container.isMap() && containers.isEmpty()) {
                // a map without additional containers would render empty "additionalProperties" -> add an empty object
                ip.add(" {}");
            } else {
                ip.nextLevel();
                renderContainer(ip, containers, valueTypeRenderer);
                ip.prevLevel();
            }
        }
    }

    private void renderContainer(IndentationPrinter ip, boolean isMap, Runnable valueTypeRenderer) {
        if (isMap) {
            renderMapContainer(ip);
        } else {
            renderNonMapContainer(ip, valueTypeRenderer);
        }
    }

    private void renderNonMapContainer(IndentationPrinter ip, Runnable valueTypeRenderer) {
        renderArray(ip);
        ip.nextLevel();
        renderValue(ip, valueTypeRenderer);
        ip.prevLevel();
    }

    private void renderArray(IndentationPrinter ip) {
        ip.add("type: array");
        ip.add("items:");
    }

    private void renderMapContainer(IndentationPrinter ip) {
        ip.add("type: object");
        ip.add("additionalProperties:");
    }

    public void renderType(IndentationPrinter ip, Parameter parameter) {
        renderType(ip, OpenApiTypeFormat.from(parameter));
    }

    private void renderType(IndentationPrinter ip, DataTypeReference dtr) {
        renderType(ip, OpenApiTypeFormat.from(dtr));
    }

    private void renderType(IndentationPrinter ip, OpenApiTypeFormat tf) {
        ip.add("type: ", tf.getType());
        tf.getFormat().ifPresent(f -> ip.add("format: ", f));
    }

    public void renderObsoletedFileFormat(IndentationPrinter ip) {
        logger.info("CALLING obsoleted format");
        //new Exception("bad code").printStackTrace();
        renderType(ip, OpenApiTypeFormat.BINARY_STREAM_TYPE);
    }

    private static void addSchemaRef(IndentationPrinter ip, DataType value) {
        addSchemaSlugReference(ip, value.getSlug());
    }

    public void addSchemaRef(IndentationPrinter ip, DataTypeReference ref) {
        logger.info("Looking at ref %s", ref.getBaseType());

        String slug = ref.getSlug();
        if (slug != null && !slug.isEmpty()) {
            addSchemaSlugReference(ip, slug);
        } else {
            renderType(ip, ref);
        }
    }

    private static void addSchemaSlugReference(IndentationPrinter ip, String slug) {
        ip.add("$ref: \"#/components/schemas/" + slug + "\"");
    }

    public boolean doRemoveObjectPrefix() {
        return removeObjectPrefix;
    }
}
