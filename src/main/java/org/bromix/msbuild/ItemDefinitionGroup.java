package org.bromix.msbuild;

import org.bromix.msbuild.ParentElement;
import org.bromix.msbuild.Element;
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
public class ItemDefinitionGroup extends ParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public ItemDefinitionGroup(){
        super("ItemDefinitionGroup", Type.ItemDefinitionGroup);
    }
    
    public ItemDefinitionGroup(Condition condition){
        super("ItemDefinitionGroup", Type.ItemDefinitionGroup);
        this.condition = condition;
    }
    
    public ItemDefinition addItemDefinition(String name){
        return addItemDefinition(name, new Condition());
    }
    
    public ItemDefinition addItemDefinition(String name, Condition condition){
        ItemDefinition itemDefinition = new ItemDefinition(name, condition);
        children.add(itemDefinition);
        return itemDefinition;
    }
    
    public List<ItemDefinition> getItems(){
        List<ItemDefinition> items = new ArrayList<>();
        
        for(Element element : children){
            if(element.getElementType()==Type.ItemDefinition){
                items.add((ItemDefinition)element);
            }
        }
        
        return Collections.unmodifiableList(items);
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
