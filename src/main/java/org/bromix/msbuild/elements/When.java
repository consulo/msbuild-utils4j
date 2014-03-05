package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;

/**
 *
 * @author Matthias Bromisch
 */
public class When extends Element implements Conditionable{
    private final List<Element> children = new ArrayList<Element>();
    private final Condition condition;
    
    public When(){
        super("When", Type.When);
        this.condition = new Condition();
    }
    
    public When(Condition condition){
        super("When", Type.When);
        this.condition = condition;
    }
    
    public List<Element> getChildren(){
        return Collections.unmodifiableList(children);
    }
    
    public void add(Choose choose){
        children.add(choose);
    }
    
    public void add(ItemGroup itemGroup){
        children.add(itemGroup);
    }
    
    public void add(PropertyGroup propertyGroup){
        children.add(propertyGroup);
    }

    public Condition getCondition() {
        return condition;
    }
}
