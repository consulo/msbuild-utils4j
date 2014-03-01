package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class WhenElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public WhenElement(){
        super("When");
    }
    
    public void add(ChooseElement chooseElement){
        elements.add(chooseElement);
    }
    
    public void add(ItemGroupElement itemGroupElement){
        elements.add(itemGroupElement);
    }
    
    public void add(PropertyGroupElement propertyGroupElement){
        elements.add(propertyGroupElement);
    }
}
