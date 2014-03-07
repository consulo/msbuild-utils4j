package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementAttribute;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of PropertyGroup element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/t4w159bs.aspx
 * 
 * @author Matthias Bromisch
 */

@ElementDefinition(
        childrenType = ElementDefinition.ChildrenType.ONE_TYPE,
        childrenTypes = {Property.class}
)
public class PropertyGroup extends AbstractParentElement implements Conditionable{
    @ElementAttribute
    private Condition condition = new Condition();
    
    public PropertyGroup(){
        super("PropertyGroup", Type.PropertyGroup);
    }
    
    public PropertyGroup(Condition condition){
        super("PropertyGroup", Type.PropertyGroup);
        this.condition = condition;
    }
    
    public void add(Property property){
        children.add(property);
    }
    
    public List<Property> getProperties(){
        List<Property> properties = new ArrayList<Property>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Property){
                properties.add((Property)element);
            }
        }
        
        return Collections.unmodifiableList(properties);
    }

    public Condition getCondition() {
        return condition;
    }
}
