package org.bromix.msbuild;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Matthias Bromisch
 */
public class TextExtender {
    private final ProjectContext context;
    
    public TextExtender(ProjectContext context){
        this.context = context;
    }
    
    public String extend(String text){
        String result = extendProperties(text);
        return result;
    }
    
    private String extendProperties(String text){
        String result = text;
        
        Pattern pattern = Pattern.compile("\\$\\((.+?)\\)");
        Matcher matcher = pattern.matcher(result);
        while(matcher.find()){
            String property = matcher.group(1);
            MatchResult match =  matcher.toMatchResult();
            int start = match.start();
            int end = match.end();
            
            String front = result.substring(0, start);
            String tail = result.substring(end);
            result=front+context.getProperties().get(property)+tail;
            
            matcher = pattern.matcher(result);
        }
        return result;
    }
}
