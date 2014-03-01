package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class Task extends AbstractElement{
    private final List<Element> elements = new ArrayList<Element>();
    
    public Task(String name){
        super(name);
    }
    
    public void add(Output outputElement){
        elements.add(outputElement);
    }
}
