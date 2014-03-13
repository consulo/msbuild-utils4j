package org.bromix.msbuild.elements;

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
public class When extends AbstractParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public When(){
        super("When", Type.When);
    }
    
    public When(Condition condition){
        super("When", Type.When);
        this.condition = condition;
    }
    
    public void add(Choose choose){
        children.add(choose);
    }
    
    public void add(ItemGroup itemGroup){
        children.add(itemGroup);
    }
    
    public void add(PropertyGroup propertyGroup){
        children.add(propertyGroup);
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
