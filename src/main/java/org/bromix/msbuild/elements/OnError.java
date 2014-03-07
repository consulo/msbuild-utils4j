package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of OnError element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164285.aspx
 * 
 * @author Matthias Bromisch
 */
@ElementDefinition(
)
public class OnError extends Element implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    @ElementValue( required = true)
    private String executeTargets = ""; // required
    
    public OnError(){
        super("OnError", Type.OnError);
    }
    
    public OnError(String executeTargets){
        super("OnError", Type.OnError);
        this.executeTargets = executeTargets;
    }
    
    public OnError(String executeTargets, Condition condition){
        super("OnError", Type.OnError);
        this.executeTargets = executeTargets;
        this.condition = condition;
    }
    
    /**
     * The targets to execute if a task fails. Separate multiple targets with semicolons.
     * @param executeTargets 
     */
    public void setExecuteTargets(String executeTargets){
        this.executeTargets = executeTargets;
    }
    
    /**
     * The targets to execute if a task fails. Separate multiple targets with semicolons.
     * @return 
     */
    public String getExecuteTargets(){
        return executeTargets;
    }

    public Condition getCondition() {
        return condition;
    }
}
