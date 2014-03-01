package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class UsingTaskElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public UsingTaskElement(){
        super("UsingTask");
    }
    
    public void add(ParameterGroupElement parameterGroupElement){
        elements.add(parameterGroupElement);
    }
}
