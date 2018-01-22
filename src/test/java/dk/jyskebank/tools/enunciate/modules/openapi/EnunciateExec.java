package dk.jyskebank.tools.enunciate.modules.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webcohesion.enunciate.Enunciate;

public class EnunciateExec {
  private static final Logger logger = LoggerFactory.getLogger(EnunciateExec.class);

	private String runOnSubPackage;

	public EnunciateExec(String runOnSubPackage) {
	  this.runOnSubPackage = runOnSubPackage;
	}
          
	public void test() throws IOException {
		Enunciate enunciate = new Enunciate();
		Path srcDir = Paths.get("src/test/java");
		Path outputdir = Paths.get("build", "test-enunciate", runOnSubPackage);
		Path buildDir = outputdir.resolve("build");
		Path docsDir = outputdir.resolve("docs");
		DirectoryDeleter.delete(outputdir);
		Files.createDirectories(buildDir);
        Files.createDirectories(docsDir);
		
		runEnunciate(enunciate, srcDir, buildDir, docsDir);
		
		Path actual = docsDir.resolve("openapi.yml");
		List<String> actualYml = Files.readAllLines(actual, StandardCharsets.UTF_8);
		Path expected = srcDir.resolve(this.getClass().getPackage().getName().replace('.', '/')).resolve(runOnSubPackage).resolve("_expected.yml");
		List<String> expectedYml = Files.readAllLines(expected, StandardCharsets.UTF_8);
        
		assertThat(actualYml).isEqualTo(expectedYml);
    }

    private Path runEnunciate(Enunciate enunciate, Path srcDir, Path buildDir, Path docsDir) throws IOException {
      enunciate.setBuildDir(buildDir.toFile());
      
      List<URL> classPathUrls = Arrays.asList(((URLClassLoader)Thread.currentThread().getContextClassLoader()).getURLs());
      List<File> classpathFiles = classPathUrls.stream()
      	.map(u -> new File(u.getFile()))
      	.collect(Collectors.toList());

      Path srcSubPackageDir = srcDir.resolve("dk/jyskebank/tools/enunciate/modules/openapi").resolve(runOnSubPackage);
      enunciate.setSourceFiles(getSourceFiles(srcDir, runOnSubPackage));
      
      Path configFile = srcSubPackageDir.resolve("enunciate.xml");
      logger.info("CONFIG is {} {}", configFile, Files.exists(configFile));
      enunciate.loadConfiguration(configFile.toFile());
      enunciate.getConfiguration().setBase(srcSubPackageDir.toFile());
      enunciate.loadDiscoveredModules();
      
      enunciate.setLogger(new OutputLogger());
      enunciate.addExclude("**/*.yml");
      enunciate.setClasspath(classpathFiles);
      enunciate.addExport("openapi", docsDir.toFile());
      enunciate.run();

      return srcDir;
    }
	
	private Set<File> getSourceFiles(Path d, String subdirFilter) throws IOException {
		try (Stream<Path> files = Files.walk(d)) {
			return files
				.filter(Files::isRegularFile)
				.filter(f -> f.getFileName().toString().endsWith(".java"))
				.filter(f -> f.toString().replace('\\', '/').contains("openapi/"+subdirFilter))
				.map(Path::toFile)
				.collect(Collectors.toSet());
		}
	}
}
