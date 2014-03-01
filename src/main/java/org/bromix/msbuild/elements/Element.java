package org.bromix.msbuild.elements;

/**
 * This interface represents the base of each element.
 * Every element has at least a name (xml-tag). Also we use the interface for
 * list operations if a class uses children of elements.
 * 
 * @author Matthias Bromisch
 */
public interface Element {
    /**
     * Returns the name of the element.
     * @return name of the element
     */
    public String getName();
    
    /**
     * Sets the label of the element
     * @param label 
     */
    public void setLabel(String label);
    
    /**
     * Returns the label of the element.
     * @return 
     */
    public String getLabel();
}
