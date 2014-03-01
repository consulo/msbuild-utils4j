package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 *
 * @author Matthias Bromisch
 */
public class AbstractConditionalElement extends AbstractElement implements Conditionable{
    private Condition condition = new Condition();
    
    public AbstractConditionalElement(String name){
        super(name);
    }
    
    public void setCondition(Condition condition){
        this.condition = condition;
    }
    
    public Condition getCondition(){
        return condition;
    }
}
