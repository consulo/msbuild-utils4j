package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implemenation of ParameterGroup Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ff606260.aspx">ParameterGroup Element (MSBuild)</a>
 * @see Parameter
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {Parameter.class}
)
public class ParameterGroup extends AbstractParentElement{
    public ParameterGroup(){
        super("ParameterGroup", Type.ParameterGroup);
    }
    
    public List<Parameter> getParameter(){
        List<Parameter> parameters = new ArrayList<>();
        
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
