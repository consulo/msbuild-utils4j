package org.bromix.msbuild;

import java.util.Map;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectQuery {    
    private final Project project;
    
    public ProjectQuery(Project project){
        this.project = project;
    }
    
    public ProjectContext query(ProjectContext context) throws ConditionException{
        ProjectContext result = new ProjectContext(context);
        ProjectContext itemDefinitions = new ProjectContext();
        collect(project, result, itemDefinitions);
        return result;
    }
    
    private void collect(ParentElement parentElement, ProjectContext result, ProjectContext itemDefinitions) throws ConditionException{
        for(Element childElement : parentElement.getChildren()){
            boolean isValid = true;
            if(childElement instanceof Conditionable){
                isValid = ((Conditionable)childElement).getCondition().evaluate(result.getProperties());
            }
            
            if(isValid){
                if(childElement.getClass()==Property.class){
                    Property property = (Property)childElement;
                    result.getProperties().put(property.getName(), property.getValue());
                }
                else if(childElement.getClass()==Item.class){
                    Item item = (Item)childElement;
                    
                    ProjectContext.PropertyMap properties = new ProjectContext.PropertyMap();
                    if(!itemDefinitions.getItems().get(item.getName()).isEmpty()){
                        properties.putAll(itemDefinitions.getItems().get(item.getName()).get(0));
                    }
                    
                    result.getItems().get(item.getName()).add(properties);
                    properties.put("Include", item.getInclude());
                    collectItemMetadata(result, item, properties);
                }
                else if(childElement.getClass()==ItemDefinition.class){
                    ItemDefinition itemDefinition = (ItemDefinition)childElement;
                    
                    ProjectContext.PropertyMap properties = new ProjectContext.PropertyMap();
                    if(itemDefinitions.getItems().get(itemDefinition.getName()).isEmpty()){
                        itemDefinitions.getItems().get(itemDefinition.getName()).add(properties);
                    }else{
                        properties = itemDefinitions.getItems().get(itemDefinition.getName()).get(0);
                    }
                    collectItemMetadata(result, itemDefinition, properties);
                }
                
                if(childElement instanceof ParentElement){
                    collect((ParentElement)childElement, result, itemDefinitions);
                }
            }
        }
    }
    
    private void collectItemMetadata(ProjectContext result, Item item, Map<String, String> values) throws ConditionException{
        for(ItemMetadata itemMetadata : item.getMetadataList()){
            if(itemMetadata.getCondition().evaluate(result.getProperties())){
                values.put(itemMetadata.getName(), itemMetadata.getValue());
            }
        }
    }
    
    private void collectItemMetadata(ProjectContext result, ItemDefinition itemDefinition, Map<String, String> values) throws ConditionException{
        for(ItemMetadata itemMetadata : itemDefinition.getMetadataList()){
            if(itemMetadata.getCondition().evaluate(result.getProperties())){
                values.put(itemMetadata.getName(), itemMetadata.getValue());
            }
        }
    }
}
