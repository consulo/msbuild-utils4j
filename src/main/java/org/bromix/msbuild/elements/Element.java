package org.bromix.msbuild.elements;

import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementName;

/**
 * Superclass for MSBuild elements.
 * 
 * @author Matthias Bromisch
 */
public abstract class Element {
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
    
    @ElementValue
    protected String label = "";
    @ElementName
    protected String elementName;
    protected Element.Type elementType; 
    
    /**
     * Create a new element instance of a particular type.
     * @param elementName name of the element (xml tag name).
     * @param elementType particular type
     */
    protected Element(String elementName, Element.Type elementType){
        this.elementName = elementName;
        this.elementType = elementType;
    }
    
    /**
     * Returns the name of the element.
     * @return name of the element
     */
    public String getElementName(){
        return elementName;
    }
    
    /**
     * Each element has an enumerated type expressing the type of element.
     * @return 
     */
    public Element.Type getElementType(){
        return elementType;
    }
    
    public String getLabel(){
        return label;
    }
    
    public void setLabel(String label){
        this.label = label;
    }
}
