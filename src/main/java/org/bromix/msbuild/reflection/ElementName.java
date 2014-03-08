package org.bromix.msbuild.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used internally for the {@link ProjectReader}.
 * <p>
 * The {@link ProjectReader} will use the annotation to find the field for the
 * internal element name.
 * @author Matthias Bromisch
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ElementName {
    
}
