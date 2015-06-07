package org.bromix.msbuild;

import org.bromix.msbuild.reflection.ElementValue;
import org.bromix.msbuild.reflection.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Class to query the current properties under a given {@link ProjectContext}.
 * @see ProjectContext
 * @author Matthias Bromisch
 */
public class ProjectQuery {    
    private final Project project;
    
    /**
     * Default constructor.
     * Creates a query for the given project.
     * @param project 
     */
    public ProjectQuery(Project project){
        this.project = project;
    }
    
    /**
     * Executes the query under the given {@link ProjectContext}.
     * @param context current context.
     * @return resulting context.
     * @throws ConditionException 
     * @throws ProjectIOException 
     */
    public ProjectContext query(ProjectContext context) throws ConditionException, ProjectIOException{
        // create a copy of the current context.
        ProjectContext result = new ProjectContext(context);
        
        /*
        we use a ProjectContext to collect all ItemDefinitions and assign them
        on the way to each found item with the corresponding name.
        */
        ProjectContext itemDefinitions = new ProjectContext();
        
        // execute the interal collect method
        collectFromElement(project, result, itemDefinitions);
        return result;
    }
    
    private void collectFromElement(Element msBuildElement, ProjectContext result, ProjectContext itemDefinitions) throws ProjectIOException, ConditionException {
        boolean isValid = true;
        
        // If an element implements Conditionable then evaluate the condition.
        if(msBuildElement instanceof Conditionable){
            isValid = ((Conditionable)msBuildElement).getCondition().evaluate(result.getProperties());
        }

        if(isValid){
            // <Project>
            if(msBuildElement.getClass()==Project.class){
                collectFields(msBuildElement, result.getProperties());
            }
            // <Property>
            else if(msBuildElement.getClass()==Property.class){
                Property property = (Property)msBuildElement;
                result.getProperties().put(property.getName(), property.getValue());
            }
            else if(msBuildElement.getClass()==Item.class){
                Item item = (Item)msBuildElement;

                // First we create a map of properties and assign all ItemDefinitions.
                ProjectContext.PropertyMap properties = new ProjectContext.PropertyMap();
                if(!itemDefinitions.getItems().get(item.getName()).isEmpty()){
                    properties.putAll(itemDefinitions.getItems().get(item.getName()).get(0));
                }

                // collect all fields
                collectFields(msBuildElement, properties);

                // collect ItemMetaData
                collectItemMetadata(result, item, properties);

                result.getItems().get(item.getName()).add(properties);
            }
            // collect ItemMetaData
            else if(msBuildElement.getClass()==ItemDefinition.class){
                ItemDefinition itemDefinition = (ItemDefinition)msBuildElement;

                ProjectContext.PropertyMap properties = new ProjectContext.PropertyMap();
                if(itemDefinitions.getItems().get(itemDefinition.getName()).isEmpty()){
                    itemDefinitions.getItems().get(itemDefinition.getName()).add(properties);
                }else{
                    properties = itemDefinitions.getItems().get(itemDefinition.getName()).get(0);
                }
                collectItemMetadata(result, itemDefinition, properties);
            }
            else if(msBuildElement.getClass()==Import.class){
                Import _import = (Import)msBuildElement;
                result.getImports().add(_import.getProject());
            }

            // collect children
            if(msBuildElement instanceof ParentElement){
                collectFromParentElement((ParentElement)msBuildElement, result, itemDefinitions);
            }
        }
    }
    
    private void collectFromParentElement(ParentElement parentElement, ProjectContext result, ProjectContext itemDefinitions) throws ConditionException, ProjectIOException{
        for(Element childElement : parentElement.getChildren()){
            collectFromElement(childElement, result, itemDefinitions);
        }
    }
    
    private void collectFields(Element msBuildElement, ProjectContext.PropertyMap properties) throws ProjectIOException{
        List<Field> fields = ReflectionHelper.getDeclaredFieldsWithAnnotation(msBuildElement.getClass(), true, ElementValue.class);
        for(Field field : fields){
            ElementValue elementValue = (ElementValue)field.getAnnotation(ElementValue.class);
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Object fieldValueObject = field.get(msBuildElement);
                
                if(elementValue.valueType()==ElementValue.ValueType.ELEMENT_ATTRIBUTE){
                    // build the name of the attribute (based on the field name)
                    String attributeName = field.getName();
                    // normalize the filename to camelcase
                    attributeName = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
                    // use the binding instead
                    if(elementValue.bind()!=null && !elementValue.bind().isEmpty()){
                        attributeName = elementValue.bind();
                    }
                    
                    String value = null;
                    if(field.getType().isAssignableFrom(String.class)){
                        value = (String)fieldValueObject;
                    }
                    else if(field.getType().isAssignableFrom(Boolean.class)){
                        value = ((Boolean)fieldValueObject).toString();
                    }
                    else if(field.getType().isAssignableFrom(boolean.class)){
                        value = ((Boolean)fieldValueObject).toString();
                    }
                    
                    if(elementValue.required() && (value==null || value.isEmpty())){
                        throw new ProjectIOException(String.format("Value '%s' for element '%s' is required", attributeName, msBuildElement.getElementName()));
                    }
                    
                    if(value!=null && !value.isEmpty()){
                        properties.put(attributeName, value);
                    }
                }
                
                field.setAccessible(isAccessible);
                
            } catch ( IllegalArgumentException  ex) {
                throw new ProjectIOException(ex);
            } catch (IllegalAccessException ex) {
                throw new ProjectIOException(ex);
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
