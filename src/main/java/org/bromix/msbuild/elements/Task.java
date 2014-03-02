package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Task element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/77f2hx1s.aspx
 * 
 * @author Matthias Bromisch
 */
public class Task extends AbstractConditionalElement{
    public Task(String name){
        super(name);
    }
    
    public void add(Output output){
        elements.add(output);
    }
    
    public List<Output> getOutputs(){
        List<Output> outputs = new ArrayList<Output>();
        
        for(Element element : elements){
            if(element instanceof Output){
                outputs.add((Output)element);
            }
        }
        
        return Collections.unmodifiableList(outputs);
    }
}
