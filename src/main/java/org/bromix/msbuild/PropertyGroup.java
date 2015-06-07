package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of PropertyGroup Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/t4w159bs.aspx">PropertyGroup Element (MSBuild)</a>
 * @see Property
 * @author Matthias Bromisch
 */

@ElementDefinition(
        children = {Property.class}
)
public class PropertyGroup extends ParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public PropertyGroup(){
        super("PropertyGroup", Type.PropertyGroup);
    }
    
    public PropertyGroup(Condition condition){
        super("PropertyGroup", Type.PropertyGroup);
        this.condition = condition;
    }
    
    public Property addProperty(String name, String value){
        return addProperty(name, value, new Condition());
    }
    
    public Property addProperty(String name, String value, Condition condition){
        Property property = new Property(name, value, condition);
        children.add(property);
        return property;
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

    @Override
    public Condition getCondition() {
        return condition;
    }
}
