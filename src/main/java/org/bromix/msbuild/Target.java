package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of Target Element.
 * <p>
 * At the moment Task Elements are not completed yet.
 * @see <a href="http://msdn.microsoft.com/en-us/library/t50z2hka.aspx">Target Element (MSBuild)</a>
 * @see PropertyGroup
 * @see ItemGroup
 * @see OnError
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {
            PropertyGroup.class,
            ItemGroup.class,
            OnError.class,
            Task.class
        }
)
public class Target extends ParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    @ElementValue( required =  true )
    private String name = "";
    @ElementValue
    private String inputs = "";
    @ElementValue
    private String outputs = "";
    @ElementValue
    private String returns = "";
    @ElementValue
    private boolean keepDuplicateOutputs = false;
    @ElementValue
    private String beforeTargets = "";
    @ElementValue
    private String afterTargets = "";
    @ElementValue
    private String dependsOnTargets = "";
    
    public Target(){
        super("Target", Type.Target);
    }
    
    public Target(String name){
        super("Target", Type.Target);
        this.name = name;
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

    @Override
    public Condition getCondition() {
        return condition;
    }
}
