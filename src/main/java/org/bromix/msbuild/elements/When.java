package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class When extends AbstractElement{
    public When(){
        super("When");
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
