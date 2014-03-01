package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ImportGroupElement extends AbstractConditionalElement{
    private List<Element> elements = new ArrayList<Element>();
    
    private String label = "";
    
    public ImportGroupElement(){
        super("ImportGroup");
    }
    
    public void setLabel(String label){
        this.label = label;
    }
    
    public String getLabel(){
        return label;
    }
    
    public void add(ImportElement importElement){
        elements.add(importElement);
    }
}
