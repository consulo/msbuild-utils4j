package org.bromix.msbuild;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Condition attribute of MSBuild.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/7szfhaft.aspx">MSBuild Conditions</a>
 * @author Matthias Bromisch
 */
public class Condition{
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
        Map<String, String> properties = new HashMap<String, String>();
        return evaluate(properties);
    }
    
    /**
     * Evaluates this condition based on the given context.
     * @param properties
     * @return <code>true</code> or <code>false</code> based on the condition.
     * @throws ConditionException 
     */
    public boolean evaluate(Map<String, String> properties) throws ConditionException{
        // always return true if the condition is empty
        if(condition.isEmpty()){
            return true;
        }
        
        // this will initialize the engine once
        initEngine();
        
        /*
        first we have to fix the condition for possibles errors made by
        Microsoft Visual Studio.
        */
        ConditionFix fix = new ConditionFix(condition);
        String _condition = fix.getFixedCondition();
        
        // normalize the condition for JEXL
        _condition = normalizeCondition(_condition);
        
        // replace all properties
        for(String key : properties.keySet()){
            String name = String.format("$(%s)", key);
            _condition = _condition.replace(name, properties.get(key));
        }
        
        // create an expression of the condition
        Expression expression = jexlEngine.createExpression(_condition);
        
        // evaluate
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
     * Normalize the condition to work properly with JEXL.
     * @param _condition
     * @return normalized condition for JEXL.
     */
    private String normalizeCondition(String _condition) {
        String result = _condition;
        
        // Normalize methods of 'exists' and 'hasTrailingSlash'
        result = result.replaceAll("(?i)Exists\\(", "msbuild:exists(");
        result = result.replaceAll("(?i)HasTrailingSlash\\(", "msbuild:hasTrailingSlash(");
        
        // normalize 'and' and 'or'
        result = result.replaceAll("(?i)and", "and");
        result = result.replaceAll("(?i)or", "or");
        
        // normalize backslashes
        result = result.replace("\\", "/");
        
        return result;
    }
    
    /**
     * Initializes the engine for jexl only once
     */
    private void initEngine(){
        if(jexlEngine==null){
            jexlEngine = new JexlEngine();
            jexlEngine.setStrict(true);
            
            Map<String, Object> functions = new HashMap<String, Object>();
            functions.put("msbuild", new MSBuildFunctions());
            jexlEngine.setFunctions(functions);
        }
    }
    
    /**
     * Class to implement methods for MSBuild Conditions.
     * This class provides the functions <code>hasTrailingSlash</code> and
     * <code>exists</code>
     */
    public class MSBuildFunctions{
        public Boolean hasTrailingSlash(String text){
            if(text.endsWith("\\")){
                return true;
            }
            
            return text.endsWith("/");
        }
        
        public Boolean exists(String path){
            File file = new File(path);
            return file.exists();
        }
    };
    
    /**
     * Class to correct some mistakes of Microsoft Visual Studio.
     * Microsoft Visual Studio provides some import projects which have false
     * conditions. Some conditions haven't correct declared strings to be
     * validated.
     * <p>
     * For example:
     * <p>
     * <code>$(EnableManagedIncrementalBuild)==''</code>
     * The property <code>$(EnableManagedIncrementalBuild)</code> isn't enclosed
     * in '''. This class tries to correct them.
     */
    public static class ConditionFix{
        private final String condition;
        private InputStream stream;
        
        public ConditionFix(String condition){
            this.condition = condition;
        }
        
        /**
         * Returns a corrected condition.
         * Fixes only incorrect string declarations.
         * @return
         * @throws ConditionException 
         */
        public String getFixedCondition() throws ConditionException{
            String result = "";
            try {
                stream = new ByteArrayInputStream(condition.getBytes("UTF-8"));
                int ch;
                while( (ch = stream.read()) > -1){
                    if(ch=='$'){
                        result+=parseProperty();
                    }
                    else if(ch=='\''){
                        result+=parseString();
                    }
                    else{
                        result+=(char)ch;
                    }
                }
            } catch (IOException ex) {
                throw new ConditionException(ex);
            }
            
            return result;
        }
        
        /**
         * Returns the property enclosed in '''.
         * @return
         * @throws ConditionException 
         */
        private String parseProperty() throws ConditionException{
            String property = "'$(";
            
            int ch;
            try {
                ch = stream.read();
                if(ch!='('){
                    throw new ConditionException("'(' expected for property");
                }
                while( ((ch = stream.read()) > -1) && ch!=')' ){
                    property+=(char)ch;
                }
            } catch (IOException ex) {
                throw new ConditionException(ex);
            }
            
            property+=")'";
            return property;
        }
        
        /**
         * Returns the complete String.
         * This method is for completion, so we don't parse properties enclosed
         * in '''.
         * @return
         * @throws ConditionException 
         */
        private String parseString() throws ConditionException{
            String string = "'";
            
            int ch;
            try {
                while( ((ch = stream.read()) > -1) && ch!='\'' ){
                    string+=(char)ch;
                }
            } catch (IOException ex) {
                throw new ConditionException(ex);
            }
            
            string+='\'';
            return string;
        }
    };
}
