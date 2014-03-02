package org.bromix.msbuild;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 *
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
        
        public Boolean Exists(String path){
            File file = new File(path);
            return file.exists();
        }
    };
    
    static JexlEngine jexlEngine = null;
    
    private String condition = "";

    /**
     * Default constructor.
     * Create an empty condition.
     */
    public Condition(){
        this.condition = "";
    }
    
    /**
     * Creates the given condition.
     * @remark http://msdn.microsoft.com/en-us/library/7szfhaft.aspx
     * @param condition 
     */
    public Condition(String condition){
        this.condition = condition;
    }
    
    public boolean evaluate() throws ConditionException{
        return evaluate(new ConditionContext());
    }
    
    public boolean evaluate(ConditionContext context) throws ConditionException{
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
        for(String var : context.getNames()){
            _condition = _condition.replace(var, context.get(var, ""));
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
     * Returns true if the condition is empty otherwise false.
     * @return true if the condition is empty otherwise false.
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
    
    private static boolean hasTrailingSlash(String text){
        if(text.endsWith("/")){
            return true;
        }
        
        return text.endsWith("\\");
    }
    
    /**
     * Initializes the engine for jexl only once
     */
    private void initEngine(){
        if(jexlEngine==null){
            jexlEngine = new JexlEngine();
            jexlEngine.setStrict(true);
            
            Map<String, Object> functions = new HashMap<String, Object>();
            functions.put(null, new MSBuildFunctions());
            jexlEngine.setFunctions(functions);
        }
    }
}
