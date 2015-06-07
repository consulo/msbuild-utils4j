package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ReflectionHelper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Matthias Bromisch
 */
public class MSBuildReader {
    private static Set<Class<?>> elementDefinitions = null;
    
    public MSBuildReader(){
        if(elementDefinitions==null){
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .filterInputsBy(new FilterBuilder().includePackage("org.bromix.msbuild"))
                    .setUrls(ClasspathHelper.forPackage("org.bromix.msbuild"))
                    .setScanners(new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new MethodParameterScanner())
            );
            elementDefinitions = reflections.getTypesAnnotatedWith(ElementDefinition.class);
        }
    }
    
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
        //saxBuilder.setJDOMFactory(new LocatedJDOMFactory());
        Document document = null;
        try {
            document = saxBuilder.build(inputStream);
        } catch (JDOMException ex) {
            throw new ProjectIOException(ex);
        } catch (IOException ex) {
            throw new ProjectIOException(ex);
        }
        
        // At this point we test the support for LocatedElement
        Element root = document.getRootElement();

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
            throw new ProjectIOException("'Project' expected at root");
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
    public <T extends org.bromix.msbuild.Element> T readElement(String xml, Class<? extends org.bromix.msbuild.Element> elementClass) throws ProjectIOException{
        SAXBuilder builder = new SAXBuilder();

        Document document;
        try {
            document = builder.build(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        }
        catch (IOException ex) {
            throw new ProjectIOException(ex);
        }
        catch (JDOMException ex) {
            throw new ProjectIOException(ex);
        }

        Element element = document.getRootElement();
        return readElement(element, elementClass);
    }
    
    /**
     * Tries to read the MSBuild Element from the given xml-element.
     * @param xmlElement xml-element.
     * @param msBuildElementClass MSBuild Element Class.
     * @return instance of a MSBuild Element.
     * @throws ProjectIOException 
     */
    private <T extends org.bromix.msbuild.Element> T readElement(Element xmlElement, Class<? extends org.bromix.msbuild.Element> msBuildElementClass) throws ProjectIOException{
        T msBuildElement;
        try {
            msBuildElement = (T)msBuildElementClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new ProjectIOException(ex);
        } catch (InstantiationException ex) {
            throw new ProjectIOException(ex);
        }
        
        msBuildElement.setElementName(xmlElement.getName());
        
        readElementAttributes(msBuildElement, xmlElement);

        if(msBuildElement instanceof ParentElement){
            readChildren((ParentElement)msBuildElement, xmlElement);
        }
        
        return msBuildElement;
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
    private void readElementAttributes(org.bromix.msbuild.Element msBuildElement, Element xmlElement) throws ProjectIOException {
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
                    throw new ProjectIOException(String.format("Missing value for element '%s'", xmlElement.getName()));
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
                    throw new ProjectIOException(String.format("Missing value '%s' for element '%s'", attributeName, xmlElement.getName()));
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
            } catch (IllegalArgumentException ex) {
                throw new ProjectIOException(ex);
            } catch (IllegalAccessException ex) {
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
    private void readChildren(org.bromix.msbuild.ParentElement msBuildElement, Element xmlElement) throws ProjectIOException {
        if(!xmlElement.getChildren().isEmpty()){
            ElementDefinition parentDefinition = (ElementDefinition)msBuildElement.getClass().getAnnotation(ElementDefinition.class);
            
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
            
            // try to read the children
            for(Element _element : xmlElement.getChildren()){
                Element childElement = _element;
                org.bromix.msbuild.Element childObject = null;
                
                if(!childNames.isEmpty() && childClasses.isEmpty() && childNames.indexOf(childElement.getName())!=-1){
                    Class childClass = findClassForElement(childElement.getName());
                    if(childClass==null){
                        throw new ProjectIOException(String.format("Unknown element '%s'", childElement.getName()));
                    }
                    childObject = readElement(childElement, childClass);
                }
                else if(childNames.isEmpty() && !childClasses.isEmpty()){
                    Class childClass = childClasses.get(0);
                    childObject = readElement(childElement, childClass);
                }
                else{
                    throw new ProjectIOException(String.format("Unknown element '%s'", childElement.getName()));
                }
                
                if(childObject!=null){
                    msBuildElement.addChild(childObject);
                }
                else{
                    throw new ProjectIOException(String.format("Unknown element '%s'", childElement.getName()));
                }
            }
        }
    }
    
    /**
     * Based on the {@link ElementDefinition} annotation this method tries to return
     * the corresponding class for the given name of the element.
     * @param elementName name of the element
     * @return class corresponding to the element
     */
    private Class<? extends org.bromix.msbuild.Element> findClassForElement(String elementName){
        for(Class cls : elementDefinitions){
            ElementDefinition elementDefinition = (ElementDefinition)cls.getAnnotation(ElementDefinition.class);
            
            if(elementDefinition!=null){
                String className = cls.getSimpleName();
                className = className.substring(0, 1).toUpperCase()+className.substring(1);
                if(elementDefinition.bind()!=null &&  !elementDefinition.bind().isEmpty()){
                    className =  elementDefinition.bind();
                }
                
                if(elementName.equals(className)){
                    return cls;
                }
            }
        }
        
        return null;
    }
}
