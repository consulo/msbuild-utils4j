package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ChooseElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public ChooseElement(){
        super("Choose");
    }
    
    public void add(OtherwiseElement otherwiseElement){
        elements.add(otherwiseElement);
    }
    
    public void add(WhenElement whenElement){
        elements.add(whenElement);
    }
}
