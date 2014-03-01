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
public class ItemDefinitionGroupElement extends AbstractConditionalElement{
    private List<ItemElement> items = new ArrayList<ItemElement>();
    
    public ItemDefinitionGroupElement(){
        super("ItemDefinitionGroup");
    }
    
    public void add(ItemElement item){
        items.add(item);
    }
    
    public List<ItemElement> getItems(){
        return Collections.unmodifiableList(items);
    }
}
