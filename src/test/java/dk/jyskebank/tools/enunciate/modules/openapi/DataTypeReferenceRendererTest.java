package dk.jyskebank.tools.enunciate.modules.openapi;

import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.BaseTypeFormat;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataTypeReferenceRendererTest {

    private static final String DOS_NEWLINE = "\r\n";

    @Test
    void verifyRenderingOfOneDimensionalArray() {


        DataTypeReferenceRenderer renderer = new DataTypeReferenceRenderer(new OutputLogger(), false);

        DataTypeReference dtr = mock(DataTypeReference.class);
        List<DataTypeReference.ContainerType> containers = new ArrayList<>();
        containers.add(DataTypeReference.ContainerType.array);
        when(dtr.getContainers()).thenReturn(containers);
        when(dtr.getBaseType()).thenReturn(BaseType.number);
        when(dtr.getBaseTypeFormat()).thenReturn(BaseTypeFormat.INT32);
        final IndententationPrinter ip = TestHelper.getIndentationPrinter();
        renderer.render(ip, dtr, null);

        final String expected = createExpectedResultOneDimensionalArray();
        assertEquals(expected, ip.toString());
    }

    @Test
    void verifyRenderingOfTwoDimensionalArray() {

        DataTypeReferenceRenderer renderer = new DataTypeReferenceRenderer(new OutputLogger(), false);

        DataTypeReference dtr = mock(DataTypeReference.class);
        List<DataTypeReference.ContainerType> containers = new ArrayList<>();
        containers.add(DataTypeReference.ContainerType.array);
        containers.add(DataTypeReference.ContainerType.array);
        when(dtr.getContainers()).thenReturn(containers);
        when(dtr.getBaseType()).thenReturn(BaseType.number);
        when(dtr.getBaseTypeFormat()).thenReturn(BaseTypeFormat.INT32);

        final IndententationPrinter ip = TestHelper.getIndentationPrinter();
        renderer.render(ip, dtr, null);

        final String expected = createExpectedResultTwoDimensionalArray();
        assertEquals(expected, ip.toString());
    }

    @Test
    void verifyRenderingOfThreeDimensionalArray() {

        DataTypeReferenceRenderer renderer = new DataTypeReferenceRenderer(new OutputLogger(), false);

        DataTypeReference dtr = mock(DataTypeReference.class);
        List<DataTypeReference.ContainerType> containers = new ArrayList<>();
        containers.add(DataTypeReference.ContainerType.array);
        containers.add(DataTypeReference.ContainerType.array);
        containers.add(DataTypeReference.ContainerType.array);
        when(dtr.getContainers()).thenReturn(containers);
        when(dtr.getBaseType()).thenReturn(BaseType.number);
        when(dtr.getBaseTypeFormat()).thenReturn(BaseTypeFormat.INT32);

        final IndententationPrinter ip = TestHelper.getIndentationPrinter();
        renderer.render(ip, dtr, null);

        final String expected = createExpectedResultThreeDimensionalArray();
        assertEquals(expected, ip.toString());
    }

    private String createExpectedResultOneDimensionalArray() {
        return "type: array" + DOS_NEWLINE +
                "items:" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "  type: integer" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "  format: int32";


    }

    private String createExpectedResultTwoDimensionalArray() {
        return "type: array" + DOS_NEWLINE +
                "items:" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "  type: array" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "  items:" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "    type: integer" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "    format: int32";


    }

    private String createExpectedResultThreeDimensionalArray() {
        return "type: array" + DOS_NEWLINE +
                "items:" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "  type: array" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "  items:" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "    type: array" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "    items:" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "      type: integer" + DOS_NEWLINE +
                TestHelper.INITIAL_INDENTATION + "      format: int32";
    }
}