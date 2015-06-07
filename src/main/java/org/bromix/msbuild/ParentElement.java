package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Derived abstract class of {@link Element}
 * <p>
 * This implementation helps to handle children of a particular element type.
 * @see Element
 * @author Matthias Bromisch
 */
public abstract class ParentElement extends Element{
    protected List<Element> children = new ArrayList<Element>();
    
    /**
     * Default constructor for a derived element class.
     * @param elementName internal name of the element.
     * @param elementType internal type of the element.
     */
    protected ParentElement(String elementName, Element.Type elementType){
        super(elementName, elementType);
    }
    
    /**
     * Will add an <code>Element</code> to this <code>Element</code>.
     * This method validates each <code>Element</code> based on the <code>ElementDefinition</code>
     * annotation.
     * @param element <code>Element</code> to add.
     * @see ElementDefinition
     * @throws IllegalArgumentException if the implementation of the <code>Element</code> is not supported.
     * @throws RuntimeException if the <code>ElementDefinition</code> is missing.
     */
    public void addChild(Element element){
        /*
        Get the annotation and validate the given element against each supported
        child class.
        */
        ElementDefinition elementDefinition = this.getClass().getAnnotation(ElementDefinition.class);
        if(elementDefinition!=null){
            boolean isOk = false;
            for(Class cls : elementDefinition.children()){
                if(element.getClass().isAssignableFrom(cls)){
                    isOk = true;
                    break;
                }
            }
            
            if(!isOk){
                throw new IllegalArgumentException(String.format("Child element of class '%s' not supported for elemnt '%s'", element.getClass().getSimpleName(), this.getClass().getSimpleName()));
            }
        }
        else{
            // we need the annotation to validate the class of the children.
            throw new RuntimeException(String.format("Missing annotation '%s' for class '%s'", ElementDefinition.class.getSimpleName(), this.getClass().getSimpleName()));
        }
        
        children.add(element);
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
