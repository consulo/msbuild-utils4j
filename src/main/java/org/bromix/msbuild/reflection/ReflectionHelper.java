package org.bromix.msbuild.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * Class to help with some needed reflections.
 * @author Matthias Bromisch
 */
final public class ReflectionHelper {
    private static Set<Class<?>> elementDefinitions = null;
    
    /**
     * Based on the {@link ElementDefinition} annotation this method tries to return
     * the corresponding class for the given name of the element.
     * @param elementName name of the element
     * @return class corresponding to the element
     */
    public static Class findClassForElement(String elementName){
        initElementDefinitions();
        
        for(Class cls : elementDefinitions){
            ElementDefinition elementDefinition = (ElementDefinition)cls.getAnnotation(ElementDefinition.class);
            
            if(elementDefinition!=null){
                String className = cls.getSimpleName();
                className = className.substring(0, 1).toUpperCase()+className.substring(1);
                if(elementDefinition.bind()!=null &&  !elementDefinition.bind().isEmpty()){
                    className =  elementDefinition.bind();
                }
                
                if(elementName.equals(className)){
                    return cls;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns a list of all declared fields of a class.
     * @param cls Class the collect the fields of.
     * @param includeSuperClass <code>true</code> if super classes should be included otherwise <code>false</code>
     * @return list declared fields
     * @see Field
     */
    public static List<Field> getDeclearedFields(Class cls, boolean includeSuperClass){
        List<Field> fields = new ArrayList<Field>();
        
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
        List<Field> fields = new ArrayList<Field>();
        
        for(Field field : getDeclearedFields(cls, includeSuperClass)){
            if(field.isAnnotationPresent(annotationClass)){
                fields.add(field);
            }
        }
        
        return fields;
    }
    
    private static void initElementDefinitions(){
        if(elementDefinitions==null){
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .filterInputsBy(new FilterBuilder().includePackage("org.bromix.msbuild"))
                    .setUrls(ClasspathHelper.forPackage("org.bromix.msbuild"))
                    .setScanners(new TypeAnnotationsScanner(), new MethodAnnotationsScanner(), new MethodParameterScanner())
            );
            elementDefinitions = reflections.getTypesAnnotatedWith(ElementDefinition.class);
        }
    }
}
