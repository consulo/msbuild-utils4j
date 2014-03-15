package org.bromix.msbuild;

import org.bromix.msbuild.ParentElement;
import org.bromix.msbuild.Element;
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
public class ParameterGroup extends ParentElement{
    public ParameterGroup(){
        super("ParameterGroup", Type.ParameterGroup);
    }
    
    public Parameter addParameter(String name){
        Parameter parameter = new Parameter(name);
        children.add(parameter);
        return parameter;
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
}
