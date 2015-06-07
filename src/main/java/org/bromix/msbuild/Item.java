package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;
import org.bromix.msbuild.reflection.ElementDefinition.NameMatching;

/**
 * Implementation of Item Element
 * 
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms164283.aspx">Item Element (MSBuild)</a>
 * @see ItemMetadata
 * @author Matthias Bromisch
 */
@ElementDefinition(
        nameMatching = NameMatching.VARIABLE,
        children = { ItemMetadata.class }
)
public class Item extends ParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    @ElementValue(required = true)
    private String include = ""; // required
    @ElementValue
    private String exclude = ""; // optional
    @ElementValue
    private String remove = ""; // optional
    @ElementValue
    private String keepMetadata = ""; // optional
    @ElementValue
    private String removeMetadata = ""; // optional
    @ElementValue
    private String keepDuplicates = ""; // optional
    
    
    public Item(){
        super("", Type.Item);
    }
    
    public Item(String name, String include){
        super(name, Type.Item);
        this.include = include;
    }
    
    public Item(String name, String include, Condition condition){
        super(name, Type.Item);
        this.include = include;
        this.condition = condition;
    }
    
    public ItemMetadata addMetadata(String name, String value){
        return addMetadata(name, value, new Condition());
    }
    
    public ItemMetadata addMetadata(String name, String value, Condition condition){
        ItemMetadata itemMetadata = new ItemMetadata(name, value, condition);
        children.add(itemMetadata);
        return itemMetadata;
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
    
    public String getName(){
        return elementName;
    }
    
    public String getInclude(){
        return include;
    }
    
    public void setExclude(String exclude){
        this.exclude = exclude;
    }
    
    public String getExclude(){
        return exclude;
    }
    
    public void setRemove(String remove){
        this.remove = remove;
    }
    
    public String getRemove(){
        return remove;
    }
    
    public void setKeepMetadata(String keepMetadata){
        this.keepMetadata = keepMetadata;
    }
    
    public String getKeepMetadata(){
        return keepMetadata;
    }
    
    public void setRemoveMetadata(String removeMetadata){
        this.removeMetadata = removeMetadata;
    }
    
    public String getRemoveMetadata(){
        return removeMetadata;
    }
    
    public void setKeepDuplicates(String keepDuplicates){
        this.keepDuplicates = keepDuplicates;
    }
    
    public String getKeepDuplicates(){
        return keepDuplicates;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
