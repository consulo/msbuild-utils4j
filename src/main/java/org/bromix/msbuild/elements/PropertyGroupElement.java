package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class PropertyGroupElement extends AbstractConditionalElement{
    private List<PropertyElement> properties = new ArrayList<PropertyElement>();
    
    public PropertyGroupElement(){
        super("PropertyGroup");
    }
    
    public void add(PropertyElement property){
        properties.add(property);
    }
    
    public List<PropertyElement> getProperties(){
        return Collections.unmodifiableList(properties);
    }
}
