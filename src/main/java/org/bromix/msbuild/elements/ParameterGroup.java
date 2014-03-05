package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implemenation of ParameterGroup element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ff606260.aspx
 * 
 * @author Matthias Bromisch
 */
public class ParameterGroup extends AbstractParentElement{
    public ParameterGroup(){
        super("ParameterGroup", Type.ParameterGroup);
    }
    
    public List<Parameter> getParameter(){
        List<Parameter> parameters = new ArrayList<Parameter>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Parameter){
                parameters.add((Parameter)element);
            }
        }
        
        return Collections.unmodifiableList(parameters);
    }
    
    public void add(Parameter parameter){
        children.add(parameter);
    }
}
