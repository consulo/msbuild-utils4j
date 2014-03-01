package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
 * Implementation of a MSBuild-Project.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/bcxfsh87.aspx
 * 
 * @author Matthias Bromisch
 */
public class Project {
    private List<Element> elements = new ArrayList<Element>();
    
    private String defaultTargets = ""; // optional
    private String initialTargets = ""; // optional
    private String toolsVersion = ""; // optional
    private String treatAsLocalProperty = ""; // optional
    
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
    
    public List<Element> getChildren(){
        return Collections.unmodifiableList(elements);
    }
    
    public void add(Choose choose){
        elements.add(choose);
    }
    
    public void add(Import importElement){
        elements.add(importElement);
    }
    
    public void add(ImportGroup importGroupElement){
        elements.add(importGroupElement);
    }
    
    public void add(ItemGroup itemGroup){
        elements.add(itemGroup);
    }
    
    public void add(ItemDefinitionGroup element){
        elements.add(element);
    }
    
    public void add(ProjectExtensions projectExtensionsElement){
        elements.add(projectExtensionsElement);
    }
    
    public void add(PropertyGroup propertyGroupElement){
        elements.add(propertyGroupElement);
    }
    
    public void add(Target targetElement){
        elements.add(targetElement);
    }
    
    public void add(UsingTask usingTaskElement){
        elements.add(usingTaskElement);
    }
}
