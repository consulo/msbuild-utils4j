package org.bromix.msbuild.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Matthias Bromisch
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ElementDefinition {
    enum NameMatching{
        STRICT,
        VARIABLE
    };
    
    String bind() default "";
    NameMatching nameMatching() default NameMatching.STRICT;
    Class[] children() default {};
}
