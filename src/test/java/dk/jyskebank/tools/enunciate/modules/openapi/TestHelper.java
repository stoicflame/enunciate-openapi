package dk.jyskebank.tools.enunciate.modules.openapi;

import dk.jyskebank.tools.enunciate.modules.openapi.yaml.IndententationPrinter;

public class TestHelper {


    public static final String INITIAL_INDENTATION = "";

    public static IndententationPrinter getIndentationPrinter() {
        return new IndententationPrinter(INITIAL_INDENTATION, false);
    }
}
