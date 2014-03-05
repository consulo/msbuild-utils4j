package org.bromix.msbuild.elements;

import org.bromix.msbuild.Condition;

/**
 * Implemenation of Import element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/92x05xfs.aspx
 * 
 * @author Matthias Bromisch
 */
public class Import extends Element implements Conditionable{
    private String project = ""; // required
    private final Condition condition;
    
    public Import(String project){
        super("Import", Type.Import);
        this.project = project;
        this.condition = new Condition();
    }
    
    public Import(String project, Condition condition){
        super("Import", Type.Import);
        this.project = project;
        this.condition = condition;
    }
    
    /**
     * Sets the project.
     * @param project 
     */
    public void setProject(String project){
        this.project = project;
    }
    
    /**
     * Returns the project.
     * @return 
     */
    public String getProject(){
        return project;
    }

    public Condition getCondition() {
        return condition;
    }
}
