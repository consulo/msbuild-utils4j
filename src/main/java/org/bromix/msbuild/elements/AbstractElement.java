package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public abstract class AbstractElement implements Element{
    private String name = "";
    private String label = "";
    
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
    
    public void setLabel(String label){
        this.label = label;
    }
    
    public String getLabel(){
        return label;
    }
}
