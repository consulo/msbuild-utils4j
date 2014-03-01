package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class PropertyElement extends AbstractElement{
    private final String value;
    
    public PropertyElement(String name, String value){
        super(name);
        this.value = value;
    }
}
