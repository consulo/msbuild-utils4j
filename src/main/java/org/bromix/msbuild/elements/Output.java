package org.bromix.msbuild.elements;

/**
 * Implementation of Output element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164287.aspx
 * 
 * @author Matthias Bromisch
 */
public class Output extends AbstractConditionalElement{
    private String taskParameter = "";
    private String propertyName = "";
    private String itemName = "";
    
    public Output(String taskParameter){
        super("Output");
        this.taskParameter = taskParameter;
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
}
