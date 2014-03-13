package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of ItemGroup Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/646dk05y.aspx">ItemGroup Element (MSBuild)</a>
 * @see Item
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = { Item.class }
)
public class ItemGroup extends AbstractParentElement implements Conditionable{
    @ElementValue
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
        List<Item> items = new ArrayList<>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Item){
                items.add((Item)element);
            }
        }
        
        return Collections.unmodifiableList(items);
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
