package org.bromix.msbuild;

import org.bromix.msbuild.ParentElement;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of When Element
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164289.aspx">When Element (MSBuild)</a>
 * @see Choose
 * @see ItemGroup
 * @see PropertyGroup
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {
            Choose.class,
            ItemGroup.class,
            PropertyGroup.class
        }
)
public class When extends ParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public When(){
        super("When", Type.When);
    }
    
    public When(Condition condition){
        super("When", Type.When);
        this.condition = condition;
    }
    
    public Choose addChoose(){
        Choose choose = new Choose();
        children.add(choose);
        return choose;
    }
    
    public ItemGroup addItemGroup(){
        return addItemGroup(new Condition());
    }
    
    public ItemGroup addItemGroup(Condition condition){
        ItemGroup itemGroup = new ItemGroup(condition);
        children.add(itemGroup);
        return itemGroup;
    }
    
    public PropertyGroup addPropertyGroup(){
        return addPropertyGroup(new Condition());
    }
    
    public PropertyGroup addPropertyGroup(Condition condition){
        PropertyGroup propertyGroup = new PropertyGroup(condition);
        children.add(propertyGroup);
        return propertyGroup;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
