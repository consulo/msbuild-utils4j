package org.bromix.msbuild;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * Implementation of the Condition attribute of MSBuild.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/7szfhaft.aspx">MSBuild Conditions</a>
 * @author Matthias Bromisch
 */
public class Condition{
    public class MSBuildFunctions{
        public Boolean HasTrailingSlash(String text){
            if(text.endsWith("\\")){
                return true;
            }
            
            return text.endsWith("/");
        }
        
        public Boolean exists(String path){
            return Exists(path);
        }
        
        public Boolean Exists(String path){
            File file = new File(path);
            return file.exists();
        }
    };
    
    static JexlEngine jexlEngine = null;
    
    private String condition = "";

    /**
     * Default constructor.
     * This creates an empty condition which will always return <code>true</code>
     * @see Condition#evaluate()
     */
    public Condition(){
        this.condition = "";
    }
    
    /**
     * Creates an instance of the given condition.
     * @param condition expression of a condition.
     * @see <a href="http://msdn.microsoft.com/en-us/library/7szfhaft.aspx">MSBuild Conditions</a>
     * @see Condition#evaluate()
     */
    public Condition(String condition){
        this.condition = condition;
    }
    
    /**
     * Evaluates this condition
     * @return <code>true</code> or <code>false</code> based on the condition.
     * @throws ConditionException
     */
    public boolean evaluate() throws ConditionException{
        Map<String, String> properties = new HashMap<>();
        return evaluate(properties);
    }
    
    /**
     * Evaluates this condition based on the given context.
     * @param properties
     * @return <code>true</code> or <code>false</code> based on the condition.
     * @throws ConditionException 
     * @see ConditionContext
     */
    public boolean evaluate(Map<String, String> properties) throws ConditionException{
        // allways return true if the condition is empty
        if(condition.isEmpty()){
            return true;
        }
        
        // this will initialize the engine once
        initEngine();
        
        /*
        Jexl can not handle variables in strings. So we make a copy of the
        original condition and replace all defined variables.
        */
        String _condition = condition;
        _condition = _condition.replace("\\", "/");
        _condition = _condition.replace("Or", "or");
        _condition = _condition.replace("And", "and");
        
        for(String key : properties.keySet()){
            String name = String.format("$(%s)", key);
            _condition = _condition.replace(name, properties.get(key));
        }
        Expression expression = jexlEngine.createExpression(_condition);
        
        JexlContext dummyContext = new MapContext();
        try{
            Object obj = expression.evaluate(dummyContext);
            if(obj instanceof Boolean){
                return (Boolean)obj;
            }
        }
        catch(org.apache.commons.jexl2.JexlException ex){
            throw new ConditionException(ex);
        }
        
        return false;
    }
    
    /**
     * Returns <code>true</code> if this condition is empty otherwise <code>false</code>.
     * @return <code>true</code> if this condition is empty otherwise <code>false</code>.
     */
    public boolean isEmpty(){
        return condition.isEmpty();
    }
    
    /**
     * Returns the condition.
     * @return
     */
    @Override
    public String toString(){
        return condition;
    }
    
    /**
     * Initializes the engine for jexl only once
     */
    private void initEngine(){
        if(jexlEngine==null){
            jexlEngine = new JexlEngine();
            jexlEngine.setStrict(true);
            
            Map<String, Object> functions = new HashMap<>();
            functions.put(null, new MSBuildFunctions());
            jexlEngine.setFunctions(functions);
        }
    }
}
