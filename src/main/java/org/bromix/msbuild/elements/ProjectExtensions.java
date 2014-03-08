package org.bromix.msbuild.elements;

import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of ProjectExtensions Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ycwcwzs7.aspx">ProjectExtensions Element (MSBuild)</a>
 * @author Matthias Bromisch
 */
@ElementDefinition(
)
public class ProjectExtensions extends Element{
    public ProjectExtensions(){
        super("ProjectExtensions", Type.ProjectExtension);
    }
}
