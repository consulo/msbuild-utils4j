package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of ItemMetadata Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164284.aspx">ItemMetadata Element (MSBuild)</a>
 * @author Matthias Bromisch
 */

@ElementDefinition(
        nameMatching = ElementDefinition.NameMatching.VARIABLE
)
public class ItemMetadata extends Element implements Conditionable{
    @ElementValue( valueType = ElementValue.ValueType.ELEMENT_TEXT )
    private String value = "";
    @ElementValue
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
    
    public String getName(){
        return elementName;
    }
    
    public String getValue(){
        return value;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
