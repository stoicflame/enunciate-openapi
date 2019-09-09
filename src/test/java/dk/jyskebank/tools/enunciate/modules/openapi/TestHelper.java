package dk.jyskebank.tools.enunciate.modules.openapi;

import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndentationPrinter;

public class TestHelper {


    public static final String INITIAL_INDENTATION = "";

    public static IndentationPrinter getIndentationPrinter() {
        return new IndentationPrinter(INITIAL_INDENTATION, false);
    }
}
