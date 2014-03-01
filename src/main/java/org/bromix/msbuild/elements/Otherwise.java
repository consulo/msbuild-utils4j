package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class Otherwise extends AbstractElement{
    List<Element> elements = new ArrayList<Element>();
    
    public Otherwise(){
        super("Otherwise");
    }
    
    public void add(ChooseElement chooseElement){
        elements.add(chooseElement);
    }
    
    public void add(ItemGroup itemGroupElement){
        elements.add(itemGroupElement);
    }
    
    public void add(PropertyGroup propertyGroupElement){
        elements.add(propertyGroupElement);
    }
    
    public List<Element> getChildren(){
        return Collections.unmodifiableList(elements);
    }
}
