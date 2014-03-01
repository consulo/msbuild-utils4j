package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class ItemMetadata extends AbstractConditionalElement{
    private final String value;
    
    public ItemMetadata(String name, String value){
        super(name);
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
