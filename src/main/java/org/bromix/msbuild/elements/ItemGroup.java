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
public class ItemGroup extends AbstractElement{
    private final List<Item> items = new ArrayList<Item>();
    
    public ItemGroup(){
        super("ItemGroup");
    }
    
    public void addItem(Item item){
        items.add(item);
    }
    
    public List<Item> getItems(){
        return Collections.unmodifiableList(items);
    }
}
