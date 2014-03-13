package org.bromix.msbuild;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import org.bromix.msbuild.reflection.ElementList;
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
public class ProjectWriter {
    public void write(Project project, File file) throws ProjectIOException{
        OutputStream stream;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            throw new ProjectIOException(ex);
        }
        write(project, stream);
    }
    
    public void write(Project project, OutputStream stream) throws ProjectIOException{
        Namespace ns = Namespace.getNamespace("http://schemas.microsoft.com/developer/msbuild/2003");
        Element root = createXmlElement(project, ns);
        Document document = new Document(root);
        
        XMLOutputter xmlOutput = new XMLOutputter();
        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(document, stream);
        } catch (IOException ex) {
            throw new ProjectIOException(ex);
        }
    }

    private Element createXmlElement(org.bromix.msbuild.elements.Element msbuildElement, Namespace namespace) throws ProjectIOException {
        Element xmlElement = new Element(msbuildElement.getElementName(), namespace);
        
        writeXmlElementAttributes(xmlElement, msbuildElement);
        writeChildren(xmlElement, msbuildElement, namespace);
        
        return xmlElement;
    }

    private void writeXmlElementAttributes(Element xmlElement, org.bromix.msbuild.elements.Element msbuildElement) throws ProjectIOException {
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(msbuildElement.getClass(), true, ElementValue.class);
        for(Field field : fields){
            ElementValue elementValue = (ElementValue)field.getAnnotation(ElementValue.class);
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Object fieldValueObject = field.get(msbuildElement);
                
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
                    
                    if(elementValue.required() && (value==null || value.isEmpty())){
                        throw new ProjectIOException(String.format("Value '%s' for element '%s' is required", attributeName, msbuildElement.getElementName()));
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
                        throw new ProjectIOException(String.format("Value for element '%s' is required", msbuildElement.getElementName()));
                    }
                    
                    xmlElement.setText(value);
                }
                
                field.setAccessible(isAccessible);
                
            } catch (    IllegalArgumentException | IllegalAccessException ex) {
                throw new ProjectIOException(ex);
            }
        }
    }

    private void writeChildren(Element xmlElement, org.bromix.msbuild.elements.Element msbuildElement, Namespace namespace) throws ProjectIOException {
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(msbuildElement.getClass(), true, ElementList.class);
        if(!fields.isEmpty()){
            Field fieldList = fields.get(0);
            boolean isAccessible = fieldList.isAccessible();
            fieldList.setAccessible(true);
            
            try {
                Object obj = fieldList.get(msbuildElement);
                List<org.bromix.msbuild.elements.Element> children = (List<org.bromix.msbuild.elements.Element>)obj;
                
                for(org.bromix.msbuild.elements.Element element : children){
                    Element child = createXmlElement(element, namespace);
                    xmlElement.addContent(child);
                }
                
            } catch (    IllegalArgumentException | IllegalAccessException ex) {
                throw new ProjectIOException(ex);
            }
            fieldList.setAccessible(isAccessible);
        }
    }
}
