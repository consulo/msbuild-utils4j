package org.bromix.msbuild.elements;

/**
 * Implementation of Choose element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164282.aspx
 * 
 * @author Matthias Bromisch
 */
public class Choose extends AbstractElement{
    public Choose(){
        super("Choose");
    }
    
    public void add(Otherwise otherwise){
        elements.add(otherwise);
    }
    
    public void add(When when){
        elements.add(when);
    }
}
