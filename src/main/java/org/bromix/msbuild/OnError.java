package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of OnError Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164285.aspx">OnError Element (MSBuild)</a>
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
    
    public void setExecuteTargets(String executeTargets){
        this.executeTargets = executeTargets;
    }
    
    public String getExecuteTargets(){
        return executeTargets;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
