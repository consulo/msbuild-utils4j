package org.bromix.msbuild;

import org.bromix.msbuild.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.reflection.ElementList;

/**
 * Derived abstract class of {@link Element}
 * <p>
 * This implementation helps to handle children of a particular element type.
 * @see Element
 * @author Matthias Bromisch
 */
public abstract class ParentElement extends Element{
    @ElementList
    protected List<Element> children = new ArrayList<>();
    
    /**
     * Default constructor for a derived element class.
     * @param elementName internal name of the element.
     * @param elementType internal type of the element.
     */
    protected ParentElement(String elementName, Element.Type elementType){
        super(elementName, elementType);
    }
    
    /**
     * Returns a list of all children.
     * @return list of all children.
     * @see Element
     */
    public List<Element> getChildren(){
        return Collections.unmodifiableList(children);
    }
}
