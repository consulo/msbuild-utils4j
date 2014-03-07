package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.reflection.ElementList;

/**
 * Abstract helper class.
 * 
 * @author Matthias Bromisch
 */
public abstract class AbstractParentElement extends Element{
    @ElementList
    protected List<Element> children = new ArrayList<Element>();
    
    protected AbstractParentElement(String elementName, Element.Type elementType){
        super(elementName, elementType);
    }
    
    public List<Element> getChildren(){
        return Collections.unmodifiableList(children);
    }
}
