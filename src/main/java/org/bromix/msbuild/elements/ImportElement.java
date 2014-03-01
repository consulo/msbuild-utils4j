package org.bromix.msbuild.elements;

/**
 *
 * @author Matthias Bromisch
 */
public class ImportElement extends AbstractElement{
    private String project = ""; // required
    
    public ImportElement(String project){
        super("Import");
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
