package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implement a ItemDefinitionGroup-Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb629392.aspx">ItemDefinitionGroup Element (MSBuild)</a>
 * @see ItemDefinition
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {ItemDefinition.class}
)
public class ItemDefinitionGroup extends AbstractParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public ItemDefinitionGroup(){
        super("ItemDefinitionGroup", Type.ItemDefinitionGroup);
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
