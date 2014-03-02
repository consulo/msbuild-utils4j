package org.bromix.msbuild.elements;

/**
 * Implementation of TaskBody element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ff606253.aspx
 * 
 * @author Matthias Bromisch
 */
public class TaskBody extends AbstractElement{
    private boolean evaluate = false;
    
    public TaskBody(){
        super("TaskBody");
    }
    
    public void setEvaluate(boolean enabled){
        this.evaluate = enabled;
    }
    
    public boolean getEvaluate(){
        return evaluate;
    }
}
