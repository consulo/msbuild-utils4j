package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;

/**
 * Implement a ItemDefinitionGroup-Element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/bb629392.aspx
 * 
 * @author Matthias Bromisch
 */
public class ItemDefinitionGroup extends AbstractParentElement implements Conditionable{
    final private Condition condition;
    
    public ItemDefinitionGroup(){
        super("ItemDefinitionGroup", Type.ItemDefinitionGroup);
        this.condition = new Condition();
    }
    
    public ItemDefinitionGroup(Condition condition){
        super("ItemDefinitionGroup", Type.ItemDefinitionGroup);
        this.condition = condition;
    }
    
    public void add(ItemDefinition item){
        children.add(item);
    }
    
    public List<ItemDefinition> getItems(){
        List<ItemDefinition> items = new ArrayList<ItemDefinition>();
        
        for(Element element : children){
            if(element.getElementType()==Type.ItemDefinition){
                items.add((ItemDefinition)element);
            }
        }
        
        return Collections.unmodifiableList(items);
    }

    public Condition getCondition() {
        return condition;
    }
}
