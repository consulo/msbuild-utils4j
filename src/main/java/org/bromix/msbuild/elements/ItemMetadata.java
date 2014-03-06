package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;
import org.bromix.msbuild.elements.annotations.ElementAttribute;
import org.bromix.msbuild.elements.annotations.ElementDefinition;

/**
 * Implementation of ItemMetadata element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164284.aspx
 * 
 * @author Matthias Bromisch
 */

@ElementDefinition
public class ItemMetadata extends Element implements Conditionable{
    @ElementAttribute
    private String value = "";
    @ElementAttribute
    private Condition condition = new Condition();
    
    public ItemMetadata(){
        super("", Type.ItemMetadata);
    }
    
    public ItemMetadata(String name, String value){
        super(name, Type.ItemMetadata);
        this.value = value;
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
