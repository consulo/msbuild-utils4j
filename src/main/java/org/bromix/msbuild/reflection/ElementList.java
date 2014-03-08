package org.bromix.msbuild.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bromix.msbuild.ProjectReader;

/**
 * Used internally for the {@link ProjectReader}.
 * <p>
 * The {@link ProjectReader} will use the annotation to find the field which
 * holds all children.
 * @author Matthias Bromisch
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ElementList {
}
