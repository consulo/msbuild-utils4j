package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectQuery {
    public static class Context{
        private final Map<String, String> properties = new HashMap<>();
        private final Map<String, List<Map<String, String>>> items = new HashMap<>();
        private final Map<String, List<Map<String, String>>> itemDefintions = new HashMap<>();
        
        public Context(){
            properties.put("Configuration", "");
            properties.put("Platform", "");
        }
        
        public Map<String, String> getProperties(){
            return properties;
        }

        
        public Map<String, List<Map<String, String>>> getItems(){
            return items;
        }
        
        public Map<String, List<Map<String, String>>> getItemDefinitions(){
            return itemDefintions;
        }
    };
    
    private final Project project;
    
    public ProjectQuery(Project project){
        this.project = project;
    }
    
    public void query(Context context) throws ConditionException{
        collect(project, context);
    }
    
    private void collect(ParentElement parentElement, Context context) throws ConditionException{
        for(Element childElement : parentElement.getChildren()){
            boolean isValid = true;
            if(childElement instanceof Conditionable){
                isValid = ((Conditionable)childElement).getCondition().evaluate(context.getProperties());
            }
            
            if(isValid){
                if(childElement.getClass()==Property.class){
                    Property property = (Property)childElement;
                    context.getProperties().put(property.getName(), property.getValue());
                }
                else if(childElement.getClass()==Item.class){
                    Item item = (Item)childElement;
                    if(!context.getItems().containsKey(item.getName())){
                        context.getItems().put(item.getName(), new ArrayList<Map<String, String>>());
                    }
                    
                    Map<String, String> values = new HashMap<>();
                    context.getItems().get(item.getName()).add(values);
                    values.put("Include", item.getInclude());
                    collectItemMetadata(context, item, values);
                }
                else if(childElement.getClass()==ItemDefinition.class){
                    ItemDefinition itemDefinition = (ItemDefinition)childElement;
                    if(!context.getItemDefinitions().containsKey(itemDefinition.getName())){
                        context.getItemDefinitions().put(itemDefinition.getName(), new ArrayList<Map<String, String>>());
                    }
                    
                    Map<String, String> values = new HashMap<>();
                    context.getItemDefinitions().get(itemDefinition.getName()).add(values);
                    collectItemMetadata(context, itemDefinition, values);
                }
                
                if(childElement instanceof ParentElement){
                    collect((ParentElement)childElement, context);
                }
            }
        }
    }
    
    private void collectItemMetadata(Context context, Item item, Map<String, String> values) throws ConditionException{
        for(ItemMetadata itemMetadata : item.getMetadataList()){
            if(itemMetadata.getCondition().evaluate(context.getProperties())){
                values.put(itemMetadata.getName(), itemMetadata.getValue());
            }
        }
    }
    
    private void collectItemMetadata(Context context, ItemDefinition itemDefinition, Map<String, String> values) throws ConditionException{
        for(ItemMetadata itemMetadata : itemDefinition.getMetadataList()){
            if(itemMetadata.getCondition().evaluate(context.getProperties())){
                values.put(itemMetadata.getName(), itemMetadata.getValue());
            }
        }
    }
}
