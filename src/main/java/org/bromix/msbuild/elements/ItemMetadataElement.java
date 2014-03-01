package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class ItemMetadataElement extends AbstractElement{
    private final String value;
    
    public ItemMetadataElement(String name, String value){
        super(name);
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
