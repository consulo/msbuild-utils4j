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
 *
 * @author Matthias Bromisch
 */
public class Project {
    private final List<Element> elements = new ArrayList<Element>();
    
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
    
    public void add(ProjectExtensionsElement projectExtensions){
        elements.add(projectExtensions);
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
