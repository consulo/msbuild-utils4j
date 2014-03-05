package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Implementation of Output element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164287.aspx
 * 
 * @author Matthias Bromisch
 */
public class Output extends Element implements Conditionable{
    private final Condition condition;
    private String taskParameter = "";
    private String propertyName = "";
    private String itemName = "";
    
    public Output(String taskParameter){
        super("Output", Type.Output);
        this.taskParameter = taskParameter;
        this.condition = new Condition();
    }
    
    public Output(String taskParameter, Condition condition){
        super("Output", Type.Output);
        this.taskParameter = taskParameter;
        this.condition = condition;
    }
    
    public String getTaskParameter(){
        return taskParameter;
    }
    
    public void setPropertyName(String propertyName){
        this.propertyName = propertyName;
        itemName = "";
    }
    
    public String getPropertyName(){
        return propertyName;
    }
    
    public void setItemName(String itemName){
        this.itemName = itemName;
        propertyName = "";
    }
    
    public String getItemName(){
        return itemName;
    }

    public Condition getCondition() {
        return condition;
    }
}
