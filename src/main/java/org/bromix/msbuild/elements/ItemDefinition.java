package org.bromix.msbuild.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bromix.msbuild.Condition;
import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ElementDefinition;

/**
 * Implementation of ItemDefinition Element.
 * 
 * This kind of element isn't documented. The MSBuild Reference links to an Item
 * Element for ItemDefinitionGroups. But Item Elements require at least a
 * 'Include' attribute.
 * 
 * @author Matthias Bromisch
 */
@ElementDefinition(
        nameMatching = ElementDefinition.NameMatching.VARIABLE,
        children = {ItemMetadata.class}
)
public class ItemDefinition extends AbstractParentElement implements Conditionable{
    @ElementValue
    private Condition condition = new Condition();
    
    public ItemDefinition(){
        super("", Type.ItemDefinition);
    }
    
    public ItemDefinition(String name){
        super(name, Type.ItemDefinition);
    }
    
    public ItemDefinition(String name, Condition condition){
        super(name, Type.ItemDefinition);
        this.condition = condition;
    }
    
    public void add(ItemMetadata metadata){
        children.add(metadata);
    }
    
    public List<ItemMetadata> getMetadataList(){
        List<ItemMetadata> metadataList = new ArrayList<>();
        
        for(Element element : children){
            if(element.getElementType()==Type.ItemMetadata){
                metadataList.add((ItemMetadata)element);
            }
        }
        
        return Collections.unmodifiableList(metadataList);
    }

    @Override
    public Condition getCondition() {
        return condition;
    }
}
