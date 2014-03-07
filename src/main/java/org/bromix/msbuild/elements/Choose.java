package org.bromix.msbuild.elements;

import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of Choose element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164282.aspx
 * 
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {
            Otherwise.class,
            When.class
        }
)
public class Choose extends AbstractParentElement{
    public Choose(){
        super("Choose", Type.Choose);
    }
    
    public void add(Otherwise otherwise){
        children.add(otherwise);
    }
    
    public void add(When when){
        children.add(when);
    }
}
