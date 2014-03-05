package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Implementation of TaskBody element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ff606253.aspx
 * 
 * @author Matthias Bromisch
 */
public class TaskBody extends AbstractParentElement implements Conditionable{
    final private Condition condition;
    private boolean evaluate = false;
    
    public TaskBody(){
        super("TaskBody", Type.TaskBody);
        this.condition = new Condition();
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
