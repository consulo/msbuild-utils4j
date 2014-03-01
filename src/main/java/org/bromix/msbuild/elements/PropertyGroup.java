package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class PropertyGroup extends AbstractConditionalElement{
    private final List<Property> properties = new ArrayList<Property>();
    
    public PropertyGroup(){
        super("PropertyGroup");
    }
    
    public void addProperty(Property property){
        properties.add(property);
    }
    
    public List<Property> getProperties(){
        return Collections.unmodifiableList(properties);
    }
}
