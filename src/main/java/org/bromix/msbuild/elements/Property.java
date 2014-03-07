package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementAttribute;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implemenation of Property element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164288.aspx
 * 
 * @author Matthias Bromisch
 */

@ElementDefinition(
        childrenType = ElementDefinition.ChildrenType.NONE
)
public class Property extends Element implements Conditionable{
    private String value = "";
    @ElementAttribute
    private Condition condition = new Condition();
    
    public Property(){   
        super("", Type.Property);
    }
    
    public Property(String name, String value){
        super(name, Type.Property);
        this.value = value;
    }
    
    public Property(String name, String value, Condition condition){
        super(name, Type.Property);
        this.value = value;
        this.condition = condition;
    }
    
    public String getValue(){
        return value;
    }

    public Condition getCondition() {
        return condition;
    }
}
