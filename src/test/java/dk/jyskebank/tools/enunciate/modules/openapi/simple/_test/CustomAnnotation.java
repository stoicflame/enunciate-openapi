package dk.jyskebank.tools.enunciate.modules.openapi.simple._test;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A custom annotation should be included via specification extension.
 */
@Target({ TYPE, FIELD })
@Retention(RUNTIME)
public @interface CustomAnnotation {
	String custom();
	String[] listValues() default {};
}
