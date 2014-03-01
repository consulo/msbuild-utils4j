package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ItemGroupElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public ItemGroupElement(String name){
        super(name);
    }
    
    public void add(ItemElement itemElement){
        elements.add(itemElement);
    }
}
