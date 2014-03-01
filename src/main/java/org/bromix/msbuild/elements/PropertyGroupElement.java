package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class PropertyGroupElement extends AbstractConditionalElement{
    private List<Element> elements = new ArrayList<Element>();
    
    private String label = "";
    
    public PropertyGroupElement(){
        super("PropertyGroup");
    }
    
    /**
     * Set the label of the PropertyGroup.
     * @param label 
     */
    public void setLabel(String label){
        this.label = label;
    }
    
    /**
     * Returns the label of the PropertyGroup.
     * @return 
     */
    public String getLabel(){
        return label;
    }
    
    public void add(PropertyElement propertyElement){
        elements.add(propertyElement);
    }
}
