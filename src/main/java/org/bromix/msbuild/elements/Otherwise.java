package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class Otherwise extends AbstractParentElement{
    public Otherwise(){
        super("Otherwise", Type.Otherwise);
    }
    
    public void add(Choose choose){
        children.add(choose);
    }
    
    public void add(ItemGroup itemGroup){
        children.add(itemGroup);
    }
    
    public void add(PropertyGroup propertyGroup){
        children.add(propertyGroup);
    }
}
