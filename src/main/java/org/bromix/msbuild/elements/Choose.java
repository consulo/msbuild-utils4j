package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of Choose element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164282.aspx">Choose Element (MSBuild)</a>
 * @see Otherwise
 * @see When
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {
            Otherwise.class,
            When.class
        }
)
public class Choose extends AbstractParentElement{
    public Choose(){
        super("Choose", Type.Choose);
    }
    
    public Otherwise addOtherwise(){
        Otherwise otherwise = new Otherwise();
        children.add(otherwise);
        return otherwise;
    }
    
    public When addWhen(Condition condition){
        When when = new When(condition);
        children.add(when);
        return when;
    }
}
