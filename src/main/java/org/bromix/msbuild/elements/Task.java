package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;
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
public class Task extends AbstractParentElement implements Conditionable{
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
    
    public void add(Output output){
        children.add(output);
    }
    
    public List<Output> getOutputs(){
        List<Output> outputs = new ArrayList<Output>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Output){
                outputs.add((Output)element);
            }
        }
        
        return Collections.unmodifiableList(outputs);
    }

    public Condition getCondition() {
        return condition;
    }
}
