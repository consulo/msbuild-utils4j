package org.bromix.msbuild.elements;

/**
 * Implementation of OnError element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164285.aspx
 * 
 * @author Matthias Bromisch
 */
public class OnError extends AbstractConditionalElement{
    private String executeTargets = ""; // required
    
    public OnError(String executeTargets){
        super("OnError");
        this.executeTargets = executeTargets;
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
}
