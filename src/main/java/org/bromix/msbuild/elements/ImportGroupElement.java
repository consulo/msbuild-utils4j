package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ImportGroupElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public ImportGroupElement(){
        super("ImportGroup");
    }
    
    public void add(ImportElement importElement){
        elements.add(importElement);
    }
}
