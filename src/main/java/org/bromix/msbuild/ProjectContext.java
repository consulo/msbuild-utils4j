package org.bromix.msbuild;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectContext{
    private PropertyMap properties = new PropertyMap();
    private ItemMap items = new ItemMap();
    private List<String> imports = new ArrayList<String>();
    
    public ProjectContext(){
    }
    
    public ProjectContext(ProjectContext projectContext){
        properties = new PropertyMap(projectContext.getProperties());
        items = new ItemMap(projectContext.getItems());
        imports.addAll(projectContext.getImports());
    }
    
    public PropertyMap getProperties(){
        return properties;
    }
    
    public ItemMap getItems(){
        return items;
    }
    
    public List<String> getImports(){
        return imports;
    }
    
    public class ItemMap implements Map<String, List<PropertyMap>>{
        private final Map<String, List<PropertyMap>> map = new HashMap<String, List<PropertyMap>>();
        
        public ItemMap(){
        }
        
        public ItemMap(ItemMap itemMap){
            for(String key : itemMap.keySet()){
                List<PropertyMap> list = new ArrayList<PropertyMap>();
                map.put(key, list);
                
                for(PropertyMap property : itemMap.get(key)){    
                    list.add(new PropertyMap(property));
                }
            }
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }

        @Override
        public List<PropertyMap> get(Object key) {
            if(!map.containsKey((String)key)){
                map.put((String)key, new ArrayList<PropertyMap>());
            }
            return map.get(key);
        }

        @Override
        public List<PropertyMap> put(String key, List<PropertyMap> value) {
            return map.put(key, value);
        }

        @Override
        public List<PropertyMap> remove(Object key) {
            return map.remove(key);
        }

        @Override
        public void putAll(Map<? extends String, ? extends List<PropertyMap>> m) {
            map.putAll(m);
        }

        @Override
        public void clear() {
            map.clear();
        }

        @Override
        public Set<String> keySet() {
            return map.keySet();
        }

        @Override
        public Collection<List<PropertyMap>> values() {
            return map.values();
        }

        @Override
        public Set<Entry<String, List<PropertyMap>>> entrySet() {
            return map.entrySet();
        }
    };
    
    public static class PropertyMap implements Map<String, String>{
        private final Map<String, String> map = new HashMap<String, String>();
        
        public PropertyMap(){
        }
        
        public PropertyMap(PropertyMap propertyMap){
            map.putAll(propertyMap);
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return true;
        }

        @Override
        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }

        @Override
        public String get(Object key) {
            if(!map.containsKey((String)key)){
                map.put((String)key, "");
            }
            
            return map.get(key);
        }

        @Override
        public String put(String key, String value) {
            return map.put(key, value);
        }

        @Override
        public String remove(Object key) {
            return map.remove(key);
        }

        @Override
        public void putAll(Map<? extends String, ? extends String> m) {
            map.putAll(m);
        }

        @Override
        public void clear() {
            map.clear();
        }

        @Override
        public Set<String> keySet() {
            return map.keySet();
        }

        @Override
        public Collection<String> values() {
            return map.values();
        }

        @Override
        public Set<Entry<String, String>> entrySet() {
            return map.entrySet();
        }
    };
}
