package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ImportGroup extends AbstractConditionalElement{
    private final List<Import> imports = new ArrayList<Import>();
    
    public ImportGroup(){
        super("ImportGroup");
    }
    
    public void addImport(Import _import){
        imports.add(_import);
    }
    
    public List<Import> getImports(){
        return Collections.unmodifiableList(imports);
    }
}
