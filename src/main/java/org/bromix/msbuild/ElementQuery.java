package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Matthias Bromisch
 */
public class ElementQuery {
    private final Element element;
    
    public ElementQuery(Element element){
        this.element = element;
    }
    
    public <T extends Element> T findFirst(Class<? extends Element> cls){
        return findFirst(cls, null);
    }
    
    public <T extends Element> T findFirst(Class<? extends Element> cls, String elementName){
        List<T> result = collect(cls, elementName);
        if(!result.isEmpty()){
            return result.get(0);
        }
        
        return null;
    }
    
    public <T extends Element> List<T> collect(Class<? extends Element> cls){
        return collect(null);
    }
    
    public <T extends Element> List<T> collect(Class<? extends Element> cls, String elementName){
        List<T> result = new ArrayList<>();
        
        if(element instanceof ParentElement){
            collect((ParentElement)element, result, cls, elementName);
        }
        
        return Collections.unmodifiableList(result);
    }
    
    private <T extends Element> void collect(ParentElement parentElement, List<T> result, Class<? extends Element> cls, String elementName){
        for(Element child : parentElement.getChildren()){
            if(child.getClass()==cls){
                if(elementName==null || child.getElementName().equals(elementName)){
                    result.add((T) child);
                }
            }
            
            if(child instanceof ParentElement){
                collect((ParentElement)child, result, cls, elementName);
            }
        }
    }
}
