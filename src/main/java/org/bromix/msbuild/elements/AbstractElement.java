package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public abstract class AbstractElement implements Element{
    String name = "";
    
    public AbstractElement(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
