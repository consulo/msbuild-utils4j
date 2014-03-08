package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implemenation of Import Element.
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/92x05xfs.aspx">Import Element (MSBuild)</a>
 * @author Matthias Bromisch
 */
@ElementDefinition
public class Import extends Element implements Conditionable{
    @ElementValue( required = true)
    private String project = ""; // required
    @ElementValue
    private Condition condition = new Condition();
    
    public Import(){
        super("Import", Type.Import);
    }
    
    public Import(String project){
        super("Import", Type.Import);
        this.project = project;
    }
    
    public Import(String project, Condition condition){
        super("Import", Type.Import);
        this.project = project;
        this.condition = condition;
    }
    
    public String getProject(){
        return project;
    }

    public Condition getCondition() {
        return condition;
    }
}
