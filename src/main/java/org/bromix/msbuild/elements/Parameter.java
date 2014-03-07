package org.bromix.msbuild.elements;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of Parameter element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ff606257.aspx
 * 
 * @author Matthias Bromisch
 */
@ElementDefinition(
        nameMatching = ElementDefinition.NameMatching.VARIABLE
)
public class Parameter extends Element{
    @ElementValue
    private String parameterType = "";
    @ElementValue
    private boolean output = false;
    @ElementValue
    private boolean required = false;
    
    public Parameter(){
        super("", Type.Parameter);
    }
    
    public Parameter(String name){
        super(name, Type.Parameter);
    }
    
    public void setParameterType(String parameterType){
        this.parameterType = parameterType;
    }
    
    public String getParameterType(){
        return parameterType;
    }
    
    public void setOutput(boolean enabled){
        this.output = enabled;
    }
    
    public boolean getOutput(){
        return output;
    }
    
    public void setRequired(boolean enabled){
        this.required = enabled;
    }
    
    public boolean getRequired(){
        return required;
    }
}
