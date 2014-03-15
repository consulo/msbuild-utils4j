package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;

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
    
    protected String elementName;
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
    
    public void setElementName(String elementName){
        ElementDefinition elementDefinition = this.getClass().getAnnotation(ElementDefinition.class);
        if(elementDefinition!=null){
            // the name must match
            if(elementDefinition.nameMatching()==ElementDefinition.NameMatching.EQUALS){   
                /*
                Create a name to match. First we use the name of the class and
                if we have an binding through the annotation, we use the binding.
                */
                String name = this.getClass().getSimpleName();
                if(elementDefinition.bind()!=null && !elementDefinition.bind().isEmpty()){
                    name = elementDefinition.bind();
                }
                
                if(!name.equals(elementName)){
                    throw new IllegalArgumentException(String.format("Element name '%s' for class '%s' invalid", elementName, this.getClass().getSimpleName()));
                }
            }
        }else{
            throw new RuntimeException(String.format("Missing annotation '%s' for class '%s'", ElementDefinition.class.getSimpleName(), this.getClass().getSimpleName()));
        }
        this.elementName = elementName;
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
