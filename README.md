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

## Release ##

When built (with Jyske Bank internal build server), the artifacts are uploaded to Maven Central at the coordinates:

	dk.jyskebank.tooling.enunciate:enunciate-openapi


## Building Module ##

Simply checkout the project and run gradle:

    export JAVA_HOME=/PATH/TO/JDK/8
    gradlew
    
The built module will be in the folder _dist/publish_.

## Using the Module ##

From Gradle you would do something like this:

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "gradle.plugin.com.webcohesion.enunciate:enunciate-gradle:2.10.1"
        classpath "dk.jyskebank.tooling.enunciate:enunciate-openapi:1.0.4"
      }
    }

I have changed the [Gradle Enunciate plugin](https://github.com/stoicflame/enunciate-gradle) to allow specifying additional modules.
But this will not be available until next release (2.11).


I am unable to help Maven users. But if you know how to make it work, please let me know, so I can insert instructions here.

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


