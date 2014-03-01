package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public abstract class AbstractElement implements Element{
    private String name = "";
    
    /**
     * Default constructor to create an element with the given name.
     * @param name name of the element
     */
    public AbstractElement(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
