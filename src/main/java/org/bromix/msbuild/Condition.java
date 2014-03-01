package org.bromix.msbuild;

/**
 *
 * @author Matthias Bromisch
 */
public class Condition {
    private String condition = "";

    public Condition(){
        this.condition = "";
    }
    
    public Condition(String condition){
        this.condition = condition;
    }
    
    public boolean isEmpty(){
        return condition.isEmpty();
    }
    
    @Override
    public String toString(){
        return condition;
    }
}
