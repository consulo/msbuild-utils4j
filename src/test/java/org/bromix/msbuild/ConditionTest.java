/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Matthias Bromisch
 */
public class ConditionTest {
    
    public ConditionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Microsoft writes some false conditions.
     * Some conditions are missing ''' to be recognized as strings. The condition
     * implementation should fix this issue.
     * @throws org.bromix.msbuild.ConditionException
     */
    @Test
    public void testConditionFix() throws ConditionException{
        Condition.ConditionFix fix = new Condition.ConditionFix("$(EnableManagedIncrementalBuild)=='' and '$(CLRSupport)'!='' and '$(CLRSupport)'!='false'\"");
        String fixedCondition = fix.getFixedCondition();
        assertEquals("'$(EnableManagedIncrementalBuild)'=='' and '$(CLRSupport)'!='' and '$(CLRSupport)'!='false'\"", fixedCondition);
        
        Condition.ConditionFix fix2 = new Condition.ConditionFix("'$(CLRSupport)'!='' and $(CLRSupport)!='false'\"");
        String fixedCondition2 = fix2.getFixedCondition();
        assertEquals("'$(CLRSupport)'!='' and '$(CLRSupport)'!='false'\"", fixedCondition2);
        
        Condition.ConditionFix fix3 = new Condition.ConditionFix("'$(Configuration)|$(Platform)'=='Debug|Win32'");
        String fixedCondition3 = fix3.getFixedCondition();
        assertEquals("'$(Configuration)|$(Platform)'=='Debug|Win32'", fixedCondition3);
    }

    /**
     * Test of evaluate method, of class Condition.
     * @throws java.lang.Exception
     */
    @Test
    public void testEvaluateString() throws Exception {
        System.out.println("'|'=='Debug|Win32' (false)");
        Condition instance1 = new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'");
        boolean expResult1 = false;
        boolean result1 = instance1.evaluate();
        assertEquals(expResult1, result1);
        
        System.out.println("'Debug|Win32'=='Debug|Win32' (true)");
        Condition instance2 = new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'");
        boolean expResult2 = true;
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("Configuration", "Debug");
        properties.put("Platform", "Win32");
        boolean result2 = instance2.evaluate(properties);
        assertEquals(expResult2, result2);
        
        System.out.println("'Release|Win32'!='Debug|Win32' (true)");
        Condition instance3 = new Condition("'$(Configuration)|$(Platform)'!='Debug|Win32'");
        boolean expResult3 = true;
        Map<String, String> properties2 = new HashMap<String, String>();
        properties2.put("Configuration", "Release");
        properties2.put("Platform", "Win32");
        boolean result3 = instance3.evaluate(properties2);
        assertEquals(expResult3, result3);
    }
    
    @Test
    public void testEvaluateNumberic() throws Exception {
        System.out.println("1>2 (false)");
        Condition instance1 = new Condition("1>2");
        boolean expResult1 = false;
        boolean result1 = instance1.evaluate();
        assertEquals(expResult1, result1);
        
        System.out.println("1<2 (true)");
        Condition instance2 = new Condition("1<2");
        boolean expResult2 = true;
        boolean result2 = instance2.evaluate();
        assertEquals(expResult2, result2);
        
        System.out.println("2<=2 (true)");
        Condition instance3 = new Condition("2<=2");
        boolean expResult3 = true;
        boolean result3 = instance3.evaluate();
        assertEquals(expResult3, result3);
        
        System.out.println("2>=2 (true)");
        Condition instance4 = new Condition("2>=2");
        boolean expResult4 = true;
        boolean result4 = instance4.evaluate();
        assertEquals(expResult4, result4);
    }
    
    @Test
    public void testEvaluateHasTrailingSlash() throws Exception {
        System.out.println("HasTrailingSlash('stringA') (false)");
        Condition instance1 = new Condition("HasTrailingSlash('stringA')");
        boolean expResult1 = false;
        boolean result1 = instance1.evaluate();
        assertEquals(expResult1, result1);
        
        System.out.println("!HasTrailingSlash('stringA') (true)");
        Condition instance2 = new Condition("!HasTrailingSlash('stringA')");
        boolean expResult2 = true;
        boolean result2 = instance2.evaluate();
        assertEquals(expResult2, result2);
        
        System.out.println("HasTrailingSlash('stringA/') (true)");
        Condition instance3 = new Condition("HasTrailingSlash('stringA/')");
        boolean expResult3 = true;
        boolean result3 = instance3.evaluate();
        assertEquals(expResult3, result3);
        
        System.out.println("!HasTrailingSlash('stringA/') (false)");
        Condition instance4 = new Condition("!HasTrailingSlash('stringA/')");
        boolean expResult4 = false;
        boolean result4 = instance4.evaluate();
        assertEquals(expResult4, result4);
        
        System.out.println("HasTrailingSlash('stringA\\') (true)");
        Condition instance5 = new Condition("HasTrailingSlash('stringA\\\')");
        boolean expResult5 = true;
        boolean result5 = instance5.evaluate();
        assertEquals(expResult5, result5);
        
        System.out.println("!HasTrailingSlash('stringA\\') (false)");
        Condition instance6 = new Condition("!HasTrailingSlash('stringA\\\')");
        boolean expResult6 = false;
        boolean result6 = instance6.evaluate();
        assertEquals(expResult6, result6);
    }
    
    @Test
    public void testEvaluateExists() throws Exception {
        URI fileUri = this.getClass().getResource("/vs2010/SomeStaticLib/SomeStaticLib.vcxproj").toURI();
        String file = new File(fileUri).toString();
        
        System.out.println("Exists('file') (true)");
        Condition instance1 = new Condition(String.format("Exists('%s')", file));
        boolean expResult1 = true;
        boolean result1 = instance1.evaluate();
        assertEquals(expResult1, result1);
        
        System.out.println("!Exists('file') (false)");
        Condition instance2 = new Condition(String.format("!Exists('%s')", file));
        boolean expResult2 = false;
        boolean result2 = instance2.evaluate();
        assertEquals(expResult2, result2);
        
        URI folderUri = this.getClass().getResource("/vs2010/SomeStaticLib").toURI();
        String folder = new File(folderUri).toString();
        
        System.out.println("Exists('folder') (true)");
        Condition instance3 = new Condition(String.format("Exists('%s')", folder));
        boolean expResult3 = true;
        boolean result3 = instance3.evaluate();
        assertEquals(expResult3, result3);
        
        System.out.println("!Exists('folder') (false)");
        Condition instance4 = new Condition(String.format("!Exists('%s')", folder));
        boolean expResult4 = false;
        boolean result4 = instance4.evaluate();
        assertEquals(expResult4, result4);
    }
    
    @Test
    public void testEvaluateAndOr() throws Exception {
        System.out.println("'$(Configuration)'=='Debug' AND '$(Platform)'=='Win32' (true)");
        Condition instance1 = new Condition("'$(Configuration)'=='Debug' and '$(Platform)'=='Win32'");
        boolean expResult1 = true;
        Map<String, String> properties1 = new HashMap<String, String>();
        properties1.put("Configuration", "Debug");
        properties1.put("Platform", "Win32");
        boolean result1 = instance1.evaluate(properties1);
        assertEquals(expResult1, result1);
        
        System.out.println("'$(Configuration)'=='Debug' OR '$(Platform)'=='Win32' (true)");
        Condition instance2 = new Condition("'$(Configuration)'=='Debug' or '$(Platform)'=='Win32'");
        boolean expResult2 = true;
        Map<String, String> properties2 = new HashMap<String, String>();
        properties2.put("Configuration", "Release");
        properties2.put("Platform", "Win32");
        boolean result2 = instance2.evaluate(properties2);
        assertEquals(expResult2, result2);
        
        System.out.println("'$(Configuration)'=='Debug' OR '$(Platform)'=='Win32' (false)");
        Condition instance3 = new Condition("'$(Configuration)'=='Debug' or '$(Platform)'=='Win32'");
        boolean expResult3 = false;
        Map<String, String> properties3 = new HashMap<String, String>();
        properties3.put("Configuration", "Release");
        properties3.put("Platform", "x64");
        boolean result3 = instance3.evaluate(properties3);
        assertEquals(expResult3, result3);
    }

    /**
     * Test of isEmpty method, of class Condition.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty (true)");
        Condition instance1 = new Condition();
        boolean expResult1 = true;
        boolean result1 = instance1.isEmpty();
        assertEquals(expResult1, result1);
        
        System.out.println("isEmpty (false)");
        Condition instance2 = new Condition("'A'=='B'");
        boolean expResult2 = false;
        boolean result2 = instance2.isEmpty();
        assertEquals(expResult2, result2);
    }

    /**
     * Test of toString method, of class Condition.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Condition instance = new Condition("'A'=='B'");
        String expResult = "'A'=='B'";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
