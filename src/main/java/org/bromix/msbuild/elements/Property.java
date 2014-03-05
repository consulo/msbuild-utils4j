package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Implemenation of Property element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164288.aspx
 * 
 * @author Matthias Bromisch
 */
public class Property extends Element implements Conditionable{
    private final String value;
    private final Condition condition;
    
    public Property(String name, String value){
        super(name, Type.Property);
        this.value = value;
        this.condition = new Condition();
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
