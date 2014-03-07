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
    enum ChildrenType{
        NONE,
        MULTIPLE_TYPES,
        ONE_TYPE
    };
    
    String bind() default "";
    ChildrenType childrenType();
    Class[] childrenTypes() default {};
}
