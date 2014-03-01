package org.bromix.msbuild;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bromix.msbuild.elements.Choose;
import org.bromix.msbuild.elements.Conditionable;
import org.bromix.msbuild.elements.Import;
import org.bromix.msbuild.elements.ImportGroup;
import org.bromix.msbuild.elements.ItemDefinitionGroup;
import org.bromix.msbuild.elements.Item;
import org.bromix.msbuild.elements.ItemGroup;
import org.bromix.msbuild.elements.ItemMetadata;
import org.bromix.msbuild.elements.Otherwise;
import org.bromix.msbuild.elements.Property;
import org.bromix.msbuild.elements.PropertyGroup;
import org.bromix.msbuild.elements.When;
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
        
        return readProject(root);
    }
    
    private Project readProject(LocatedElement projectElement) throws ProjectIOException{
        Project project = new Project();
        
        //Read and validate the attributes
        for(Attribute attr : projectElement.getAttributes()){
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
                String message = String.format("Unsupported attribute '%s' in line '%d'", attr.getName(), projectElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read and validate elements
        for(Element _element : projectElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            if(element.getName().equalsIgnoreCase("Choose")){
                Choose choose = readChoose(element);
                project.add(choose);
            }
            else if(element.getName().equalsIgnoreCase("Import")){
                Import _import = readImport(element);
                project.add(_import);
            }
            else if(element.getName().equalsIgnoreCase("ImportGroup")){
                ImportGroup importGroup = readImportGroup(element);
                project.add(importGroup);
            }
            else if(element.getName().equalsIgnoreCase("ItemGroup")){
                ItemGroup itemGroup = readItemGroup(element);
                project.add(itemGroup);
            }
            else if(element.getName().equalsIgnoreCase("ProjectExtensions")){
                String message = String.format("Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
            else if(element.getName().equalsIgnoreCase("PropertyGroup")){
                PropertyGroup propertyGroup = readPropertyGroup(element);
                project.add(propertyGroup);
            }
            else if(element.getName().equalsIgnoreCase("ItemDefinitionGroup")){
                ItemDefinitionGroup itemDefinitionGroup = readItemDefinitionGroup(element);
                project.add(itemDefinitionGroup);
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
        
        return project;
    }

    private ItemGroup readItemGroup(LocatedElement itemGroupElement) throws ProjectIOException {
        ItemGroup itemGroup = new ItemGroup();
        readBaseElement(itemGroup, itemGroupElement);
        
        //Read and validate the attributes
        for(Attribute attr : itemGroupElement.getAttributes()){
            String message = String.format("(ItemGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), itemGroupElement.getLine());
            throw new ProjectIOException(message);
        }
     
        // read items
        for(Element _element : itemGroupElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Item item = readItem(element, true);
            itemGroup.add(item);
        }
        
        return itemGroup;
    }

    private Item readItem(LocatedElement itemElement, boolean validateInclude) throws ProjectIOException {
        String include = itemElement.getAttributeValue("Include");
        if(validateInclude && include.isEmpty()){
            String message = String.format("(Item) Missing attribute 'Include' in line '%d'", itemElement.getLine());
            throw new ProjectIOException(message);
        }
        itemElement.removeAttribute("Include");
        
        Item item = new Item(itemElement.getName(), include);
        readBaseElement(item, itemElement);
        
        //Read and validate the attributes
        for(Attribute attr : itemElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Exclude")){
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
                String message = String.format("(Item) Unsupported attribute '%s' in line '%d'", attr.getName(), itemElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read ItemMetadata
        for(Element _element : itemElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            ItemMetadata itemMetadata = readItemMetadata(element);
            item.add(itemMetadata);
        }
        
        return item;
    }

    private ItemDefinitionGroup readItemDefinitionGroup(LocatedElement itemDefinitionGroupElement) throws ProjectIOException {
        ItemDefinitionGroup itemDefinitionGroup = new ItemDefinitionGroup();
        readBaseElement(itemDefinitionGroup, itemDefinitionGroupElement);
        
        //Read and validate the attributes
        for(Attribute attr : itemDefinitionGroupElement.getAttributes()){
            String message = String.format("(ItemDefinitionGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), itemDefinitionGroupElement.getLine());
            throw new ProjectIOException(message);
        }
     
        // read items
        for(Element _element : itemDefinitionGroupElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Item item = readItem(element, false);
            itemDefinitionGroup.add(item);
        }
        
        return itemDefinitionGroup;
    }

    private PropertyGroup readPropertyGroup(LocatedElement propertyGroupElement) throws ProjectIOException {
        PropertyGroup propertyGroup = new PropertyGroup();
        readBaseElement(propertyGroup, propertyGroupElement);
        
        //Read and validate the attributes
        for(Attribute attr : propertyGroupElement.getAttributes()){
            String message = String.format("(PropertyGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), propertyGroupElement.getLine());
            throw new ProjectIOException(message);
        }
        
        for(Element _element : propertyGroupElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Property property = readProperty(element);
            propertyGroup.add(property);
        }
     
        return propertyGroup;
    }

    private Import readImport(LocatedElement importElement) throws ProjectIOException {
        String project = importElement.getAttributeValue("Project");
        if(project.isEmpty()){
            String message = String.format("(Import) Missing attribute 'Project' in line '%d'", importElement.getLine());
            throw new ProjectIOException(message);
        }
        importElement.removeAttribute("Project");
        
        Import _import = new Import(project);
        readBaseElement(_import, importElement);
        
        //Read and validate the attributes
        for(Attribute attr : importElement.getAttributes()){
            String message = String.format("(Import) Unsupported attribute '%s' in line '%d'", attr.getName(), importElement.getLine());
            throw new ProjectIOException(message);
        }
        
        return _import;
    }

    private ImportGroup readImportGroup(LocatedElement importGroupElement) throws ProjectIOException {
        ImportGroup importGroup = new ImportGroup();
        readBaseElement(importGroup, importGroupElement);
        
        //Read and validate the attributes
        for(Attribute attr : importGroupElement.getAttributes()){
            String message = String.format("(ImportGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), importGroupElement.getLine());
            throw new ProjectIOException(message);
        }
        
        for(Element _element : importGroupElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Import _import = readImport(element);
            importGroup.add(_import);
        }
     
        return importGroup;
    }

    private ItemMetadata readItemMetadata(LocatedElement itemMetadataElement) throws ProjectIOException {
        String name = itemMetadataElement.getName();
        String value = itemMetadataElement.getText();
        ItemMetadata itemMetadata = new ItemMetadata(name, value);
        readBaseElement(itemMetadata, itemMetadataElement);
        
        //Read and validate the attributes
        for(Attribute attr : itemMetadataElement.getAttributes()){
            String message = String.format("(ItemMetaData) Unsupported attribute '%s' in line '%d'", attr.getName(), itemMetadataElement.getLine());
            throw new ProjectIOException(message);
        }
        
        return itemMetadata;
    }

    private Property readProperty(LocatedElement propertyElement) throws ProjectIOException {
        String name = propertyElement.getName();
        String value = propertyElement.getText();
        Property property = new Property(name, value);
        readBaseElement(property, propertyElement);
        
        //Read and validate the attributes
        for(Attribute attr : propertyElement.getAttributes()){
            String message = String.format("(Property) Unsupported attribute '%s' in line '%d'", attr.getName(), propertyElement.getLine());
            throw new ProjectIOException(message);
        }
     
        return property;
    }

    private Choose readChoose(LocatedElement chooseElement) throws ProjectIOException {
        Choose choose = new Choose();
        readBaseElement(choose, chooseElement);
        
        // read and validate attributes
        for(Attribute attr : chooseElement.getAttributes()){
            String message = String.format("(Choose) Unsupported attribute '%s' in line '%d'", attr.getName(), chooseElement.getLine());
            throw new ProjectIOException(message);
        }
        
        // read children
        for(Element _element : chooseElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            if(element.getName().equalsIgnoreCase("Otherwise")){
                Otherwise otherwise = readOtherwise(element);
                choose.add(otherwise);
            }
            else if(element.getName().equalsIgnoreCase("When")){
                When when = readWhen(element);
                choose.add(when);
            }
            else{
                String message = String.format("(Choose) Unsupported element '%s' in line '%d'", element.getName(), chooseElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return choose;
    }
    
    private void readBaseElement(org.bromix.msbuild.elements.Element element, LocatedElement xmlElement){
        String label = xmlElement.getAttributeValue("Label", "");
        if(!label.isEmpty()){
            element.setLabel(label);
            xmlElement.removeAttribute("Label");
        }
        
        if(element instanceof Conditionable){
            Conditionable conditionable = (Conditionable)element;
            
            String condition = xmlElement.getAttributeValue("Condition", "");
            if(!condition.isEmpty()){
                conditionable.setCondition(new Condition(condition));
                xmlElement.removeAttribute("Condition");
            }
            else{
                conditionable.setCondition(new Condition());
            }
        }
    }

    private Otherwise readOtherwise(LocatedElement otherwiseElement) throws ProjectIOException {
        Otherwise otherwise = new Otherwise();
        readBaseElement(otherwise, otherwiseElement);
        
        // read and validate attributes
        for(Attribute attr : otherwiseElement.getAttributes()){
            String message = String.format("(Otherwise) Unsupported attribute '%s' in line '%d'", attr.getName(), otherwiseElement.getLine());
            throw new ProjectIOException(message);
        }
        
        // read children
        for(Element _element : otherwiseElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            if(element.getName().equalsIgnoreCase("Choose")){
                Choose choose = readChoose(element);
                otherwise.add(choose);
            }
            else if(element.getName().equalsIgnoreCase("ItemGroup")){
                ItemGroup itemGroup = readItemGroup(element);
                otherwise.add(itemGroup);
            }
            else if(element.getName().equalsIgnoreCase("PropertyGroup")){
                PropertyGroup propertyGroup = readPropertyGroup(element);
                otherwise.add(propertyGroup);
            }
            else{
                String message = String.format("(Otherwise) Unsupported element '%s' in line '%d'", element.getName(), otherwiseElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return otherwise;
    }

    private When readWhen(LocatedElement whenElement) throws ProjectIOException {
        When when = new When();
        readBaseElement(when, whenElement);
        
        // read and validate attributes
        for(Attribute attr : whenElement.getAttributes()){
            String message = String.format("(When) Unsupported attribute '%s' in line '%d'", attr.getName(), whenElement.getLine());
            throw new ProjectIOException(message);
        }
        
        // read children
        for(Element _element : whenElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            if(element.getName().equalsIgnoreCase("Choose")){
                Choose choose = readChoose(element);
                when.add(choose);
            }
            else if(element.getName().equalsIgnoreCase("ItemGroup")){
                ItemGroup itemGroup = readItemGroup(element);
                when.add(itemGroup);
            }
            else if(element.getName().equalsIgnoreCase("PropertyGroup")){
                PropertyGroup propertyGroup = readPropertyGroup(element);
                when.add(propertyGroup);
            }
            else{
                String message = String.format("(When) Unsupported element '%s' in line '%d'", element.getName(), whenElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return when;
    }
}
