/*
 * Copyright © 2018 Jyske Bank
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

import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.resources.Parameter;
import com.webcohesion.enunciate.modules.jaxb.model.types.KnownXmlType;

import java.util.Optional;

public class OpenApiTypeFormat {
//    private static final Map<BaseTypeFormat, String> ENUNCIATE_TO_OPENAPI_FORMAT = new EnumMap<>(BaseTypeFormat.class);
//
//    static {
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.INT32, "int32");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.INT64, "int64");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.FLOAT, "float");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.DOUBLE, "double");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.OCTETS, "binary");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.BASE64, "byte");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.DATE, "date");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.DATE_TIME, "date-time");
//        ENUNCIATE_TO_OPENAPI_FORMAT.put(BaseTypeFormat.PASSWORD, "password");
//    }

    private final String type;
    private final String format;

    //	public static final OpenApiTypeFormat BASE64_TYPE = new OpenApiTypeFormat(BaseType.string, BaseTypeFormat.BASE64);
    public static final OpenApiTypeFormat BASE64_TYPE = new OpenApiTypeFormat(BaseType.string, "byte");
    //	public static final OpenApiTypeFormat BINARY_STREAM_TYPE = new OpenApiTypeFormat(BaseType.string, BaseTypeFormat.OCTETS);
    public static final OpenApiTypeFormat BINARY_STREAM_TYPE = new OpenApiTypeFormat(BaseType.string, "binary");
    //	public static final OpenApiTypeFormat DATE_TIME_TYPE = new OpenApiTypeFormat(BaseType.string, BaseTypeFormat.DATE_TIME);
    public static final OpenApiTypeFormat DATE_TIME_TYPE = new OpenApiTypeFormat(BaseType.string, "date-time");
    //	public static final OpenApiTypeFormat DATE_TYPE = new OpenApiTypeFormat(BaseType.string, BaseTypeFormat.DATE);
    public static final OpenApiTypeFormat DATE_TYPE = new OpenApiTypeFormat(BaseType.string, "date");

    private OpenApiTypeFormat(BaseType enunciateType, String enunciateFormat) {
        type = fromEnunciateType(enunciateType, enunciateFormat);
        format = enunciateFormat;
    }


//	public static String toOpenApiFormat(BaseTypeFormat format) {
//		return format == null ? null : ENUNCIATE_TO_OPENAPI_FORMAT.get(format);
//	}

    private static OpenApiTypeFormat fromValidated(BaseType baseType, String format) {
        // This was AFAIK a now-obsoleted fallback in older swagger formats. Cannot find documentation now.
        if (format == null && baseType == BaseType.object) {
            return BINARY_STREAM_TYPE;
        }

        return new OpenApiTypeFormat(baseType, format);
    }

    public static OpenApiTypeFormat from(BaseType baseType, String format) {
        return fromValidated(baseType, format);
    }

    public static OpenApiTypeFormat from(BaseType baseType) {
        return fromValidated(baseType, null);
    }

    public static OpenApiTypeFormat from(Parameter parameter) {
        return fromValidated(toEnunciateType(parameter.getTypeName()), parameter.getTypeFormat());
    }

    public static OpenApiTypeFormat from(DataTypeReference dtr) {
        // Overrides for XML types
        // https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md#dataTypes
        String label = dtr.getLabel();
        if (KnownXmlType.BASE64_BINARY.getName().equals(label)) {
            return BINARY_STREAM_TYPE;
        } else if (KnownXmlType.DATE_TIME.getName().equals(label)) {
            return DATE_TIME_TYPE;
        } else if (KnownXmlType.DATE.getName().equals(label)) {
            return DATE_TYPE;
        }

        return fromValidated(dtr.getBaseType(), dtr.getBaseTypeFormat());
    }

    public Optional<String> getFormat() {
        return Optional.ofNullable(format);
    }

    public String getType() {
        return type;
    }

    private static BaseType toEnunciateType(String type) {
        switch (type) {
            case "integer":
            case "number":
                return BaseType.number;
            case "boolean":
                return BaseType.bool;
            case "string":
                return BaseType.string;
            case "object":
                return BaseType.object;
            default:
                throw new IllegalStateException("Called with unhandled type " + type);
        }
    }

    private static String fromEnunciateType(BaseType baseType, String enunciateFormat) {
        switch (baseType) {
            case bool:
                return "boolean";
            case number:
                if (("int32".equals(enunciateFormat) || "int64".equals(enunciateFormat))) {
                    return "integer";
                } else {
                    return "number";
                }
            case string:
                return "string";
            case object:
                return "object";
            default:
                throw new IllegalStateException("Called with unhandled type " + baseType);
        }
    }
}
