package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of TaskBody Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ff606253.aspx">TaskBody Element (MSBuild)</a>
 * 
 * @author Matthias Bromisch
 */
@ElementDefinition(
)
public class TaskBody extends ParentElement implements Conditionable{
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
    
    public ParameterGroup addParameterGroup(){
        ParameterGroup parameterGroup = new ParameterGroup();
        children.add(parameterGroup);
        return parameterGroup;
    }
    
    public TaskBody addTaskBody(){
        return addTaskBody(new Condition());
    }
    
    public TaskBody addTaskBody(Condition condition){
        TaskBody taskBody = new TaskBody(condition);
        children.add(taskBody);
        return taskBody;
    }
    
    public void setEvaluate(boolean enabled){
        this.evaluate = enabled;
    }
    
    public boolean getEvaluate(){
        return evaluate;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
