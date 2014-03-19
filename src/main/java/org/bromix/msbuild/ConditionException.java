package org.bromix.msbuild;

/**
 *
 * @author Matthias Bromisch
 */
public class ConditionException extends Exception{
    public ConditionException(Throwable cause){
        super(cause);
    }
    
    public ConditionException(String message){
        super(message);
    }
}
