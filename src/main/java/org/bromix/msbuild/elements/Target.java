package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class Target extends AbstractElement{
    public Target(){
        super("Target");
    }
    
    public void add(Task task){
        elements.add(task);
    }
    
    public void add(PropertyGroup propertyGroup){
        elements.add(propertyGroup);
    }
    
    public void add(ItemGroup itemGroup){
        elements.add(itemGroup);
    }
    
    public void add(OnError onError){
        elements.add(onError);
    }
}
