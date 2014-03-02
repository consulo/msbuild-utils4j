package org.bromix.msbuild.elements;

/**
 * Implementation of Parameter element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ff606257.aspx
 * 
 * @author Matthias Bromisch
 */
public class Parameter extends AbstractElement{
    private String parameterType = "";
    private boolean output = false;
    private boolean required = false;
    
    public Parameter(String name){
        super(name);
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
        this.required = required;
    }
    
    public boolean getRequired(){
        return required;
    }
}
