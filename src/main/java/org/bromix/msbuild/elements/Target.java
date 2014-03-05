package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Implementation of Target element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/t50z2hka.aspx
 * 
 * @author Matthias Bromisch
 */
public class Target extends AbstractParentElement implements Conditionable{
    final private Condition condition;
    
    private String name = "";
    private String inputs = "";
    private String outputs = "";
    private String returns = "";
    private boolean keepDuplicateOutputs = false;
    private String beforeTargets = "";
    private String afterTargets = "";
    private String dependsOnTargets = "";
    
    public Target(String name){
        super("Target", Type.Target);
        this.name = name;
        this.condition = new Condition();
    }
    
    public Target(String name, Condition condition){
        super("Target", Type.Target);
        this.name = name;
        this.condition = condition;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setInputs(String inputs){
        this.inputs = inputs;
    }
    
    public String getInputs(){
        return inputs;
    }
    
    public void setOutputs(String outputs){
        this.outputs = outputs;
    }
    
    public String getOutputs(){
        return outputs;
    }
    
    public void setReturns(String returns){
        this.returns = returns;
    }
    
    public String getReturns(){
        return returns;
    }
    
    public void setKeepDuplicateOutputs(boolean enabled) {
        this.keepDuplicateOutputs = enabled;
    }
    
    public boolean getKeepDuplicateOutputs(){
        return keepDuplicateOutputs;
    }
    
    public void setBeforeTargets(String beforeTargets){
        this.beforeTargets = beforeTargets;
    }
    
    public String getBeforeTargets(){
        return beforeTargets;
    }
    
    public void setAfterTargets(String afterTargets){
        this.afterTargets = afterTargets;
    }
    
    public String getAfterTargets(){
        return afterTargets;
    }
    
    public void setDependsOnTargets(String dependsOnTargets){
        this.dependsOnTargets = dependsOnTargets;
    }
    
    public String getDependsOnTargets(){
        return dependsOnTargets;
    }
    
    public void add(Task task){
        children.add(task);
    }
    
    public void add(PropertyGroup propertyGroup){
        children.add(propertyGroup);
    }
    
    public void add(ItemGroup itemGroup){
        children.add(itemGroup);
    }
    
    public void add(OnError onError){
        children.add(onError);
    }

    public Condition getCondition() {
        return condition;
    }
}
