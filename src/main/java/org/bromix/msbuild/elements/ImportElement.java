package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class ImportElement extends AbstractConditionalElement{
    private String project = ""; // required
    
    public ImportElement(String project){
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
