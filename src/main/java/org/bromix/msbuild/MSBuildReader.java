package org.bromix.msbuild;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ReflectionHelper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.LocatedElement;
import org.jdom2.located.LocatedJDOMFactory;

/**
 *
 * @author Matthias Bromisch
 */
public class MSBuildReader {
    /**
     * Reads the project from the given file.
     * @param projectFilename
     * @return instance of the project
     * @throws ProjectIOException 
     */
    public Project readProject(File projectFilename) throws ProjectIOException{
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(projectFilename);
        } catch (FileNotFoundException ex) {
            throw new ProjectIOException(ex);
        }
        
        return readProject(inStream);
    }
    
    /**
     * Reads the project from the given stream.
     * @param inputStream
     * @return instance of the project.
     * @throws ProjectIOException 
     */
    public Project readProject(InputStream inputStream) throws ProjectIOException{
        SAXBuilder saxBuilder = new SAXBuilder();
        saxBuilder.setJDOMFactory(new LocatedJDOMFactory());
        Document document = null;
        try {
            document = saxBuilder.build(inputStream);
        } catch (JDOMException | IOException ex) {
            throw new ProjectIOException(ex);
        }
        
        // At this point we test the support for LocatedElement
        Element _root = document.getRootElement();
        LocatedElement root = null;
        if(_root instanceof LocatedElement){
            root = (LocatedElement)_root;
        }
        if(root==null){
            throw new RuntimeException("class LocatedElement not supported");
        }
        
        /*
        We use the namespace of the root element.
        Namespace = http://schemas.microsoft.com/developer/msbuild/2003
        */
        Namespace namespace = root.getNamespace();
        if(!namespace.getURI().equalsIgnoreCase("http://schemas.microsoft.com/developer/msbuild/2003")){
            throw new ProjectIOException(String.format("Unsupported namespace '%s'", namespace.toString()));
        }
        
        // test the root element for '<Project>'
        if(!root.getName().equalsIgnoreCase("Project")){
            throw new ProjectIOException(String.format("'Project' expected at line '%d'", root.getLine()));
        }
        
        org.bromix.msbuild.Element msBuildElement = readElement(root, Project.class);
        if(msBuildElement.getClass().isAssignableFrom(Project.class)){
            return (Project)msBuildElement;
        }
        
        throw new ProjectIOException("Something went really wrong!");
    }
    
    /**
     * Reads an MSBuild Element from the given xml-string.
     * @param xml
     * @param elementClass expected class.
     * @return instance of an MSBuild Element.
     * @throws ProjectIOException 
     */
    public org.bromix.msbuild.Element readElement(String xml, Class<? extends org.bromix.msbuild.Element> elementClass) throws ProjectIOException{
        SAXBuilder builder = new SAXBuilder();
        builder.setJDOMFactory(new LocatedJDOMFactory());
        
        Document document;
        try {
            document = builder.build(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        } catch (JDOMException | IOException ex) {
            throw new ProjectIOException(ex);
        }
        
        LocatedElement element = (LocatedElement)document.getRootElement();
        return readElement(element, elementClass);
    }
    
    /**
     * Tries to read the MSBuild Element from the given xml-element.
     * @param xmlElement xml-element.
     * @param msBuildElementClass MSBuild Element Class.
     * @return instance of a MSBuild Element.
     * @throws ProjectIOException 
     */
    private org.bromix.msbuild.Element readElement(LocatedElement xmlElement, Class<? extends org.bromix.msbuild.Element> msBuildElementClass) throws ProjectIOException{
        org.bromix.msbuild.Element msBuildElement;
        try {
            msBuildElement = (org.bromix.msbuild.Element)msBuildElementClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new ProjectIOException(ex);
        }
        
        msBuildElement.setElementName(xmlElement.getName());
        
        readElementAttributes(msBuildElement, xmlElement);

        if(msBuildElement instanceof ParentElement){
            readChildren((ParentElement)msBuildElement, xmlElement);
        }
        
        return (org.bromix.msbuild.Element)msBuildElement;
    }

    /**
     * Internal method to read the attributes of the given element.
     * This method will only read the attributes defined by the annotation
     * {@link ElementValue}.
     * @param msBuildElement
     * @param xmlElement
     * @throws ProjectIOException 
     * @see ElementValue
     */
    private void readElementAttributes(org.bromix.msbuild.Element msBuildElement, LocatedElement xmlElement) throws ProjectIOException {
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(msBuildElement.getClass(), true, ElementValue.class);
        for(Field field : fields){
            // enter the field :)
            ElementValue elementValue = (ElementValue)field.getAnnotation(ElementValue.class);
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            
            String value;
            if(elementValue.valueType()==ElementValue.ValueType.ELEMENT_TEXT){
                 value = xmlElement.getValue();
                 // is the attribute required?
                if(elementValue.required() && value.isEmpty()){
                    throw new ProjectIOException(String.format("Missing value for element '%s' at line '%d'", xmlElement.getName(), xmlElement.getLine()));
                }
            }
            else{
                // build the name of the attribute (based on the field name)
                String attributeName = field.getName();
                // normalize the filename to camelcase
                attributeName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
                // use the binding instead
                if(elementValue.bind()!=null && !elementValue.bind().isEmpty()){
                    attributeName = elementValue.bind();
                }
                value = xmlElement.getAttributeValue(attributeName, "");
                
                // is the attribute required?
                if(elementValue.required() && value.isEmpty()){
                    throw new ProjectIOException(String.format("Missing value '%s' for element '%s' at line '%d'", attributeName, xmlElement.getName(), xmlElement.getLine()));
                }
            }

            // we use an object for the value and validate each type we know and need
            Object valueObject = null;
            if(field.getType().isAssignableFrom(String.class)){
                valueObject = value;
            }
            else if(field.getType().isAssignableFrom(boolean.class)){
                valueObject = Boolean.parseBoolean(value);
            }
            else if(field.getType().isAssignableFrom(Boolean.class)){
                valueObject = Boolean.parseBoolean(value);
            }
            else if(field.getType().isAssignableFrom(Condition.class)){
                valueObject = new Condition(value);   
            }

            try {
                field.set(msBuildElement, valueObject);
            } catch (    IllegalArgumentException | IllegalAccessException ex) {
                throw new ProjectIOException(ex);
            }
            
            field.setAccessible(isAccessible);
        }
    }

    /**
     * Internal method to read the children of an given element.
     * @param msBuildElement
     * @param xmlElement
     * @throws ProjectIOException 
     */
    private void readChildren(org.bromix.msbuild.ParentElement msBuildElement, LocatedElement xmlElement) throws ProjectIOException {
        if(!xmlElement.getChildren().isEmpty()){
            ElementDefinition parentDefinition = (ElementDefinition)msBuildElement.getClass().getAnnotation(ElementDefinition.class);
            
            // collect all strict and variable children
            List<String> childNames = new ArrayList<>();
            List<Class> childClasses = new ArrayList<>();
            for(Class cls : parentDefinition.children()){
                ElementDefinition childDefinition = (ElementDefinition)cls.getAnnotation(ElementDefinition.class);
                if(childDefinition!=null){
                    if(childDefinition.nameMatching()==ElementDefinition.NameMatching.EQUALS){
                        String childName = cls.getSimpleName();
                        childName = childName.substring(0, 1).toUpperCase()+childName.substring(1);
                        if(childDefinition.bind()!=null && !childDefinition.bind().isEmpty()){
                            childName = childDefinition.bind();
                        }
                        childNames.add(childName);
                    }
                    else if(childDefinition.nameMatching()==ElementDefinition.NameMatching.VARIABLE){
                        childClasses.add(cls);
                    }
                }
            }
            
            // try to read the children
            for(Element _element : xmlElement.getChildren()){
                LocatedElement childElement = (LocatedElement)_element;
                org.bromix.msbuild.Element childObject = null;
                
                if(!childNames.isEmpty() && childClasses.isEmpty() && childNames.indexOf(childElement.getName())!=-1){
                    Class childClass = ReflectionHelper.findClassForElement(childElement.getName());
                    if(childClass==null){
                        throw new ProjectIOException(String.format("Unknown element '%s' in line '%d'", childElement.getName(), childElement.getLine()));
                    }
                    childObject = readElement(childElement, childClass);
                }
                else if(childNames.isEmpty() && !childClasses.isEmpty()){
                    Class childClass = childClasses.get(0);
                    childObject = readElement(childElement, childClass);
                }
                else{
                    throw new ProjectIOException(String.format("Unknown element '%s' in line '%d'", childElement.getName(), childElement.getLine()));
                }
                
                if(childObject!=null){
                    msBuildElement.addChild(childObject);
                }
                else{
                    throw new ProjectIOException(String.format("Unknown element '%s' in line '%d'", childElement.getName(), childElement.getLine()));
                }
            }
        }
    }
}
