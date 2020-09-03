# Enunciate OpenApi3 Module #


This project contains an [OpenAPI3](https://github.com/OAI/OpenAPI-Specification) module for [Enunciate](https://github.com/stoicflame/enunciate).
The code is based on the Enunciate built-in module for Swagger.

The module has been used to generate documentation for a number of internal projects in Jyske Bank.
The output validates in the [Swagger Editor](http://editor.swagger.io/).

I have not had opportunity to test the output with the recently released [Code Gen](https://swagger.io/swagger-codegen/) for OpenApi3.

## Support ##

I will try to prioritize handling of bugfix and feature submissions.

But I cannot (at this time, at least) commit time to fixing problems that may be reported - unless they happen to be a problem for internal projects as well.

I hope to improve the documentation over time.
As it is, documentation is pretty barren - but I wanted to make the module available to those able to use it in its current form.


## Using the Module ##

NOTE: The module is compiled against Enunciate 2.12.1. It fails building against the latest version (at present 2.13.1).

From Gradle you would do something like this:

    apply plugin: "com.webcohesion.enunciate"

    dependencies {
      enunciate "dk.jyskebank.tooling.enunciate:enunciate-openapi:1.1.x"
    }


For Maven, you would do something like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- pom.xml -->
<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd"
>
    <modelVersion>4.0.0</modelVersion>
    <!-- ... snip ... -->
    <build>
        <!-- ... snip ... -->
        <pluginManagement>
            <!-- ... snip ... -->
            <plugin>
                <groupId>com.webcohesion.enunciate</groupId>
                <artifactId>enunciate-maven-plugin</artifactId>
                <version>2.12.1</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>docs</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <docsDir>${project.build.directory}</docsDir>
                    <encoding>UTF-8</encoding>
                </configuration>
                <dependencies>
                    <!-- Provides OpenAPI 3 generation -->
                    <dependency>
                        <groupId>dk.jyskebank.tooling.enunciate</groupId>
                        <artifactId>enunciate-openapi</artifactId>
                        <version>1.1.2</version>
                    </dependency>
                </dependencies>
            </plugin>
        </pluginManagement>
    </build>
</project>
```

### Enable the Module ###

Your _enunciate.xml_ file should include this to enable the module:
    
	<enunciate>
	  <modules>
	    <openapi disabled="false" />

### Replacing Swagger UI Files ###

The enunciate-openapi jar includes openapi-swagger-ui.zip. This contains the bundled Swagger UI files.

Sometimes there is a need to provide a custom instance of the Swagger UI, such as to provide custom configuration of the SwaggerUI JavaScript object or to set up custom colors.
To do so, download your own copy of Swagger UI, tweak it to your needs, and tell the plugin to use that directory (or zip file) as the "base" using the base attribute in the configuration file. For example:

	<enunciate>
	  <modules>
	    <openapi disabled="false" base="/path/to/my/custom/swagger-ui"/>

You can also use a relative path. In this case, you should know that the base path is relative to $buildDir. So you want something like:

	<enunciate>
	  <modules>
	    <openapi disabled="false" base="../src/main/enunciate/custom-swagger-ui"/>

### Removing 'json_' prefix ###
Enunciate generates output with the prefix 'json_'. To prevent this prefix from showing up in the OpenAPI service definition (openapi.yml), you
can add the flag 'removeObjectPrefix'. The default value is 'false'.

     <enunciate>
	  <modules>
	    <openapi disabled="false" removeObjectPrefix="true"/>

### Excluding examples ###
Enunciate provides the option to include examples of the request/response body, but if you use an Enum in it, the Enum value
is chosen randomly. This can be inconvenient when you are developing because the output will always be different and this makes it hard to spot the changes that you are working on. 
To alleviate this problem you can exclude the examples by setting the flag 'disableExamples=true' on the openapi element in enunciate.xml. The default value is 'false'.

     <enunciate>
	  <modules>
	    <openapi disabled="false" disableExamples="true"/>

## Release ##

When built (with Jyske Bank internal build server), the artifacts are uploaded to Maven Central at the coordinates:

	dk.jyskebank.tooling.enunciate:enunciate-openapi


## Building Module ##

Simply checkout the project and run gradle:

    export JAVA_HOME=/PATH/TO/JDK/8
    gradlew
    
The built module will be in the folder _dist/publish_.

## Testing ##

Each of the test packages below `dk.jyskebank.tools.enunciate.modules.openapi` contains an example application (input) and the expected output (openapi.yml).

The `EnunciateTestGenerator` dynamically creates the tests from existing folders.

Each test will output its results in a folder below `build/test-enunciate`. The output is compared with the expected output.

