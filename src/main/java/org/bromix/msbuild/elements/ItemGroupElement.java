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
public class ItemGroupElement extends AbstractElement{
    private List<ItemElement> items = new ArrayList<ItemElement>();
    
    public ItemGroupElement(){
        super("ItemGroup");
    }
    
    public void add(ItemElement item){
        items.add(item);
    }
    
    public List<ItemElement> getItems(){
        return Collections.unmodifiableList(items);
    }
}
