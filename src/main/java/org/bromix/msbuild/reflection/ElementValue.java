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
@Target({ElementType.FIELD})
public @interface ElementValue {
    enum ValueType{
        ATTRIBUTE,
        ELEMENT_TEXT
    };
    
    String bind() default "";
    ValueType valueType() default ValueType.ATTRIBUTE;
    boolean required() default false;
}
