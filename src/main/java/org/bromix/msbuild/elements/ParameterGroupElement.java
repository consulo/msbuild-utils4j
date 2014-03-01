package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ParameterGroupElement extends AbstractElement{
    private final List<Element> elements = new ArrayList<Element>();
    
    public ParameterGroupElement(){
        super("ParameterGroup");
    }
    
    public void add(ParameterElement parameterElement){
        elements.add(parameterElement);
    }
    
    public void add(TaskBodyElement taskBodyElement){
        elements.add(taskBodyElement);
    }
}
