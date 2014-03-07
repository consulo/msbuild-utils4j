package org.bromix.msbuild.elements;

import org.bromix.msbuild.reflection.ElementDefinition;

/**
 *
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {
            Choose.class,
            ItemGroup.class,
            PropertyGroup.class
        }
)
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
