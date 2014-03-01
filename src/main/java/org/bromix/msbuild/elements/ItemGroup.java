package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of a ItemGroup-Element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/646dk05y.aspx
 * 
 * @author Matthias Bromisch
 */
public class ItemGroup extends AbstractConditionalElement{
    public ItemGroup(){
        super("ItemGroup");
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
