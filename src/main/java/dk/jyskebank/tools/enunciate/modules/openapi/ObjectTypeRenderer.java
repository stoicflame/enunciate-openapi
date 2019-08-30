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

import static dk.jyskebank.tools.enunciate.modules.openapi.yaml.YamlHelper.safeYamlString;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.webcohesion.enunciate.EnunciateLogger;
import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.DataTypeReference.ContainerType;
import com.webcohesion.enunciate.api.datatype.Namespace;
import com.webcohesion.enunciate.api.datatype.Property;
import com.webcohesion.enunciate.api.datatype.PropertyMetadata;
import com.webcohesion.enunciate.api.datatype.Value;
import com.webcohesion.enunciate.javac.javadoc.JavaDoc;
import com.webcohesion.enunciate.metadata.DocumentationExample;

import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.JsonToYamlHelper;

public class ObjectTypeRenderer {
    public static final String JSON_REF_FORMAT = "- $ref: \"#/components/schemas/%s\"";
    private final EnunciateLogger logger;
    private final DataTypeReferenceRenderer datatypeRefRenderer;
    private final Set<String> passThroughAnnotations;

    private final boolean removeObjectPrefix;

    public ObjectTypeRenderer(EnunciateLogger enunciateLogger, DataTypeReferenceRenderer datatypeRefRenderer, Set<String> passThroughAnnotations, boolean removeObjectPrefix) {
        this.logger = enunciateLogger;
        this.datatypeRefRenderer = datatypeRefRenderer;
        this.passThroughAnnotations = passThroughAnnotations;
        this.removeObjectPrefix = removeObjectPrefix;
    }

    public void render(IndententationPrinter ip, DataType datatype, boolean syntaxIsJson) {
        logger.info("Rendering type " + datatype.getLabel());
        ip.pushNextLevel();

        List<DataTypeReference> subtypes = datatype.getSubtypes();
        if (datatype.isAbstract() && subtypes != null) {
            ip.add("oneOf: ", "");
            for (DataTypeReference subType : subtypes) {
            	addSchemaSlugReference(ip, subType.getSlug());
            }
        } else {
            renderConcreteType(ip, datatype, syntaxIsJson);
        }

        ip.popLevel();
    }

    private static void addSchemaSlugReference(IndententationPrinter ip, String slug) {
        ip.add(String.format(JSON_REF_FORMAT, slug));
    }

    private void renderConcreteType(IndententationPrinter ip, DataType datatype, boolean syntaxIsJson) {
        ip.add("title: ", safeYamlString(datatype.getLabel()));
        addOptionalSupertypeHeader(ip, datatype);

        ip.add("type: ", getBaseType(datatype));
        addOptionalRequired(ip, datatype);
        addOptionalProperties(ip, datatype, syntaxIsJson);
        addOptionalEnum(ip, datatype);
        addOptionalXml(ip, datatype);

        addOptionalExample(ip, datatype, syntaxIsJson);
    }

    private void addOptionalSupertypeHeader(IndententationPrinter ip, DataType datatype) {
        List<DataTypeReference> supertypes = datatype.getSupertypes();
        if (supertypes == null || supertypes.isEmpty()) {
            return;
        }

        ip.add("allOf:");
        ip.nextLevel();
        ip.itemFollows();

        DataTypeReference superType = supertypes.iterator().next();
        datatypeRefRenderer.addSchemaRef(ip, superType);

        ip.itemFollows();
    }

    private void addOptionalRequired(IndententationPrinter ip, DataType datatype) {
        List<? extends Property> properties = datatype.getProperties();
        if (properties == null) {
            return;
        }

        List<Property> requiredProperties = new ArrayList<>();
        for (Property p : properties) {
            if (p.isRequired()) {
                requiredProperties.add(p);
            }
        }

        if (requiredProperties.isEmpty()) {
            return;
        }

        ip.add("required:");
        ip.nextLevel();
        for (Property p : requiredProperties) {
            ip.item("\"" + p.getName() + "\"");
        }
        ip.prevLevel();
    }

    public boolean doRemoveObjectPrefix() {
        return removeObjectPrefix;
    }

    private static String getBaseType(DataType datatype) {
        switch (datatype.getBaseType()) {
            case bool:
                return "boolean";
            case number:
                return "number";
            case string:
                return "string";
            default:
                return "object";
        }
    }

    private void addOptionalEnum(IndententationPrinter ip, DataType datatype) {
        List<? extends Value> values = datatype.getValues();
        if (values == null || values.isEmpty()) {
            return;
        }

        List<String> enums = values.stream()
                .map(Value::getValue)
                .collect(toList());
        renderEnum(ip, enums);
    }

    public void renderEnum(IndententationPrinter ip, List<String> values) {
        ip.add("enum:");
        ip.nextLevel();
        for (String e : values) {
            ip.item("\"" + e + "\"");
        }
        ip.prevLevel();
    }

    private void addOptionalProperties(IndententationPrinter ip, DataType datatype, boolean syntaxIsJson) {
        List<? extends Property> properties = datatype.getProperties();
        if (properties == null || properties.isEmpty()) {
            return;
        }

        ip.add("properties:");
        ip.nextLevel();
        for (Property p : properties) {
            addProperty(ip, datatype, p, syntaxIsJson);
        }
        ip.prevLevel();
    }

    private void addProperty(IndententationPrinter ip, DataType datatype, Property p, boolean syntaxIsJson) {
        logger.info(" adding property " + p.getName() + " with annotations " + p.getAnnotations().keySet());

        ip.add("\"" + p.getName() + "\"", ":");
        ip.nextLevel();
        if (datatype.getPropertyMetadata().containsKey("namespaceInfo")) {
            addNamespaceXml(ip, p);
        }

        addOptionalNullable(ip, p);

        addConstraints(ip, p);
        if (p.isReadOnly()) {
            ip.add("readOnly: ", Boolean.toString(p.isReadOnly()));
        }

        datatypeRefRenderer.render(ip, p.getDataType(), p.getDescription());

        addOptionalPropertyExample(ip, p, syntaxIsJson);
        addPassThroughAnnotations(ip, p);

        ip.prevLevel();
    }

    private void addPassThroughAnnotations(IndententationPrinter ip, Property p) {
        List<String> annotations = p.getAnnotations().entrySet().stream()
                .filter(e -> passThroughAnnotations.contains(e.getKey()))
                .map(e -> renderAnnotation(e.getKey(), e.getValue().getElementValues()))
                .peek(s -> logger.info("  with " + s))
                .collect(toList());

        if (!annotations.isEmpty()) {
            ip.add("x-annotations:");
            ip.nextLevel();
            annotations.forEach(a -> ip.add("- '", a, "'"));
            ip.prevLevel();
        }
    }

    private String renderAnnotation(String annotationName, Map<? extends ExecutableElement, ? extends AnnotationValue> annotationOptions) {
        StringBuilder sb = new StringBuilder("@")
                .append(annotationName);
        if (!annotationOptions.isEmpty()) {
            String args = annotationOptions.entrySet().stream()
                    .map(e -> {
                        String n = e.getKey().toString().replace("()", "");
                        String v = e.getValue().toString();
                        return n + "=" + v;
                    })
                    .collect(joining(", "));

            sb.append("(").append(args).append(")");
        }
        return sb.toString();
    }

    private static void addConstraints(IndententationPrinter ip, Property p) {
        List<ContainerType> containers = p.getDataType().getContainers();
        boolean isArray = containers != null && !containers.isEmpty();

        Max max = p.getAnnotation(Max.class);
        DecimalMax decimalMax = p.getAnnotation(DecimalMax.class);
        if (max != null) {
            ip.add("maximum: ", Long.toString(max.value()));
        } else if (decimalMax != null) {
            ip.add("maximum: ", decimalMax.value());
            ip.add("exclusiveMaximum: ", Boolean.toString(!decimalMax.inclusive()));
        }

        Min min = p.getAnnotation(Min.class);
        DecimalMin decimalMin = p.getAnnotation(DecimalMin.class);
        if (min != null) {
            ip.add("minimum: ", Long.toString(min.value()));
        } else if (decimalMin != null) {
            ip.add("minimum: ", decimalMin.value());
            ip.add("exclusiveMinimum: ", Boolean.toString(!decimalMin.inclusive()));
        }

        Size size = p.getAnnotation(Size.class);
        if (size != null) {
            if (isArray) {
                ip.add("maxItems: ", Integer.toString(size.max()));
                ip.add("minItems: ", Integer.toString(size.min()));
            } else {
                ip.add("maxLength: ", Integer.toString(size.max()));
                ip.add("minLength: ", Integer.toString(size.min()));
            }
        }

        Pattern mustMatchPattern = p.getAnnotation(Pattern.class);
        if (mustMatchPattern != null) {
            ip.add("pattern: ", "'" + mustMatchPattern.regexp() + "'");
        }
    }

    private void addOptionalNullable(IndententationPrinter ip, Property p) {
        getNillable(p).ifPresent(isNullable -> {
            if (!TypeHelper.renderAsSimpleRef(p.getDataType())) {
                ip.add("nullable: ", isNullable);
            }
        });
    }

    private void addNamespaceXml(IndententationPrinter ip, Property p) {
        PropertyMetadata metadata = AccessorProperty.getMetadata(p);
        if (metadata == null) {
            return;
        }

        Optional<String> wrappedName = getWrappedName(p);
        String namespace = metadata.getTitle();

        boolean renderAttribute = AccessorProperty.isAttribute(p);
        boolean renderNamespace = namespace != null && !namespace.isEmpty();

        if (!wrappedName.isPresent() && !renderAttribute && !renderNamespace) {
            return;
        }

        ip.add("xml:");
        ip.nextLevel();
        wrappedName.ifPresent(name -> {
            ip.add("name: ", name);
            ip.add("wrapped: true");
        });
        if (renderAttribute) {
            ip.add("attribute: ", Boolean.TRUE.toString());
        }
        if (renderNamespace) {
            ip.add("namespace: ", namespace);
        }
        ip.prevLevel();
    }

    private Optional<String> getWrappedName(Property p) {
        return getAttributeValue(p, "javax.xml.bind.annotation.XmlElementWrapper", "name()");
    }

    private Optional<String> getNillable(Property p) {
        return getAttributeValue(p, "javax.xml.bind.annotation.XmlElement", "nillable()");
    }

    private Optional<String> getAttributeValue(Property p, String annotationName, String propertyName) {
        return p.getAnnotations().entrySet().stream()
                .filter(e -> annotationName.equals(e.getKey()))
                .flatMap(am -> am.getValue().getElementValues().entrySet().stream())
                .filter(e -> propertyName.equals(e.getKey().toString()))
                .map(e -> e.getValue().getValue())
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .findFirst();
    }

    private void addOptionalXml(IndententationPrinter ip, DataType datatype) {
        String xmlName = getNonNullAndNonEmpty(AccessorDataType.getXmlName(datatype));

        Namespace namespace = datatype.getNamespace();
        String xmlNamespace = getNonNullAndNonEmpty(namespace != null ? namespace.getUri() : null);

        logger.debug("optionalXml for " + datatype.getLabel() + " : " + xmlName + " / " + xmlNamespace);

        if (xmlName != null || xmlNamespace != null) {
            ip.add("xml:");
            ip.nextLevel();
            if (xmlName != null) {
                ip.add("name: ", xmlName);
            }
            if (xmlNamespace != null) {
                ip.add("namespace: ", xmlNamespace);
            }
            ip.prevLevel();
        }
    }

    private static String getNonNullAndNonEmpty(String str) {
        if (str == null) {
            return null;
        }
        return str.isEmpty() ? null : str;
    }

    private static void addOptionalExample(IndententationPrinter ip, DataType datatype, boolean syntaxIsJson) {
        if (!syntaxIsJson) {
            return;
        }

        renderExample(ip, getExampleFromType(datatype, datatype.getBaseType(), Optional.empty()));
    }

    private static void addOptionalPropertyExample(IndententationPrinter ip, Property property, boolean syntaxIsJson) {
        if (!syntaxIsJson) {
            return;
        }

        BaseType baseType = property.getDataType() != null ? property.getDataType().getBaseType() : null;
        Optional<String> specifiedExample = findSpecifiedExample(property);
        renderExample(ip, getExampleFromType(null, baseType, specifiedExample));
    }

    private static void renderExample(IndententationPrinter ip, Optional<String> optExample) {
        optExample.ifPresent(example -> {
            ip.add("example:");
            ip.pushNextLevel();
            String yaml = JsonToYamlHelper.jsonToYaml(example);
            Stream.of(yaml.split("\n"))
                    .skip(1) // ----- header
                    .forEach(ip::add);
            ip.popLevel();
        });
    }

    private static Optional<String> getExampleFromType(DataType dataType, BaseType baseType, Optional<String> specifiedExample) {
        if (baseType == null) {
            return Optional.empty();
        }

        if (BaseType.object == baseType
                && dataType != null
                && dataType.getBaseType() == BaseType.object
                && dataType.getExample() != null) {
            return Optional.of(dataType.getExample().getBody());
        } else {
            return specifiedExample;
        }
    }

    private static Optional<String> findSpecifiedExample(Property property) {
        String example = null;

        JavaDoc.JavaDocTagList tags = property.getJavaDoc().get("documentationExample");
        if (tags != null && !tags.isEmpty()) {
            String tag = tags.get(0).trim();
            example = tag.isEmpty() ? null : tag;
        }

        DocumentationExample documentationExample = property.getAnnotation(DocumentationExample.class);
        if (documentationExample != null) {
            if (documentationExample.exclude()) {
                return Optional.empty();
            }

            example = documentationExample.value();
            example = "##default".equals(example) ? null : example;
        }

        if (example != null && (property.getDataType() == null || property.getDataType().getBaseType() == BaseType.string)) {
            example = new String(new JsonStringEncoder().quoteAsString(example));
        }

        return Optional.ofNullable(example);
    }
}
