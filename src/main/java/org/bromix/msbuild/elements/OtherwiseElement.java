package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class OtherwiseElement extends AbstractElement{
    List<Element> elements = new ArrayList<Element>();
    
    public OtherwiseElement(){
        super("Otherwise");
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
