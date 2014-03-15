package org.bromix.msbuild;

import org.bromix.msbuild.ParentElement;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of Otherwise Element
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164286.aspx">Otherwise Element (MSBuild)</a>
 * @see Choose
 * @see ItemGroup
 * @see PropertyGroup
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {
            Choose.class,
            ItemGroup.class,
            PropertyGroup.class
        }
)
public class Otherwise extends ParentElement{
    public Otherwise(){
        super("Otherwise", Type.Otherwise);
    }
    
    public Choose addChoose(){
        Choose choose = new Choose();
        children.add(choose);
        return choose;
    }
    
    public ImportGroup addImportGroup(){
        return addImportGroup(new Condition());
    }
    
    public ImportGroup addImportGroup(Condition condition){
        ImportGroup importGroup = new ImportGroup(condition);
        children.add(importGroup);
        return importGroup;
    }
    
    public PropertyGroup addPropertyGroup(){
        return addPropertyGroup(new Condition());
    }
    
    public PropertyGroup addPropertyGroup(Condition condition){
        PropertyGroup propertyGroup = new PropertyGroup(condition);
        children.add(propertyGroup);
        return propertyGroup;
    }
}
