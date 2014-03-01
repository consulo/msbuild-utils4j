package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class UsingTask extends AbstractElement{
    public UsingTask(){
        super("UsingTask");
    }
    
    public void add(ParameterGroup parameterGroup){
        elements.add(parameterGroup);
    }
}
