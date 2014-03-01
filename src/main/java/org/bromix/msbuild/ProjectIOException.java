package org.bromix.msbuild;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectIOException extends Exception{
    public ProjectIOException(String message){
        super(message);
    }
    
    public ProjectIOException(Throwable cause){
        super(cause);
    }
}
