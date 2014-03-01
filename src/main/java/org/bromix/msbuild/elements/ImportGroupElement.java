package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ImportGroupElement extends AbstractConditionalElement{
    private List<ImportElement> imports = new ArrayList<ImportElement>();
    
    public ImportGroupElement(){
        super("ImportGroup");
    }
    
    public void add(ImportElement _import){
        imports.add(_import);
    }
    
    public List<ImportElement> getImports(){
        return Collections.unmodifiableList(imports);
    }
}
