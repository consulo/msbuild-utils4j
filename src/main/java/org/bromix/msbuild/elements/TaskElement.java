package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class TaskElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public TaskElement(String name){
        super(name);
    }
    
    public void add(OutputElement outputElement){
        elements.add(outputElement);
    }
}
