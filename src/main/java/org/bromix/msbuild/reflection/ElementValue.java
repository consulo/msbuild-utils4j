package org.bromix.msbuild.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to bind fields with attributes or element values.
 * @author Matthias Bromisch
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ElementValue {
    enum ValueType{
        /**
         * Bind field with an attribute.
         */
        ATTRIBUTE,
        
        /**
         * Bind field with the value of an element.
         */
        ELEMENT_TEXT
    };
    
    /**
     * The of the attribute.
     * <p>
     * This will be ignored of the <code>ValueType</code> is <code>ELEMENT_TEXT</code>
     * @see ElementValue#valueType()
     * @return 
     */
    String bind() default "";
    
    /**
     * Sets the type of binding.
     * @see ValueType
     * @return 
     */
    ValueType valueType() default ValueType.ATTRIBUTE;
    
    /**
     * Defines if the attribute if required.
     * @return <code>true</code> if the attribute is required otherwise <code>false</code>
     */
    boolean required() default false;
}
