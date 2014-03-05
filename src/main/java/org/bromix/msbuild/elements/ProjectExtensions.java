package org.bromix.msbuild.elements;

/**
 * Implementation of ProjectExtensions element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ycwcwzs7.aspx
 * 
 * @author Matthias Bromisch
 */
public class ProjectExtensions extends Element{
    public ProjectExtensions(){
        super("ProjectExtensions", Type.ProjectExtension);
    }
}
