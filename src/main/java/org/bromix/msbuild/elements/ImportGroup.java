package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of ImportGroup Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ff606262.aspx">ImportGroup Element ([MSBuild)</a>
 * @see Import
 * @author Matthias Bromisch
 */
@ElementDefinition(
        children = {Import.class}
)
public class ImportGroup extends AbstractParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public ImportGroup(){
        super("ImportGroup", Type.ImportGroup);
    }
    
    public ImportGroup(Condition condition){
        super("ImportGroup", Type.ImportGroup);
        this.condition = condition;
    }
    
    public Import addImport(String project){
        return addImport(project, new Condition());
    }
    
    public Import addImport(String project, Condition condition){
        Import _import = new Import(project, condition);
        children.add(_import);
        return _import;
    }
    
    public List<Import> getImports(){
        List<Import> imports = new ArrayList<>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Import){
                imports.add((Import)element);
            }
        }
        
        return Collections.unmodifiableList(imports);
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
