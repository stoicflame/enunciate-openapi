package dk.jyskebank.tools.enunciate.modules.openapi;

import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;
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

    @Test
    void verifyRenderAbstractType() {

        ObjectTypeRenderer objectTypeRenderer = new ObjectTypeRenderer(new OutputLogger(), null
                , null, false, false);

        IndententationPrinter ip = TestHelper.getIndentationPrinter();
        DataType abstractType = getAbstractTypeWithTwoSubtypes();

        objectTypeRenderer.render(ip, abstractType, true);

        final String expected = createExpectedResultForAbstractType();
        assertEquals(expected, ip.toString());

    }

    @Test
    void verifyRenderConcreteType() {
        ObjectTypeRenderer objectTypeRenderer = new ObjectTypeRenderer(new OutputLogger(), null
                , null, false, false);

        IndententationPrinter ip = TestHelper.getIndentationPrinter();
        DataType concreteType = getConcreteType();

        objectTypeRenderer.render(ip, concreteType, false);

        final String expected = createExpectedResultForConcreteType();
        assertEquals(expected, ip.toString());

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

