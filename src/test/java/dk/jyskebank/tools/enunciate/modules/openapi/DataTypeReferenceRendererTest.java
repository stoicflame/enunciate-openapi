package dk.jyskebank.tools.enunciate.modules.openapi;

import com.webcohesion.enunciate.api.datatype.BaseType;
import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndentationPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataTypeReferenceRendererTest {

    private static final String DOS_NEWLINE = "\r\n";

    private DataTypeReferenceRenderer renderer = new DataTypeReferenceRenderer(new OutputLogger(), false);
    private List<DataTypeReference.ContainerType> containers = new ArrayList<>();
    private DataTypeReference dtr = mock(DataTypeReference.class);
    private final IndentationPrinter ip = TestHelper.getIndentationPrinter();
    private final StringBuffer yamlBuffer = new StringBuffer();

    @BeforeEach
    public void init() {

        System.out.println("Running init");
        when(dtr.getBaseType()).thenReturn(BaseType.number);
        when(dtr.getBaseTypeFormat()).thenReturn("int32");
        when(dtr.getContainers()).thenReturn(containers);
    }

    @Test
    void verifyRenderingOfOneDimensionalArray() {

        containers.add(DataTypeReference.ContainerType.array);
        renderer.render(ip, dtr, null);

        createExpectedResult(yamlBuffer, 1);
        assertEquals(yamlBuffer.toString(), ip.toString());
    }

    @Test
    void verifyRenderingOfTwoDimensionalArray() {

        containers.add(DataTypeReference.ContainerType.array);
        containers.add(DataTypeReference.ContainerType.array);

        renderer.render(ip, dtr, null);

        createExpectedResult(yamlBuffer, 2);
        assertEquals(yamlBuffer.toString(), ip.toString());
    }

    @Test
    void verifyRenderingOfThreeDimensionalArray() {

        containers.add(DataTypeReference.ContainerType.array);
        containers.add(DataTypeReference.ContainerType.array);
        containers.add(DataTypeReference.ContainerType.array);

        renderer.render(ip, dtr, null);

        createExpectedResult(yamlBuffer, 3);
        assertEquals(yamlBuffer.toString(), ip.toString());
    }

    // this method was added to simplify calling from tests above
    private void createExpectedResult(StringBuffer yamlBuffer, int nestingDepth) {
        createExpectedResult(yamlBuffer, nestingDepth, nestingDepth);
    }

    /**
     * @param yamlBuffer   expected result will be built up in this buffer
     * @param currentLevel the array will be traversed from outer container down to value type
     * @param nestingDepth total nesting depth
     */
    private void createExpectedResult(StringBuffer yamlBuffer, int currentLevel, int nestingDepth) {

        if (currentLevel == 0) {
            addValueType(yamlBuffer, currentLevel, nestingDepth);
        } else {
            addArrayType(yamlBuffer, currentLevel, nestingDepth);
            createExpectedResult(yamlBuffer, currentLevel - 1, nestingDepth);
        }
    }

    private void addArrayType(StringBuffer sb, int level, int nestingDepth) {

        String indenting = calculateIndenting(level, nestingDepth);
        sb.append(indenting);
        sb.append("type: array" + DOS_NEWLINE);
        sb.append(indenting);
        sb.append("items:" + DOS_NEWLINE);
    }

    private void addValueType(StringBuffer sb, int level, int nestingDepth) {
        String indenting = calculateIndenting(level, nestingDepth);
        sb.append(indenting);
        sb.append("type: integer" + DOS_NEWLINE);
        sb.append(indenting);
        sb.append("format: int32");
    }

    private String calculateIndenting(int level, int nestingDepth) {
        final char[] a = new char[(nestingDepth - level) * 2];
        Arrays.fill(a, ' ');
        return new String(a);
    }

}