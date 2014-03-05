package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;

/**
 * Implementation of ImportGroup element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ff606262.aspx
 * 
 * @author Matthias Bromisch
 */
public class ImportGroup extends AbstractParentElement implements Conditionable{
    final private Condition condition;
    
    public ImportGroup(){
        super("ImportGroup", Type.ImportGroup);
        this.condition = new Condition();
    }
    
    public ImportGroup(Condition condition){
        super("ImportGroup", Type.ImportGroup);
        this.condition = condition;
    }
    
    public void add(Import _import){
        children.add(_import);
    }
    
    public List<Import> getImports(){
        List<Import> imports = new ArrayList<Import>();
        
        for(Element element : children){
            if(element.getElementType()==Type.Import){
                imports.add((Import)element);
            }
        }
        
        return Collections.unmodifiableList(imports);
    }

    public Condition getCondition() {
        return condition;
    }
}
