package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of ItemDefinition.
 * 
 * This doesn't really exists. The reference is a little bit false at this point.
 * Based on the reference children of an ItemDefinitionGroup are based on an
 * Item element.
 * 
 * @author Matthias Bromisch
 */
public class ItemDefinition extends AbstractParentElement{
    public ItemDefinition(String name){
        super(name, Type.ItemDefinition);
    }
    
    public void add(ItemMetadata metadata){
        children.add(metadata);
    }
    
    public List<ItemMetadata> getMetadataList(){
        List<ItemMetadata> metadataList = new ArrayList<ItemMetadata>();
        
        for(Element element : children){
            if(element.getElementType()==Type.ItemMetadata){
                metadataList.add((ItemMetadata)element);
            }
        }
        
        return Collections.unmodifiableList(metadataList);
    }
}
