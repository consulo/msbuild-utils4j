package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;

/**
 * Implementation of an Item-Element.
 * 
 * For more information visit:
 * http://msdn.microsoft.com/en-us/library/ms164283.aspx
 * 
 * @author Matthias Bromisch
 */
public class Item extends AbstractParentElement implements Conditionable{
    private final Condition condition;
    
    private String include = ""; // required
    private String exclude = ""; // optional
    private String remove = ""; // optional
    private String keepMetadata = ""; // optional
    private String removeMetadata = ""; // optional
    private String keepDuplicates = ""; // optional
    
    
    public Item(String name, String include){
        super(name, Type.Item);
        this.include = include;
        this.condition = new Condition();
    }
    
    /**
     * Default constructor for Items belonging to a ItemGroup.
     * @param name
     * @param include 
     * @param condition 
     */
    public Item(String name, String include, Condition condition){
        super(name, Type.Item);
        this.include = include;
        this.condition = condition;
    }
    
    public void add(ItemMetadata metadata){
        children.add(metadata);
    }
    
    public List<ItemMetadata> getMetadataList(){
        List<ItemMetadata> metadataList = new ArrayList<ItemMetadata>();
        
        for(Element element : children){
            if(element.getElementType()==Type.ItemMetadata){
                metadataList.add((ItemMetadata)element);
            }
        }
        
        return Collections.unmodifiableList(metadataList);
    }
    
    /**
     * The file or wildcard to include in the list of items (required).
     * @param include 
     */
    public void setInclude(String include){
        this.include = include;
    }
    
    /**
     * Returns the include of the item.
     * The file or wildcard to include in the list of items.
     * @return 
     */
    public String getInclude(){
        return include;
    }
    
    /**
     * The file or wildcard to exclude from the list of items (optional).
     * @param exclude 
     */
    public void setExclude(String exclude){
        this.exclude = exclude;
    }
    
    /**
     * Returns the excludes of the item.
     * @return 
     */
    public String getExclude(){
        return exclude;
    }
    
    /**
     * The file or wildcard to remove from the list of items (optional).
     * @param remove 
     */
    public void setRemove(String remove){
        this.remove = remove;
    }
    
    /**
     * The file or wildcard to remove from the list of items.
     * @return 
     */
    public String getRemove(){
        return remove;
    }
    
    /**
     * The metadata for the source items to add to the target items (optional).
     * @param keepMetadata 
     */
    public void setKeepMetadata(String keepMetadata){
        this.keepMetadata = keepMetadata;
    }
    
    /**
     * The metadata for the source items to add to the target items (optional).
     * @return 
     */
    public String getKeepMetadata(){
        return keepMetadata;
    }
    
    /**
     * The metadata for the source items to not transfer to the target items (optional).
     * @param removeMetadata 
     */
    public void setRemoveMetadata(String removeMetadata){
        this.removeMetadata = removeMetadata;
    }
    
    /**
     * The metadata for the source items to not transfer to the target items (optional).
     * @return 
     */
    public String getRemoveMetadata(){
        return removeMetadata;
    }
    
    /**
     * Specifies whether an item should be added to the target group if it's an exact duplicate of an existing item (optional).
     * @param keepDuplicates 
     */
    public void setKeepDuplicates(String keepDuplicates){
        this.keepDuplicates = keepDuplicates;
    }
    
    /**
     * Specifies whether an item should be added to the target group if it's an exact duplicate of an existing item (optional).
     * @return 
     */
    public String getKeepDuplicates(){
        return keepDuplicates;
    }

    public Condition getCondition() {
        return condition;
    }
}
