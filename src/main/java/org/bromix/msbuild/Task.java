package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

/**
 * @author Matthias Bromisch
 */
@ElementDefinition(
        nameMatching = ElementDefinition.NameMatching.VARIABLE,
        children = {
            Output.class
        }
)
public class Task extends Element{
    public Task(){
        super("", Type.Task);
    }
    
    public Task(String name){
        super(name, Type.Task);
    }
}
