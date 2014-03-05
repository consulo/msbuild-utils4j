package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Implementation of UsingTask element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/t41tzex2.aspx
 * 
 * @author Matthias Bromisch
 */
public class UsingTask extends AbstractParentElement implements Conditionable{
    final private Condition condition;
    
    private String taskName = "";
    private String assemblyName = "";
    private String assemblyFile = "";
    private String taskFactory = "";
    
    public UsingTask(String taskName){
        super("UsingTask", Type.UsingTask);
        this.taskName = taskName;
        this.condition = new Condition();
    }
    
    public UsingTask(String taskName, Condition condition){
        super("UsingTask", Type.UsingTask);
        this.taskName = taskName;
        this.condition = condition;
    }
    
    public String getTaskName(){
        return taskName;
    }
    
    public void setAssemblyName(String assemblyName){
        this.assemblyName = assemblyName;
        assemblyFile = "";
    }
    
    public String getAssemblyName(){
        return assemblyName;
    }
    
    public void setAssemblyFile(String assemblyFile){
        this.assemblyFile = assemblyFile;
        assemblyName = "";
    }
    
    public String getAssemblyFile(){
        return assemblyFile;
    }
    
    public void setTaskFactory(String taskFactory){
        this.taskFactory = taskFactory;
    }
    
    public String getTaskFactory(){
        return taskFactory;
    }
    
    public void add(ParameterGroup parameterGroup){
        children.add(parameterGroup);
    }
    
    public void add(TaskBody taskBody){
        children.add(taskBody);
    }

    public Condition getCondition() {
        return condition;
    }
}
