package dk.jyskebank.tools.enunciate.modules.openapi.oneof;

public class Dog implements Pet {

    private boolean aggressive;

    public boolean isAggressive() {
        return aggressive;
    }

    @Override
    public String getName() {
        return null;
    }
}
