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
public class ItemDefinition extends AbstractConditionalElement{
    private final List<Item> items = new ArrayList<Item>();
    
    public ItemDefinition(){
        super("ItemDefinitionGroup");
    }
    
    public void addItem(Item item){
        items.add(item);
    }
    
    public List<Item> getItems(){
        return Collections.unmodifiableList(items);
    }
}
