package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implemenation of Property Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164288.aspx">Property Element (MSBuild)</a>
 * @author Matthias Bromisch
 */

@ElementDefinition(
        nameMatching = ElementDefinition.NameMatching.VARIABLE
)
public class Property extends Element implements Conditionable{
    @ElementValue( valueType = ElementValue.ValueType.ELEMENT_TEXT )
    private String value = "";
    @ElementValue
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
