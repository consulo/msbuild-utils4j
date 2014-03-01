package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.List;
import org.bromix.msbuild.elements.ChooseElement;
import org.bromix.msbuild.elements.Element;
import org.bromix.msbuild.elements.ImportElement;
import org.bromix.msbuild.elements.ImportGroupElement;
import org.bromix.msbuild.elements.ItemGroupElement;
import org.bromix.msbuild.elements.ProjectExtensionsElement;
import org.bromix.msbuild.elements.PropertyGroupElement;
import org.bromix.msbuild.elements.TargetElement;
import org.bromix.msbuild.elements.UsingTaskElement;

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
    
    public void add(ChooseElement chooseElement){
        elements.add(chooseElement);
    }
    
    public void add(ImportElement importElement){
        elements.add(importElement);
    }
    
    public void add(ImportGroupElement importGroupElement){
        elements.add(importGroupElement);
    }
    
    public void add(ItemGroupElement itemGroup){
        elements.add(itemGroup);
    }
    
    public void add(ProjectExtensionsElement projectExtensionsElement){
        elements.add(projectExtensionsElement);
    }
    
    public void add(PropertyGroupElement propertyGroupElement){
        elements.add(propertyGroupElement);
    }
    
    public void add(TargetElement targetElement){
        elements.add(targetElement);
    }
    
    public void add(UsingTaskElement usingTaskElement){
        elements.add(usingTaskElement);
    }
}
