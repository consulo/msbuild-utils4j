package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class UsingTask extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public UsingTask(){
        super("UsingTask");
    }
    
    public void add(ParameterGroup parameterGroupElement){
        elements.add(parameterGroupElement);
    }
}
