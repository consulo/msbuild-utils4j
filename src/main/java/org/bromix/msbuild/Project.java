package org.bromix.msbuild;

import org.bromix.msbuild.elements.AbstractParentElement;
import org.bromix.msbuild.elements.Choose;
import org.bromix.msbuild.elements.Element;
import org.bromix.msbuild.elements.Import;
import org.bromix.msbuild.elements.ImportGroup;
import org.bromix.msbuild.elements.ItemDefinitionGroup;
import org.bromix.msbuild.elements.ItemGroup;
import org.bromix.msbuild.elements.ProjectExtensions;
import org.bromix.msbuild.elements.PropertyGroup;
import org.bromix.msbuild.elements.Target;
import org.bromix.msbuild.elements.UsingTask;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of Project Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/bcxfsh87.aspx">Project Element (MSBuild)</a>
 * @see ItemGroup
 * @see PropertyGroup
 * @see Import
 * @see ImportGroup
 * @see ItemDefinitionGroup
 * @see Choose
 * @see ProjectExtensions
 * @see Target
 * @see UsingTask
 * @author Matthias Bromisch
 */

@ElementDefinition(
        children = {
            ItemGroup.class,
            PropertyGroup.class,
            Import.class,
            ImportGroup.class,
            ItemDefinitionGroup.class,
            Choose.class,
            ProjectExtensions.class,
            Target.class,
            UsingTask.class
        }
)
public class Project extends AbstractParentElement{
    @ElementValue
    private String defaultTargets = ""; // optional
    @ElementValue
    private String initialTargets = ""; // optional
    @ElementValue
    private String toolsVersion = ""; // optional
    @ElementValue
    private String treatAsLocalProperty = ""; // optional
    
    public Project(){
        super("Project", Element.Type.Project);
    }
    
    public void setDefaultTargets(String defaultTargets){
        this.defaultTargets = defaultTargets;
    }
    
    public String getDefaultTargets(){
        return defaultTargets;
    }
    
    public void setInitialTargets(String initialTargets){
        this.initialTargets = initialTargets;
    }
    
    public String getInitialTargets(){
        return initialTargets;
    }
    
    public void setToolsVersion(String toolsVersion){
        this.toolsVersion = toolsVersion;
    }
    
    public String getToolsVersion(){
        return toolsVersion;
    }
    
    public void setTreatAsLocalProperty(String treatAsLocalProperty){
        this.treatAsLocalProperty = treatAsLocalProperty;
    }
    
    public String getTreatAsLocalProperty(){
        return treatAsLocalProperty;
    }
    
    public ItemGroup addItemGroup(){
        return addItemGroup(new Condition());
    }
    
    public ItemGroup addItemGroup(Condition condition){
        ItemGroup itemGroup = new ItemGroup(condition);
        children.add(itemGroup);
        return itemGroup;
    }
    
    public Choose addChoose(){
        Choose choose = new Choose();
        children.add(choose);
        return choose;
    }
    
    public Import addImport(String project){
        return addImport(project, new Condition());
    }
    
    public Import addImport(String project, Condition condition){
        Import _import = new Import(project, condition);
        children.add(_import);
        return _import;
    }
    
    public ImportGroup addImportGroup(){
        return addImportGroup(new Condition());
    }
    
    public ImportGroup addImportGroup(Condition condition){
        ImportGroup importGroup = new ImportGroup(condition);
        children.add(importGroup);
        return importGroup;
    }
    
    public ItemDefinitionGroup addItemDefinitionGroup(){
        return addItemDefinitionGroup(new Condition());
    }
    
    public ItemDefinitionGroup addItemDefinitionGroup(Condition condition){
        ItemDefinitionGroup itemDefinitionGroup = new ItemDefinitionGroup(condition);
        children.add(itemDefinitionGroup);
        return itemDefinitionGroup;
    }
    
    public ProjectExtensions addProjectExtensions(){
        ProjectExtensions projectExtensions = new ProjectExtensions();
        children.add(projectExtensions);
        return projectExtensions;
    }
    
    public PropertyGroup addPropertyGroup(){
        return addPropertyGroup(new Condition());
    }
    
    public PropertyGroup addPropertyGroup(Condition condition){
        PropertyGroup propertyGroup = new PropertyGroup(condition);
        children.add(propertyGroup);
        return propertyGroup;
    }
    
    public Target addTarget(String name){
        return addTarget(name, new Condition());
    }
    
    public Target addTarget(String name, Condition condition){
        Target target = new Target(name, condition);
        children.add(target);
        return target;
    }
    
    public UsingTask addUsingTask(String taskName){
        return addUsingTask(taskName, new Condition());
    }
    
    public UsingTask addUsingTask(String taskName, Condition condition){
        UsingTask usingTask = new UsingTask(taskName, condition);
        children.add(usingTask);
        return usingTask;
    }
}
