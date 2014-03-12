package org.bromix.msbuild;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementList;
import org.bromix.msbuild.reflection.ElementName;
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
public class ProjectReader {
    /**
     * Reads the project from the given file.
     * @param file
     * @return instance of the project
     * @throws ProjectIOException 
     */
    public Project read(File file) throws ProjectIOException{
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            throw new ProjectIOException(ex);
        }
        
        return read(inStream);
    }
    
    /**
     * Reads the project from the given stream.
     * @param inStream
     * @return instance of the project.
     * @throws ProjectIOException 
     */
    public Project read(InputStream inStream) throws ProjectIOException{
        SAXBuilder builder = new SAXBuilder();
        builder.setJDOMFactory(new LocatedJDOMFactory());
        Document document = null;
        try {
            document = builder.build(inStream);
        } catch (JDOMException ex) {
            throw new ProjectIOException(ex);
        } catch (IOException ex) {
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
        
        Object obj = readElement(root);
        if(obj.getClass().isAssignableFrom(Project.class)){
            return (Project)obj;
        }
        
        throw new ProjectIOException("Something went really wrong!");
    }
    
    private org.bromix.msbuild.elements.Element readElement(LocatedElement element) throws ProjectIOException{
        Class elementClass = ReflectionHelper.findClassForElement(element.getName());
        if(elementClass==null){
            throw new ProjectIOException(String.format("Unknown element '%s' in line '%d'", element.getName(), element.getLine()));
        }
        return readElement(element, elementClass);
    }
    
    /**
     * Tries to read the MSBuild Element from the given xml-element.
     * @param element xml-element.
     * @param elementClass MSBuild Element Class.
     * @return instance of a MSBuild Element.
     * @throws ProjectIOException 
     */
    public org.bromix.msbuild.elements.Element readElement(LocatedElement element, Class<? extends org.bromix.msbuild.elements.Element> elementClass) throws ProjectIOException{
        Object elementObject;
        try {
            elementObject = elementClass.newInstance();
        } catch (InstantiationException ex) {
            throw new ProjectIOException(ex);
        } catch (IllegalAccessException ex) {
            throw new ProjectIOException(ex);
        }
        
        readElementName(elementObject, element);
        
        readElementAttributes(elementObject, element);
        
        readChildren(elementObject, element);
        
        return (org.bromix.msbuild.elements.Element)elementObject;
    }
    
    private void readElementName(Object elementObject, LocatedElement element) throws ProjectIOException{
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(elementObject.getClass(), true, ElementName.class);
        if(!fields.isEmpty()){
            Field field = fields.get(0);
            
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);

            // we use an object for the value and validate each type we know and need
            Object valueObject = null;
            if(field.getType().isAssignableFrom(String.class)){
                valueObject = element.getName();
            }

            try {
                field.set(elementObject, valueObject);
            } catch (IllegalArgumentException ex) {
                throw new ProjectIOException(ex);
            } catch (IllegalAccessException ex) {
                throw new ProjectIOException(ex);
            }
            
            field.setAccessible(isAccessible);
        }
        else{
            throw new ProjectIOException(String.format("Missing '%s' annotation in '%s'", ElementName.class.getSimpleName(), elementObject.getClass().getSimpleName()));
        }
    }

    private void readElementAttributes(Object elementObject, LocatedElement element) throws ProjectIOException {
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(elementObject.getClass(), true, ElementValue.class);
        for(Field field : fields){
            // enter the field :)
            ElementValue elementValue = (ElementValue)field.getAnnotation(ElementValue.class);
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            
            String value = null;
            if(elementValue.valueType()==ElementValue.ValueType.ELEMENT_TEXT){
                 value = element.getValue();
                 // is the attribute required?
                if(elementValue.required() && value.isEmpty()){
                    throw new ProjectIOException(String.format("Missing value for element '%s' at line '%d'", element.getName(), element.getLine()));
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
                value = element.getAttributeValue(attributeName, "");
                
                // is the attribute required?
                if(elementValue.required() && value.isEmpty()){
                    throw new ProjectIOException(String.format("Missing value '%s' for element '%s' at line '%d'", attributeName, element.getName(), element.getLine()));
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
                field.set(elementObject, valueObject);
            } catch (IllegalArgumentException ex) {
                throw new ProjectIOException(ex);
            } catch (IllegalAccessException ex) {
                throw new ProjectIOException(ex);
            }
            
            field.setAccessible(isAccessible);
        }
    }
    
    private List<org.bromix.msbuild.elements.Element> getChildrenList(Object parentObject) throws ProjectIOException{
        List<org.bromix.msbuild.elements.Element> list = null;
        
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(parentObject.getClass(), true, ElementList.class);
        if(!fields.isEmpty()){
            Field fieldList = fields.get(0);
            boolean isAccessible = fieldList.isAccessible();
            fieldList.setAccessible(true);
            
            Object obj=null;
            try {
                obj = fieldList.get(parentObject);
            } catch (IllegalArgumentException ex) {
                throw new ProjectIOException(ex);
            } catch (IllegalAccessException ex) {
                throw new ProjectIOException(ex);
            }
            fieldList.setAccessible(isAccessible);
            list = (List<org.bromix.msbuild.elements.Element>)obj;
        }
        
        if(list==null){
            throw new ProjectIOException(String.format("Missing '%s' annotation in '%s'", ElementList.class.getSimpleName(), parentObject.getClass().getSimpleName()));
        }
        
        return list;
    }

    private void readChildren(Object parentObject, LocatedElement parentElement) throws ProjectIOException {
        if(!parentElement.getChildren().isEmpty()){
            ElementDefinition parentDefinition = (ElementDefinition)parentObject.getClass().getAnnotation(ElementDefinition.class);
            
            // collect all strict and variable children
            List<String> childNames = new ArrayList<String>();
            List<Class> childClasses = new ArrayList<Class>();
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
            
            List<org.bromix.msbuild.elements.Element> childrenList = getChildrenList(parentObject);
            
            // try to read the children
            for(Element _element : parentElement.getChildren()){
                LocatedElement childElement = (LocatedElement)_element;
                Object childObject = null;
                
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
                    childrenList.add((org.bromix.msbuild.elements.Element)childObject);
                }
                else{
                    throw new ProjectIOException(String.format("Unknown element '%s' in line '%d'", childElement.getName(), childElement.getLine()));
                }
            }
        }
    }
}
