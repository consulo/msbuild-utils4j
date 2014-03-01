package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class ParameterGroup extends AbstractElement{
    public ParameterGroup(){
        super("ParameterGroup");
    }
    
    public void add(Parameter parameter){
        elements.add(parameter);
    }
    
    public void add(TaskBody taskBody){
        elements.add(taskBody);
    }
}
