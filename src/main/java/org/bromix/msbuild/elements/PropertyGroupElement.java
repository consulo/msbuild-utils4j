package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class PropertyGroupElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public PropertyGroupElement(){
        super("PropertyGroup");
    }
    
    public void add(PropertyElement propertyElement){
        elements.add(propertyElement);
    }
}
