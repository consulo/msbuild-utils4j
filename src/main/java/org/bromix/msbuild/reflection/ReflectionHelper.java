package org.bromix.msbuild.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to help with some needed reflections.
 * @author Matthias Bromisch
 */
final public class ReflectionHelper {
    /**
     * Returns a list of all declared fields of a class.
     * @param cls Class the collect the fields of.
     * @param includeSuperClass <code>true</code> if super classes should be included otherwise <code>false</code>
     * @return list declared fields
     * @see Field
     */
    public static List<Field> getDeclearedFields(Class cls, boolean includeSuperClass){
        List<Field> fields = new ArrayList<>();
        
        while(cls!=null){
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
            
            if(includeSuperClass){
                cls = cls.getSuperclass();
            }
            else{
                cls = null;
            }
        }
        
        return fields;
    }
    
    /**
     * Returns a list of declared of fields with the given annotation.
     * @param cls Class to collect the fields of.
     * @param includeSuperClass <code>true</code> if super classes should be included.
     * @param annotationClass
     * @return list of fields with the given annotation.
     */
    public static List<Field> getDeclaredFieldsWithAnnotation(Class cls, boolean includeSuperClass, Class annotationClass){
        List<Field> fields = new ArrayList<>();
        
        for(Field field : getDeclearedFields(cls, includeSuperClass)){
            if(field.isAnnotationPresent(annotationClass)){
                fields.add(field);
            }
        }
        
        return fields;
    }
}
