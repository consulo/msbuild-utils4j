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

/**
 * Implementation of a MSBuild element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/bcxfsh87.aspx
 * 
 * @author Matthias Bromisch
 */
public class Project extends AbstractParentElement{
    private String defaultTargets = ""; // optional
    private String initialTargets = ""; // optional
    private String toolsVersion = ""; // optional
    private String treatAsLocalProperty = ""; // optional
    
    public Project(){
        super("Project", Element.Type.Project);
    }
    
    /**
     * Sets the default target or targets.
     * Multiple targets are semi-colon (;) delimited.
     * @param defaultTargets 
     */
    public void setDefaultTargets(String defaultTargets){
        this.defaultTargets = defaultTargets;
    }
    
    /**
     * Returns the default target or target.
     * Multiple targets are semi-colon (;) delimited.
     * @return 
     */
    public String getDefaultTargets(){
        return defaultTargets;
    }
    
    /**
     * Sets the initial target or targets.
     * Multiple targets are semi-colon (;) delimited.
     * @param initialTargets 
     */
    public void setInitialTargets(String initialTargets){
        this.initialTargets = initialTargets;
    }
    
    /**
     * returns the initial target or targets.
     * Multiple targets are semi-colon (;) delimited.
     * @return 
     */
    public String getInitialTargets(){
        return initialTargets;
    }
    
    /**
     * Sets the tools version.
     * @remark from the online reference: (The version of the toolset MSBuild uses to determine the values for $(MSBuildBinPath) and $(MSBuildToolsPath).)
     * @param toolsVersion 
     */
    public void setToolsVersion(String toolsVersion){
        this.toolsVersion = toolsVersion;
    }
    
    /**
     * Returns the tools version.
     * @return 
     */
    public String getToolsVersion(){
        return toolsVersion;
    }
    
    /**
     * Sets the "TreatAsLocalProperty".
     * For more information visit:
     * http://msdn.microsoft.com/en-us/library/bcxfsh87.aspx
     * @param treatAsLocalProperty 
     */
    public void setTreatAsLocalProperty(String treatAsLocalProperty){
        this.treatAsLocalProperty = treatAsLocalProperty;
    }
    
    /**
     * returns the "TreatAsLocalProperty".
     * For more information visit:
     * http://msdn.microsoft.com/en-us/library/bcxfsh87.aspx
     * @return 
     */
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
