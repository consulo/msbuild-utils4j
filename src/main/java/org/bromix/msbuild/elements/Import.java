package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class Import extends AbstractConditionalElement{
    private String project = ""; // required
    
    public Import(String project){
        super("Import");
        this.project = project;
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
}
