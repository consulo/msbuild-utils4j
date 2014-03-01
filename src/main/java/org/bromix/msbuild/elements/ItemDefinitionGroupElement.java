package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement a ItemDefinitionGroup-Element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/bb629392.aspx
 * 
 * @author Matthias Bromisch
 */
public class ItemDefinitionGroupElement extends AbstractConditionalElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public ItemDefinitionGroupElement(){
        super("ItemDefinitionGroup");
    }
    
    public void add(ItemElement itemElement){
        elements.add(itemElement);
    }
}
