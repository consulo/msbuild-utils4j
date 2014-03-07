package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of TaskBody element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ff606253.aspx
 * 
 * @author Matthias Bromisch
 */
@ElementDefinition(
)
public class TaskBody extends AbstractParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    @ElementValue
    private boolean evaluate = false;
    
    public TaskBody(){
        super("TaskBody", Type.TaskBody);
    }
    
    public TaskBody(Condition condition){
        super("TaskBody", Type.TaskBody);
        this.condition = condition;
    }
    
    public void add(ParameterGroup parameterGroup){
        children.add(parameterGroup);
    }
    
    public void add(TaskBody taskBody){
        children.add(taskBody);
    }
    
    public void setEvaluate(boolean enabled){
        this.evaluate = enabled;
    }
    
    public boolean getEvaluate(){
        return evaluate;
    }

    public Condition getCondition() {
        return condition;
    }
}
