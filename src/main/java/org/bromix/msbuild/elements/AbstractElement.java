package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public abstract class AbstractElement implements Element{
    protected List<Element> elements = new ArrayList<Element>(); 
    protected String elementName = "";
    protected String label = "";
    
    /**
     * Default constructor to create an element with the given name.
     * @param elementName name of the element
     */
    public AbstractElement(String elementName){
        this.elementName = elementName;
    }
    
    public String getElementName(){
        return elementName;
    }
    
    public void setLabel(String label){
        this.label = label;
    }
    
    public String getLabel(){
        return label;
    }
    
    public List<Element> getChildren(){
        return Collections.unmodifiableList(elements);
    }
}
