package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of Output Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164287.aspx">Output Element (MSBuild)</a>
 * @author Matthias Bromisch
 */
@ElementDefinition(
)
public class Output extends Element implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    @ElementValue( required = true )
    private String taskParameter = "";
    @ElementValue
    private String propertyName = "";
    @ElementValue
    private String itemName = "";
    
    public Output(){
        super("Output", Type.Output);
    }
    
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

    @Override
    public Condition getCondition() {
        return condition;
    }
}
