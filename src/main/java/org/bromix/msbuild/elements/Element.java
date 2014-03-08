package org.bromix.msbuild.elements;

import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementName;

/**
 * Superclass for all elements.
 * 
 * @author Matthias Bromisch
 */
public abstract class Element {
    /**
     * Represents the type of element.
     * Each element is a strict type of a MSBuild element.
     */
    public enum Type{
        Project,
        Choose,
        Otherwise,
        When,
        ItemGroup,
        Item,
        ItemMetadata,
        PropertyGroup,
        Property,
        ImportGroup,
        Import,
        ItemDefinitionGroup,
        ItemDefinition,
        ProjectExtension,
        Target,
        Task,
        Output,
        OnError,
        UsingTask,
        ParameterGroup,
        Parameter,
        TaskBody,
    };
    
    /**
     * Label attribute of the element.
     * <p>
     * Labels are optional and every element could have one.
     */
    @ElementValue
    protected String label = "";
    
    /**
     * The internal name of the element.
     */
    @ElementName
    protected String elementName;
    
    /**
     * The internal type of the element.
     */
    protected Element.Type elementType; 
    
    /**
     * Default constructor for a derived element class.
     * @param elementName internal name of the element.
     * @param elementType internal type of the element.
     */
    protected Element(String elementName, Element.Type elementType){
        this.elementName = elementName;
        this.elementType = elementType;
    }
    
    /**
     * Returns the internal name of the element.
     * @return internal element name.
     */
    public String getElementName(){
        return elementName;
    }
    
    /**
     * Returns the internal type of the element.
     * @return internal element type.
     */
    public Element.Type getElementType(){
        return elementType;
    }
    
    /**
     * Returns the label of the element.
     * @return label of the element.
     */
    public String getLabel(){
        return label;
    }
    
    /**
     * Sets the label of the element.
     * @param label label of the element.
     */
    public void setLabel(String label){
        this.label = label;
    }
}
