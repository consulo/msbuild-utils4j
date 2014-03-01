package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implement a ItemDefinitionGroup-Element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/bb629392.aspx
 * 
 * @author Matthias Bromisch
 */
public class ItemDefinitionGroup extends AbstractConditionalElement{
    public ItemDefinitionGroup(){
        super("ItemDefinitionGroup");
    }
    
    public void add(Item item){
        elements.add(item);
    }
    
    public List<Item> getItems(){
        List<Item> items = new ArrayList<Item>();
        
        for(Element element : elements){
            if(element instanceof Item){
                items.add((Item)element);
            }
        }
        
        return Collections.unmodifiableList(items);
    }
}
