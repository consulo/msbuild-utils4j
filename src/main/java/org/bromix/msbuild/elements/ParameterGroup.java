package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ParameterGroup extends AbstractElement{
    private final List<Element> elements = new ArrayList<Element>();
    
    public ParameterGroup(){
        super("ParameterGroup");
    }
    
    public void add(Parameter parameterElement){
        elements.add(parameterElement);
    }
    
    public void add(TaskBody taskBodyElement){
        elements.add(taskBodyElement);
    }
}
