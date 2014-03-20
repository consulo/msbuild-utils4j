package org.bromix.msbuild;

import java.io.IOException;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectIOException extends IOException{
    public ProjectIOException(String message){
        super(message);
    }
    
    public ProjectIOException(Throwable cause){
        super(cause);
    }
}
