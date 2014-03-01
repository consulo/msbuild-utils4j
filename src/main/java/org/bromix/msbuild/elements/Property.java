package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class Property extends AbstractConditionalElement{
    private final String value;
    
    public Property(String name, String value){
        super(name);
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
