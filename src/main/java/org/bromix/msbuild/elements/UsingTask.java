package org.bromix.msbuild.elements;

import org.bromix.msbuild.ParentElement;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of UsingTask Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/t41tzex2.aspx">UsingTask Element (MSBuild)</a>
 * @see ParameterGroup
 * @see TaskBody
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {
            ParameterGroup.class,
            TaskBody.class
        }
)
public class UsingTask extends ParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    @ElementValue
    private String taskName = "";
    @ElementValue
    private String assemblyName = "";
    @ElementValue
    private String assemblyFile = "";
    @ElementValue
    private String taskFactory = "";
    
    public UsingTask(){
        super("UsingTask", Type.UsingTask);
    }
    
    public UsingTask(String taskName){
        super("UsingTask", Type.UsingTask);
        this.taskName = taskName;
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

    @Override
    public Condition getCondition() {
        return condition;
    }
}
