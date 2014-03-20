package org.bromix.msbuild;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ReflectionHelper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Matthias Bromisch
 */
public class MSBuildWriter {
    public void writeProject(Project project, File projectFilename) throws ProjectIOException{
        OutputStream stream;
        try {
            stream = new FileOutputStream(projectFilename);
        } catch (FileNotFoundException ex) {
            throw new ProjectIOException(ex);
        }
        writeProject(project, stream);
    }
    
    public void writeProject(Project project, OutputStream outputStream) throws ProjectIOException{
        Namespace ns = Namespace.getNamespace("http://schemas.microsoft.com/developer/msbuild/2003");
        writeElement(project, outputStream, ns);
    }
    
    public void writeElement(org.bromix.msbuild.Element msbuildElement, OutputStream outputStream) throws ProjectIOException{
        writeElement(msbuildElement, outputStream, Namespace.NO_NAMESPACE);
    }
    
    private void writeElement(org.bromix.msbuild.Element msbuildElement, OutputStream outputStream, Namespace ns) throws ProjectIOException{
        Element root = createXmlElement(msbuildElement, ns);
        Document document = new Document(root);
        
        XMLOutputter xmlOutput = new XMLOutputter();
        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(document, outputStream);
        } catch (IOException ex) {
            throw new ProjectIOException(ex);
        }
    }

    private Element createXmlElement(org.bromix.msbuild.Element msBuildElement, Namespace namespace) throws ProjectIOException {
        Element xmlElement = new Element(msBuildElement.getElementName(), namespace);
        
        writeXmlElementAttributes(xmlElement, msBuildElement);
        
        if(msBuildElement instanceof ParentElement){
            writeChildren(xmlElement, (ParentElement)msBuildElement, namespace);
        }
        
        return xmlElement;
    }

    private void writeXmlElementAttributes(Element xmlElement, org.bromix.msbuild.Element msBuildElement) throws ProjectIOException {
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(msBuildElement.getClass(), true, ElementValue.class);
        for(Field field : fields){
            ElementValue elementValue = (ElementValue)field.getAnnotation(ElementValue.class);
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Object fieldValueObject = field.get(msBuildElement);
                
                if(elementValue.valueType()==ElementValue.ValueType.ELEMENT_ATTRIBUTE){
                    // build the name of the attribute (based on the field name)
                    String attributeName = field.getName();
                    // normalize the filename to camelcase
                    attributeName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
                    // use the binding instead
                    if(elementValue.bind()!=null && !elementValue.bind().isEmpty()){
                        attributeName = elementValue.bind();
                    }
                    
                    String value = null;
                    if(field.getType().isAssignableFrom(String.class)){
                        value = (String)fieldValueObject;
                    }
                    else if(field.getType().isAssignableFrom(Condition.class)){
                        value = ((Condition)fieldValueObject).toString();
                    }
                    else if(field.getType().isAssignableFrom(Boolean.class)){
                        value = ((Boolean)fieldValueObject).toString();
                    }
                    else if(field.getType().isAssignableFrom(boolean.class)){
                        value = ((Boolean)fieldValueObject).toString();
                    }
                    
                    if(elementValue.required() && (value==null || value.isEmpty())){
                        throw new ProjectIOException(String.format("Value '%s' for element '%s' is required", attributeName, msBuildElement.getElementName()));
                    }
                    
                    if(value!=null && !value.isEmpty()){
                        xmlElement.setAttribute(attributeName, value);
                    }
                }else{
                    String value = null;
                    if(field.getType().isAssignableFrom(String.class)){
                        value = (String)fieldValueObject;
                    }
                    
                    if(elementValue.required() && (value==null || value.isEmpty())){
                        throw new ProjectIOException(String.format("Value for element '%s' is required", msBuildElement.getElementName()));
                    }
                    
                    xmlElement.setText(value);
                }
                
                field.setAccessible(isAccessible);
                
            } catch (    IllegalArgumentException | IllegalAccessException ex) {
                throw new ProjectIOException(ex);
            }
        }
    }

    private void writeChildren(Element xmlElement, org.bromix.msbuild.ParentElement msBuildElement, Namespace namespace) throws ProjectIOException {
        for(org.bromix.msbuild.Element element : msBuildElement.getChildren()){
            Element child = createXmlElement(element, namespace);
            xmlElement.addContent(child);
        }
    }
}
