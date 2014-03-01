package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class TargetElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public TargetElement(){
        super("Target");
    }
    
    public void add(TaskElement taskElement){
        elements.add(taskElement);
    }
    
    public void add(PropertyGroupElement propertyGroupElement){
        elements.add(propertyGroupElement);
    }
    
    public void add(ItemGroupElement itemGroupElement){
        elements.add(itemGroupElement);
    }
    
    public void add(OnErrorElement onErrorElement){
        elements.add(onErrorElement);
    }
}
