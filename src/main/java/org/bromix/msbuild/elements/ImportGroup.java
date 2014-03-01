package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ImportGroup extends AbstractConditionalElement{
    public ImportGroup(){
        super("ImportGroup");
    }
    
    public void add(Import _import){
        elements.add(_import);
    }
    
    public List<Import> getImports(){
        List<Import> imports = new ArrayList<Import>();
        
        for(Element element : elements){
            if(element instanceof Import){
                imports.add((Import)element);
            }
        }
        
        return Collections.unmodifiableList(imports);
    }
}
