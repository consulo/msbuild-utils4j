package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Implementation of ItemMetadata element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164284.aspx
 * 
 * @author Matthias Bromisch
 */
public class ItemMetadata extends Element implements Conditionable{
    private final String value;
    private final Condition condition;
    
    public ItemMetadata(String name, String value){
        super(name, Type.ItemMetadata);
        this.value = value;
        this.condition = new Condition();
    }
    
    public ItemMetadata(String name, String value, Condition condition){
        super(name, Type.ItemMetadata);
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
