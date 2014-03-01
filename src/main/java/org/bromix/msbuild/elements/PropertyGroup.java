package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class PropertyGroup extends AbstractConditionalElement{
    public PropertyGroup(){
        super("PropertyGroup");
    }
    
    public void add(Property property){
        elements.add(property);
    }
    
    public List<Property> getProperties(){
        List<Property> properties = new ArrayList<Property>();
        
        for(Element element : elements){
            if(element instanceof Property){
                properties.add((Property)element);
            }
        }
        
        return Collections.unmodifiableList(properties);
    }
}
