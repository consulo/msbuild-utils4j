package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Matthias Bromisch
 */
public class ConditionContext{
    private final Map<String, String> map = new HashMap<>();
    
    public void set(String name, String value){
        map.put(name, value);
    }
    
    public void remove(String name){
        if(map.containsKey(name)){
            map.remove(name);
        }
    }
    
    public String get(String name, String defaultValue){
        if(map.containsKey(name)){
            return (String)map.get(name);
        }
        
        return defaultValue;
    }
    
    public List<String> getNames(){
        List<String> vars = new ArrayList<>(map.keySet());
        return Collections.unmodifiableList(vars);
    }
}
