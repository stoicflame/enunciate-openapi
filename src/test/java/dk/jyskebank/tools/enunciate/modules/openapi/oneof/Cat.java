package dk.jyskebank.tools.enunciate.modules.openapi.oneof;

public class Cat implements Pet {

    private boolean famousOnYouTube;

    public boolean isFamousOnYouTube() {
        return famousOnYouTube;
    }

    @Override
    public String getName() {
        return null;
    }
}
