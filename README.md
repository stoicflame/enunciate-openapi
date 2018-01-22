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

It would make sense to release the built versions of the module on Maven Central.
I will get to that, eventually.

For now, you will have to build the project yourself, or use the builds I make available on GitHub.

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
        classpath "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.8.9"

        classpath files("path/to/enunciate-openapi-n.n.n.jar")
      }
    }

I have changed the [Gradle Enunciate plugin](https://github.com/stoicflame/enunciate-gradle) to allow specifying additional modules.
But this will not be available until next release (2.11).


I am unable to help Maven users. But if you know how to make it work, please let me know, so I can insert instructions here.

## Enable the Module ##

Your _enunciate.xml_ file should include this to enable the module:
    
    <openapi disabled="false" />



