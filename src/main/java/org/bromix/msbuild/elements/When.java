package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class When extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public When(){
        super("When");
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
}
