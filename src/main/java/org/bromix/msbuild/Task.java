package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementDefinition.NameMatching;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * Implementation of Task element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/77f2hx1s.aspx">Task Element (MSBuild)</a>
 * @author Matthias Bromisch
 */
@ElementDefinition(
        nameMatching = NameMatching.VARIABLE,
        children = {Output.class}
)
public class Task extends ParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public Task(){
        super("", Type.Task);
    }
    
    public Task(String name){
        super(name, Type.Task);
    }
    
    public Task(String name, Condition condition){
        super(name, Type.Task);
        this.condition = condition;
    }
    
    public Output addOutput(String taskParameter){
        return addOutput(taskParameter, new Condition());
    }
    
    public Output addOutput(String taskParameter, Condition condition){
        Output output = new Output(taskParameter, condition);
        children.add(output);
        return output;
    }
    
    public List<Output> getOutputs(){
        List<Output> outputs = new ArrayList<>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Output){
                outputs.add((Output)element);
            }
        }
        
        return Collections.unmodifiableList(outputs);
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
