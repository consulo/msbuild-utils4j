package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;

/**
 * Implementation of Task element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/77f2hx1s.aspx
 * 
 * @author Matthias Bromisch
 */
public class Task extends AbstractParentElement implements Conditionable{
    protected Condition condition;
    
    public Task(String name){
        super(name, Type.Task);
        this.condition = new Condition();
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
