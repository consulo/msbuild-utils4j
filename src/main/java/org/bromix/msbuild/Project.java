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
    
    public void add(Choose choose){
        children.add(choose);
    }
    
    public void add(Import _import){
        children.add(_import);
    }
    
    public void add(ImportGroup importGroup){
        children.add(importGroup);
    }
    
    public void add(ItemGroup itemGroup){
        children.add(itemGroup);
    }
    
    public void add(ItemDefinitionGroup itemDefinitionGroup){
        children.add(itemDefinitionGroup);
    }
    
    public void add(ProjectExtensions projectExtensions){
        children.add(projectExtensions);
    }
    
    public void add(PropertyGroup propertyGroup){
        children.add(propertyGroup);
    }
    
    public void add(Target target){
        children.add(target);
    }
    
    public void add(UsingTask usingTask){
        children.add(usingTask);
    }
}
