package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.elements.annotations.ElementAttribute;
import org.bromix.msbuild.elements.annotations.ElementDefinition;

/**
 * Implementation of a ItemGroup element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/646dk05y.aspx
 * 
 * @author Matthias Bromisch
 */
@ElementDefinition(
        name = "ItemGroup",
        children = { Item.class }
)
public class ItemGroup extends AbstractParentElement implements Conditionable{
    @ElementAttribute
    private final Condition condition;
    
    public ItemGroup(){
        super("ItemGroup", Type.ItemGroup);
        this.condition = new Condition();
    }
    
    public ItemGroup(Condition condition){
        super("ItemGroup", Type.ItemGroup);
        this.condition = condition;
    }
    
    public void add(Item item){
        children.add(item);
    }
    
    public List<Item> getItems(){
        List<Item> items = new ArrayList<Item>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Item){
                items.add((Item)element);
            }
        }
        
        return Collections.unmodifiableList(items);
    }

    public Condition getCondition() {
        return condition;
    }
}
