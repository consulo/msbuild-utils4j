package org.bromix.msbuild;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bromix.msbuild.elements.Import;
import org.bromix.msbuild.elements.ImportGroup;
import org.bromix.msbuild.elements.ItemDefinition;
import org.bromix.msbuild.elements.Item;
import org.bromix.msbuild.elements.ItemGroup;
import org.bromix.msbuild.elements.ItemMetadata;
import org.bromix.msbuild.elements.Property;
import org.bromix.msbuild.elements.PropertyGroup;
import org.jdom2.Attribute;
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
    public Project read(File file) throws ProjectIOException{
        
        SAXBuilder builder = new SAXBuilder();
        builder.setJDOMFactory(new LocatedJDOMFactory());
        Document document = null;
        try {
            document = builder.build(file);
        } catch (JDOMException ex) {
            throw new ProjectIOException(ex);
        } catch (IOException ex) {
            throw new ProjectIOException(ex);
        }
        
        List<Namespace> list = document.getNamespacesInScope();
        list = document.getNamespacesIntroduced();
        
        /*
        At this point we teste LocatedElement and cast it.
        */
        Element _root = document.getRootElement();
        LocatedElement root = null;
        if(_root instanceof LocatedElement){
            root = (LocatedElement)_root;
        }
        if(root==null){
            throw new RuntimeException("Could not cast to LocatedElement");
        }
        
        /*
        We use the namespace of the root element.
        Namespace = http://schemas.microsoft.com/developer/msbuild/2003
        */
        Namespace namespace = root.getNamespace();
        
        Project project = new Project();
        readProject(project, root, namespace);
        return project;
    }
    
    private void readProject(Project project, LocatedElement inElement, Namespace namespace) throws ProjectIOException{
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("DefaultTargets")){
                project.setDefaultTargets(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("InitialTargets")){
                project.setInitialTargets(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("ToolsVersion")){
                project.setToolsVersion(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("TreatAsLocalProperty")){
                project.setTreatAsLocalProperty(attr.getValue());
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read and validate elements
        for(Element _element : inElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            if(element.getName().equalsIgnoreCase("Choose")){
                String message = String.format("Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
            else if(element.getName().equalsIgnoreCase("Import")){
                Import _import = readImport(element, namespace);
                project.add(_import);
            }
            else if(element.getName().equalsIgnoreCase("ImportGroup")){
                readImportGroup(project, element, namespace);
            }
            else if(element.getName().equalsIgnoreCase("ItemGroup")){
                readItemGroup(project, element, namespace);
            }
            else if(element.getName().equalsIgnoreCase("ProjectExtensions")){
                String message = String.format("Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
            else if(element.getName().equalsIgnoreCase("PropertyGroup")){
                readPropertyGroup(project, element, namespace);
            }
            else if(element.getName().equalsIgnoreCase("ItemDefinitionGroup")){
                readItemDefinitionGroup(project, element, namespace);
            }
            else if(element.getName().equalsIgnoreCase("Target")){
                String message = String.format("Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
            else if(element.getName().equalsIgnoreCase("UsingTask")){
                String message = String.format("Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
            else{
                String message = String.format("Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
        }
    }

    private void readItemGroup(Project project, LocatedElement inElement, Namespace namespace) throws ProjectIOException {
        ItemGroup itemGroup = new ItemGroup();
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Label")){
                itemGroup.setLabel(attr.getValue());
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
     
        // read items
        for(Element _element : inElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Item item = readItem(element, namespace, true);
            itemGroup.addItem(item);
        }
        
        project.add(itemGroup);
    }

    private Item readItem(LocatedElement inElement, Namespace namespace, boolean validateInclude) throws ProjectIOException {
        String include = inElement.getAttributeValue("Include");
        if(validateInclude && include.isEmpty()){
            String message = String.format("Missing attribute 'Include' in line '%d'", inElement.getLine());
            throw new ProjectIOException(message);
        }
        
        Item item = new Item(inElement.getName(), include);
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("include")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Exclude")){
                item.setExclude(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Remove")){
                item.setRemove(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("KeepMetadata")){
                item.setKeepMetadata(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("RemoveMetadata")){
                item.setRemoveMetadata(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("KeepDuplicates")){
                item.setKeepDuplicates(attr.getValue());
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read ItemMetadata
        for(Element _element : inElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            ItemMetadata itemMetadata = readItemMetadata(element, namespace);
            item.addMetadata(itemMetadata);
        }
        
        return item;
    }

    private void readItemDefinitionGroup(Project project, LocatedElement inElement, Namespace namespace) throws ProjectIOException {
        ItemDefinition itemDefinitionGroup = new ItemDefinition();
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Label")){
                itemDefinitionGroup.setLabel(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                Condition condition = new Condition(attr.getValue());
                itemDefinitionGroup.setCondition(condition);
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
     
        // read items
        for(Element _element : inElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Item item = readItem(element, namespace, false);
            itemDefinitionGroup.addItem(item);
        }
        
        project.add(itemDefinitionGroup);
    }

    private void readPropertyGroup(Project project, LocatedElement inElement, Namespace namespace) throws ProjectIOException {
        PropertyGroup propertyGroup = new PropertyGroup();
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Label")){
                propertyGroup.setLabel(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                Condition condition = new Condition(attr.getValue());
                propertyGroup.setCondition(condition);
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        for(Element _element : inElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Property property = readProperty(element, namespace);
            propertyGroup.addProperty(property);
        }
     
        project.add(propertyGroup);
    }

    private Import readImport(LocatedElement inElement, Namespace namespace) throws ProjectIOException {
        String project = inElement.getAttributeValue("Project");
        if(project.isEmpty()){
            String message = String.format("Missing attribute 'Project' in line '%d'", inElement.getLine());
            throw new ProjectIOException(message);
        }
        
        Import _import = new Import(project);
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Label")){
                _import.setLabel(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                Condition condition = new Condition(attr.getValue());
                _import.setCondition(condition);
            }
            else if(attr.getName().equalsIgnoreCase("Project")){
                // do nothing
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return _import;
    }

    private void readImportGroup(Project project, LocatedElement inElement, Namespace namespace) throws ProjectIOException {
        ImportGroup importGroup = new ImportGroup();
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Label")){
                importGroup.setLabel(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                Condition condition = new Condition(attr.getValue());
                importGroup.setCondition(condition);
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        for(Element _element : inElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Import _import = readImport(element, namespace);
            importGroup.addImport(_import);
        }
     
        project.add(importGroup);
    }

    private ItemMetadata readItemMetadata(LocatedElement inElement, Namespace namespace) throws ProjectIOException {
        String name = inElement.getName();
        String value = inElement.getText();
        ItemMetadata itemMetadata = new ItemMetadata(name, value);
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Label")){
                itemMetadata.setLabel(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                Condition condition = new Condition(attr.getValue());
                itemMetadata.setCondition(condition);
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return itemMetadata;
    }

    private Property readProperty(LocatedElement inElement, Namespace namespace) throws ProjectIOException {
        String name = inElement.getName();
        String value = inElement.getText();
        Property property = new Property(name, value);
        
        //Read and validate the attributes
        for(Attribute attr : inElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Label")){
                property.setLabel(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                Condition condition = new Condition(attr.getValue());
                property.setCondition(condition);
            }
            else{
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), inElement.getLine());
                throw new ProjectIOException(message);
            }
        }
     
        return property;
    }
}
