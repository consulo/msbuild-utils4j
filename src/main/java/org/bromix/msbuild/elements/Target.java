package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class Target extends AbstractElement{
    private final List<Element> elements = new ArrayList<Element>();
    
    public Target(){
        super("Target");
    }
    
    public void add(Task taskElement){
        elements.add(taskElement);
    }
    
    public void add(PropertyGroup propertyGroupElement){
        elements.add(propertyGroupElement);
    }
    
    public void add(ItemGroup itemGroupElement){
        elements.add(itemGroupElement);
    }
    
    public void add(OnError onErrorElement){
        elements.add(onErrorElement);
    }
}
