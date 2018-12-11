package dk.jyskebank.tools.enunciate.modules.openapi;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class EnunciateTestsGenerator {
	@TestFactory
	public List<DynamicTest> makeTests() throws IOException {
		copySwaggerUiJar();

		try (Stream<Path> testDirs = Files.list(Paths.get("src/test/java/dk/jyskebank/tools/enunciate/modules/openapi"))) {
			return testDirs
					.filter(Files::isDirectory)
					.map(Path::getFileName)
					.map(Path::toString)
//					.filter("arguments"::equals)
					.filter(n -> !n.equals("enumeration"))
					.map(name -> DynamicTest.dynamicTest(name, new EnunciateExec(name)::run))
					.collect(toList());
		}	
	}

    private void copySwaggerUiJar() {
    	Path dest = Paths.get("bin/test/openapi-swagger-ui.zip");
    	Path src = Paths.get("build/distributions/openapi-swagger-ui.zip");
    	if (!Files.exists(dest)) { // NOSONAR
    		if (!Files.exists(src)) { // NOSONAR
    			throw new IllegalStateException("Missing swagger ui resources, you should run 'gradlew packSwaggerUi'");
    		}
    		try {
    			Files.createDirectories(dest.getParent());
    			Files.copy(src, dest);
    		} catch (IOException e) {
    			throw new IllegalStateException("Failed to copy swagger ui resources file", e);
    		}
    	}
    }
}
