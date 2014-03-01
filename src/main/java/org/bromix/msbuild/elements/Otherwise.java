package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class Otherwise extends AbstractElement{
    public Otherwise(){
        super("Otherwise");
    }
    
    public void add(Choose choose){
        elements.add(choose);
    }
    
    public void add(ItemGroup itemGroup){
        elements.add(itemGroup);
    }
    
    public void add(PropertyGroup propertyGroup){
        elements.add(propertyGroup);
    }
}
