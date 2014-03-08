package org.bromix.msbuild.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bromix.msbuild.ProjectReader;

/**
 * Annotations to describe the element.
 * <p>
 * The {@link ProjectReader} will use the annotations to understand the hierarchy
 * of a project file and which children are allowed.
 * @see ProjectReader
 * @author Matthias Bromisch
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ElementDefinition {
    enum NameMatching{
        /**
         * The name must match.
         */
        EQUALS,
        
        /**
         * The name could be everything.
         */
        VARIABLE
    };
    /**
     * The name of the element of the file.
     * <p>
     * If the binding is not defined the {@link ProjectReader} will create a binding
     * based on the class name.
     * @return name of the element name.
     */
    String bind() default "";
    
    /**
     * Defines how the element is matched or not.
     * <p>
     * Use <code>EQUALS</code> if the name must match. Use
     * <code>VARIABLE</code> if the name could be everything.
     * @return type of name matching.
     */
    NameMatching nameMatching() default NameMatching.EQUALS;
    
    /**
     * Supported children of an element.
     * <p>
     * Add every supported class of a child element which is supported.
     * @return supported child elements.
     */
    Class[] children() default {};
}
