package dk.jyskebank.tools.enunciate.modules.openapi;

import com.webcohesion.enunciate.api.InterfaceDescriptionFile;
import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.Namespace;
import com.webcohesion.enunciate.modules.jaxb.api.impl.ComplexDataTypeImpl;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndentationPrinter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dk.jyskebank.tools.enunciate.modules.openapi.ObjectTypeRenderer.JSON_REF_FORMAT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObjectTypeRendererTest {


    public static final String CONCRETE_TYPE_LABEL = "ConcreteX";
    public static final String CONCRETE_TYPE_XML_NAME = "xml_xmlns_concreteX";
    public static final String CONCRETE_TYPE_XML_URI = "http://www.w3.org/2000/xmlns/";
    public static final String CONCRETE_TYPE_XML_PREFIX = "xmlns";

    @Test
    void verifyRenderAbstractType() {

        ObjectTypeRenderer objectTypeRenderer = new ObjectTypeRenderer(new OutputLogger(), null
                , null, null,  false, false);

        IndentationPrinter ip = TestHelper.getIndentationPrinter();
        DataType abstractType = getAbstractTypeWithTwoSubtypes();

        objectTypeRenderer.render(ip, abstractType, true);

        final String expected = createExpectedResultForAbstractType();
        assertEquals(expected, ip.toString());

    }

    @Test
    void verifyRenderConcreteType() {
        ObjectTypeRenderer objectTypeRenderer = new ObjectTypeRenderer(new OutputLogger(), null
                , null, null, false, false);

        IndentationPrinter ip = TestHelper.getIndentationPrinter();
        DataType concreteType = getConcreteType();

        objectTypeRenderer.render(ip, concreteType, false);

        final String expected = createExpectedResultForConcreteType();
        assertEquals(expected, ip.toString());

    }

    @Test
    void verifyRenderConcreteTypeNamespacePrefix(){
        ObjectTypeRenderer objectTypeRenderer = new ObjectTypeRenderer(new OutputLogger(), null
                , null, Collections.singletonMap(CONCRETE_TYPE_XML_URI,CONCRETE_TYPE_XML_PREFIX), false, false);

        IndentationPrinter ip = TestHelper.getIndentationPrinter();
        DataType concreteType = getConcreteTypeWithNamespace();

        objectTypeRenderer.render(ip, concreteType, false);

        final String expected = createExpectedResultForConcreteTypeWithNamespacePrefix();
        assertEquals(expected, ip.toString());
    }

    @Test
    void verifyRenderConcreteTypeWithoutNamespacePrefix(){
        ObjectTypeRenderer objectTypeRenderer = new ObjectTypeRenderer(new OutputLogger(), null
                , null, null, false, false);

        IndentationPrinter ip = TestHelper.getIndentationPrinter();
        DataType concreteType = getConcreteTypeWithNamespace();

        objectTypeRenderer.render(ip, concreteType, false);

        final String expected = createExpectedResultForConcreteTypeWithoutNamespacePrefix();
        assertEquals(expected, ip.toString());
    }

    private ComplexDataTypeImpl getConcreteTypeWithNamespace() {
        ComplexDataTypeImpl mockedDataType = mock(ComplexDataTypeImpl.class);
        when(mockedDataType.isAbstract()).thenReturn(Boolean.FALSE);

        when(mockedDataType.getLabel()).thenReturn(CONCRETE_TYPE_LABEL);
        when(mockedDataType.getXmlName()).thenReturn(CONCRETE_TYPE_XML_NAME);
        when(mockedDataType.getBaseType()).thenReturn(BaseType.string);

        Namespace customNamespace = new Namespace() {
            @Override
            public String getUri() {
                return CONCRETE_TYPE_XML_URI;
            }
            @Override
            public InterfaceDescriptionFile getSchemaFile() {
                return null;
            }
            @Override
            public List<? extends DataType> getTypes() {
                return null;
            }
        };
        when(mockedDataType.getNamespace()).thenReturn(customNamespace);

        return mockedDataType;
    }

    private String createExpectedResultForConcreteTypeWithNamespacePrefix() {
        String DOS_NEWLINE = "\r\n";
        return String.format("title: \"%s\"%s%s  type: string%s%s  xml:%s%s    name: %s%s%s    namespace: %s%s%s    prefix: %s",
                CONCRETE_TYPE_LABEL, DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                CONCRETE_TYPE_XML_NAME, DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                CONCRETE_TYPE_XML_URI, DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                CONCRETE_TYPE_XML_PREFIX, DOS_NEWLINE, TestHelper.INITIAL_INDENTATION);
    }

    private String createExpectedResultForConcreteTypeWithoutNamespacePrefix() {
        String DOS_NEWLINE = "\r\n";
        return String.format("title: \"%s\"%s%s  type: string%s%s  xml:%s%s    name: %s%s%s    namespace: %s",
                CONCRETE_TYPE_LABEL, DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                CONCRETE_TYPE_XML_NAME, DOS_NEWLINE, TestHelper.INITIAL_INDENTATION,
                CONCRETE_TYPE_XML_URI, DOS_NEWLINE, TestHelper.INITIAL_INDENTATION);
    }

    private String createExpectedResultForConcreteType() {
        String DOS_NEWLINE = "\r\n";
        return String.format("title: \"%s\"%s%s  type: string",
                CONCRETE_TYPE_LABEL,
                DOS_NEWLINE,
                TestHelper.INITIAL_INDENTATION);
    }

    private DataType getConcreteType() {
        DataType mockedDataType = mock(DataType.class);
        when(mockedDataType.isAbstract()).thenReturn(Boolean.FALSE);

        when(mockedDataType.getLabel()).thenReturn(CONCRETE_TYPE_LABEL);
        when(mockedDataType.getBaseType()).thenReturn(BaseType.string);

        when(mockedDataType.getSubtypes()).thenReturn(Collections.emptyList());
        when(mockedDataType.getProperties()).thenReturn(Collections.emptyList());
        when(mockedDataType.getValues()).thenReturn(Collections.emptyList());

        return mockedDataType;
    }

    private String createExpectedResultForAbstractType() {
        String DOS_NEWLINE = "\r\n";
        return String.format("oneOf: %s%s  %s%s%s  %s",
                DOS_NEWLINE,
                TestHelper.INITIAL_INDENTATION,
                getJsonRefForSubType("A"),
                DOS_NEWLINE,
                TestHelper.INITIAL_INDENTATION,
                getJsonRefForSubType("B"));
    }

    private String getJsonRefForSubType(String a) {
        return String.format(JSON_REF_FORMAT, a);
    }



    private DataType getAbstractTypeWithTwoSubtypes() {

        DataType mockedDataType = mock(DataType.class);
        when(mockedDataType.isAbstract()).thenReturn(Boolean.TRUE);

        List<DataTypeReference> subTypes = new ArrayList<>();
        subTypes.add(createConcreteSubtype("A"));
        subTypes.add(createConcreteSubtype("B"));

        when(mockedDataType.getSubtypes()).thenReturn(subTypes);

        return mockedDataType;
    }

    private DataTypeReference createConcreteSubtype(String nameOfConcreteType) {
        DataTypeReference concreteTypeA = mock(DataTypeReference.class);
        when(concreteTypeA.getSlug()).thenReturn(nameOfConcreteType);
        return concreteTypeA;
    }
}

