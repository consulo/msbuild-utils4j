package org.bromix.msbuild;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.bromix.msbuild.elements.Choose;
import org.bromix.msbuild.elements.Conditionable;
import org.bromix.msbuild.elements.Import;
import org.bromix.msbuild.elements.ImportGroup;
import org.bromix.msbuild.elements.ItemDefinitionGroup;
import org.bromix.msbuild.elements.Item;
import org.bromix.msbuild.elements.ItemDefinition;
import org.bromix.msbuild.elements.ItemGroup;
import org.bromix.msbuild.elements.ItemMetadata;
import org.bromix.msbuild.elements.OnError;
import org.bromix.msbuild.elements.Otherwise;
import org.bromix.msbuild.elements.Output;
import org.bromix.msbuild.elements.Parameter;
import org.bromix.msbuild.elements.ParameterGroup;
import org.bromix.msbuild.elements.ProjectExtensions;
import org.bromix.msbuild.elements.Property;
import org.bromix.msbuild.elements.PropertyGroup;
import org.bromix.msbuild.elements.Target;
import org.bromix.msbuild.elements.Task;
import org.bromix.msbuild.elements.TaskBody;
import org.bromix.msbuild.elements.UsingTask;
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
                ProjectExtensions projectExtensions = readProjectExtensions(element);
                project.add(projectExtensions);
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
                Target target = readTarget(element);
                project.add(target);
            }
            else if(element.getName().equalsIgnoreCase("UsingTask")){
                UsingTask usingTask = readUsingTask(element);
                project.add(usingTask);
            }
            else{
                String message = String.format("Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return project;
    }

    private ItemGroup readItemGroup(LocatedElement itemGroupElement) throws ProjectIOException {
        //Read and validate the attributes
        ItemGroup itemGroup = new ItemGroup(new Condition(itemGroupElement.getAttributeValue("Condition", "")));
        for(Attribute attr : itemGroupElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Label")){
                itemGroup.setLabel(attr.getValue());
            }
            else{
                String message = String.format("(ItemGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), itemGroupElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read items
        for(Element _element : itemGroupElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Item item = readItem(element);
            itemGroup.add(item);
        }
        
        return itemGroup;
    }

    private Item readItem(LocatedElement itemElement) throws ProjectIOException {
        String include = itemElement.getAttributeValue("Include");
        if(include.isEmpty()){
            String message = String.format("(Item) Missing attribute 'Include' in line '%d'", itemElement.getLine());
            throw new ProjectIOException(message);
        }
        
        Item item = new Item(itemElement.getName(), include, new Condition(itemElement.getAttributeValue("Condition", "")));
        //Read and validate the attributes
        for(Attribute attr : itemElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Exclude")){
                item.setExclude(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Include")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
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
        ItemDefinitionGroup itemDefinitionGroup = new ItemDefinitionGroup(new Condition(itemDefinitionGroupElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : itemDefinitionGroupElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                //do nothing
            }
            else{
                String message = String.format("(ItemDefinitionGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), itemDefinitionGroupElement.getLine());
                throw new ProjectIOException(message);
            }
        }
     
        // read items
        for(Element _element : itemDefinitionGroupElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            ItemDefinition item = readItemDefinition(element);
            itemDefinitionGroup.add(item);
        }
        
        return itemDefinitionGroup;
    }

    private PropertyGroup readPropertyGroup(LocatedElement propertyGroupElement) throws ProjectIOException {
        PropertyGroup propertyGroup = new PropertyGroup(new Condition(propertyGroupElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : propertyGroupElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Label")){
                propertyGroup.setLabel(attr.getValue());
            }
            else{
                String message = String.format("(PropertyGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), propertyGroupElement.getLine());
                throw new ProjectIOException(message);
            }
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
        Import _import = new Import(project, new Condition(importElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : importElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Project")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Label")){
                _import.setLabel(attr.getValue());
            }
            else{
                String message = String.format("(Import) Unsupported attribute '%s' in line '%d'", attr.getName(), importElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return _import;
    }

    private ImportGroup readImportGroup(LocatedElement importGroupElement) throws ProjectIOException {
        ImportGroup importGroup = new ImportGroup(new Condition(importGroupElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : importGroupElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Label")){
                importGroup.setLabel(attr.getValue());
            }
            else{
                String message = String.format("(ImportGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), importGroupElement.getLine());
                throw new ProjectIOException(message);
            }
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
        ItemMetadata itemMetadata = new ItemMetadata(name, value, new Condition(itemMetadataElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : itemMetadataElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else{
                String message = String.format("(ItemMetaData) Unsupported attribute '%s' in line '%d'", attr.getName(), itemMetadataElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return itemMetadata;
    }

    private Property readProperty(LocatedElement propertyElement) throws ProjectIOException {
        String name = propertyElement.getName();
        String value = propertyElement.getText();
        Property property = new Property(name, value, new Condition(propertyElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : propertyElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else{
                String message = String.format("(Property) Unsupported attribute '%s' in line '%d'", attr.getName(), propertyElement.getLine());
                throw new ProjectIOException(message);
            }
        }
     
        return property;
    }

    private Choose readChoose(LocatedElement chooseElement) throws ProjectIOException {
        Choose choose = new Choose();
        
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
    
    private Otherwise readOtherwise(LocatedElement otherwiseElement) throws ProjectIOException {
        Otherwise otherwise = new Otherwise();
        
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
        When when = new When(new Condition(whenElement.getAttributeValue("Condition", "")));
        
        // read and validate attributes
        for(Attribute attr : whenElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else{
                String message = String.format("(When) Unsupported attribute '%s' in line '%d'", attr.getName(), whenElement.getLine());
                throw new ProjectIOException(message);
            }
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

    private Target readTarget(LocatedElement targetElement) throws ProjectIOException {
        String name = targetElement.getAttributeValue("Name", "");
        if(name.isEmpty()){
            String message = String.format("(Target) Missing attribute 'Name' in line '%d'", targetElement.getLine());
            throw new ProjectIOException(message);
        }
        
        Target target = new Target(name, new Condition(targetElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : targetElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Name")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Inputs")){
                target.setInputs(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Outputs")){
                target.setOutputs(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Returns")){
                target.setReturns(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("KeepDuplicateOutputs")){
                target.setKeepDuplicateOutputs(Boolean.parseBoolean(attr.getValue()));
            }
            else if(attr.getName().equalsIgnoreCase("BeforeTargets")){
                target.setBeforeTargets(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("AfterTargets")){
                target.setAfterTargets(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("DependsOnTargets")){
                target.setDependsOnTargets(attr.getValue());
            }
            else{
                String message = String.format("(Target) Unsupported attribute '%s' in line '%d'", attr.getName(), targetElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read elements
        for(Element _element : targetElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            if(element.getName().equalsIgnoreCase("Task")){
                Task task = readTask(element);
                target.add(task);
            }
            else if(element.getName().equalsIgnoreCase("PropertyGroup")){
                PropertyGroup propertyGroup = readPropertyGroup(element);
                target.add(propertyGroup);
            }
            else if(element.getName().equalsIgnoreCase("ItemGroup")){
                ItemGroup itemGroup = readItemGroup(element);
                target.add(itemGroup);
            }
            else if(element.getName().equalsIgnoreCase("OnError")){
                OnError onError = readOnError(element);
                target.add(onError);
            }
            else{
                String message = String.format("(Target) Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return target;
    }

    private Task readTask(LocatedElement taskElement) {
        /*
        For more information:
        http://msdn.microsoft.com/en-us/library/7z253716.aspx
        
        We need to implement each task and derive from the current Task element.
        */
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private OnError readOnError(LocatedElement onErrorElement) throws ProjectIOException {
        String executeTargets = onErrorElement.getAttributeValue("ExecuteTargets", "");
        if(executeTargets.isEmpty()){
            String message = String.format("(OnError) Missing attribute 'ExecuteTargets' in line '%d'", onErrorElement.getLine());
            throw new ProjectIOException(message);
        }
        
        OnError onError = new OnError(executeTargets, new Condition(onErrorElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : onErrorElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("ExecuteTargets")){
                // do nothing
            }
            else{
                String message = String.format("(OnError) Unsupported attribute '%s' in line '%d'", attr.getName(), onErrorElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read elements
        for(Element _element : onErrorElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            String message = String.format("(OnError) Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
            throw new ProjectIOException(message);
        }
        
        return onError;
    }
    
    private Output readOutput(LocatedElement outputElement) throws ProjectIOException{
        String taskParameter = outputElement.getAttributeValue("TaskParameter", "");
        if(taskParameter.isEmpty()){
            String message = String.format("(Output) Missing attribute 'TaskParameter' in line '%d'", outputElement.getLine());
            throw new ProjectIOException(message);
        }
        outputElement.removeAttribute("TaskParameter");
        
        Output output = new Output(taskParameter);
        
        // read elements
        for(Element _element : outputElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            String message = String.format("(Output) Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
            throw new ProjectIOException(message);
        }
        
        return output;
    }
    
    private ParameterGroup readParameterGroup(LocatedElement parameterGroupElement) throws ProjectIOException{
        ParameterGroup parameterGroup = new ParameterGroup();
        
        //Read and validate the attributes
        for(Attribute attr : parameterGroupElement.getAttributes()){
            String message = String.format("(ParameterGroup) Unsupported attribute '%s' in line '%d'", attr.getName(), parameterGroupElement.getLine());
            throw new ProjectIOException(message);
        }
        
        // read elements
        for(Element _element : parameterGroupElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            Parameter parameter = readParameter(element);
            parameterGroup.add(parameter);
        }
        
        return parameterGroup;
    }
    
    private Parameter readParameter(LocatedElement parameterElement) throws ProjectIOException{
        String name = parameterElement.getName();
        Parameter parameter = new Parameter(name);
        
        //Read and validate the attributes
        for(Attribute attr : parameterElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("ParameterType")){
                parameter.setParameterType(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("Output")){
                parameter.setOutput(Boolean.parseBoolean(attr.getValue()));
            }
            else if(attr.getName().equalsIgnoreCase("Required")){
                parameter.setRequired(Boolean.parseBoolean(attr.getValue()));
            }
            else{
                String message = String.format("(Parameter) Unsupported attribute '%s' in line '%d'", attr.getName(), parameterElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read elements
        for(Element _element : parameterElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            String message = String.format("(Parameter) Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
            throw new ProjectIOException(message);
        }
        
        return parameter;
    }

    private ProjectExtensions readProjectExtensions(LocatedElement projectExtensionsElement) {
        ProjectExtensions projectExtensions = new ProjectExtensions();
        return projectExtensions;
    }

    private UsingTask readUsingTask(LocatedElement usingTaskElement) throws ProjectIOException {
        String taskName = usingTaskElement.getAttributeValue("TaskName", "");
        if(taskName.isEmpty()){
            String message = String.format("(UsingTask) Missing attribute 'TaskName' in line '%d'", usingTaskElement.getLine());
            throw new ProjectIOException(message);
        }
        
        UsingTask usingTask = new UsingTask(taskName, new Condition(usingTaskElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : usingTaskElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("AssemblyName")){
                usingTask.setAssemblyName(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("AssemblyFile")){
                usingTask.setAssemblyFile(attr.getValue());
            }
            else if(attr.getName().equalsIgnoreCase("TaskFactory")){
                usingTask.setTaskFactory(attr.getValue());
            }
            else{
                String message = String.format("(UsingTask) Unsupported attribute '%s' in line '%d'", attr.getName(), usingTaskElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read elements
        for(Element _element : usingTaskElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            if(element.getName().equalsIgnoreCase("ParameterGroup")){
                ParameterGroup parameterGroup = readParameterGroup(element);
                usingTask.add(parameterGroup);
            }
            else if(element.getName().equalsIgnoreCase("TaskBody")){
                TaskBody taskBody = readTaskBody(element);
                usingTask.add(taskBody);
            }
            else{
                String message = String.format("(UsingTask) Unsupported element '%s' in line '%d'", element.getName(), element.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        return usingTask;
    }

    private TaskBody readTaskBody(LocatedElement taskBodyElement) throws ProjectIOException {
        TaskBody taskBody = new TaskBody(new Condition(taskBodyElement.getAttributeValue("Condition", "")));
        
        //Read and validate the attributes
        for(Attribute attr : taskBodyElement.getAttributes()){
            if(attr.getName().equalsIgnoreCase("Condition")){
                // do nothing
            }
            else if(attr.getName().equalsIgnoreCase("Evaluate")){
                taskBody.setEvaluate(Boolean.parseBoolean(attr.getValue()));
            }
            else{
                String message = String.format("(TaskBody) Unsupported attribute '%s' in line '%d'", attr.getName(), taskBodyElement.getLine());
                throw new ProjectIOException(message);
            }
        }
        
        // read elements
        for(Element _element : taskBodyElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            // not finished
        }
        
        return taskBody;
    }

    private ItemDefinition readItemDefinition(LocatedElement itemDefinitionElement) throws ProjectIOException {
        ItemDefinition itemDefinition = new ItemDefinition(itemDefinitionElement.getName());
        
        //Read and validate the attributes
        for(Attribute attr : itemDefinitionElement.getAttributes()){
            String message = String.format("(ItemDefinition) Unsupported attribute '%s' in line '%d'", attr.getName(), itemDefinitionElement.getLine());
            throw new ProjectIOException(message);
        }
        
        // read elements
        for(Element _element : itemDefinitionElement.getChildren()){
            LocatedElement element = (LocatedElement)_element;
            
            ItemMetadata itemMetadata = readItemMetadata(element);
            itemDefinition.add(itemMetadata);
        }
        
        return itemDefinition;
    }
}
