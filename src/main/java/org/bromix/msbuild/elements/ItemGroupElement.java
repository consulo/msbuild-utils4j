package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a ItemGroup-Element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/646dk05y.aspx
 * 
 * @author Matthias Bromisch
 */
public class ItemGroupElement extends AbstractElement{
    private List<Element> elements = new ArrayList<Element>();
    
    public ItemGroupElement(){
        super("ItemGroup");
    }
    
    public void add(ItemElement itemElement){
        elements.add(itemElement);
    }
}
