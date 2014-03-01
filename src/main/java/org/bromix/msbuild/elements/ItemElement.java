package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ItemElement extends AbstractElement{
    List<Element> elements = new ArrayList<Element>();
    
    public ItemElement(String name){
        super(name);
    }
    
    public void add(ItemMetadataElement itemMetadataElement){
        elements.add(itemMetadataElement);
    }
}
