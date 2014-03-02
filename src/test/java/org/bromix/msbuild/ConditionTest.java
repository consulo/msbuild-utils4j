/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.io.File;
import java.net.URI;
import junit.framework.TestCase;

/**
 *
 * @author Matthias Bromisch
 */
public class ConditionTest extends TestCase {
    
    public ConditionTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of evaluate method, of class Condition.
     */
    public void testEvaluateString() throws Exception {
        System.out.println("'|'=='Debug|Win32' (false)");
        Condition instance1 = new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'");
        boolean expResult1 = false;
        boolean result1 = instance1.evaluate();
        assertEquals(expResult1, result1);
        
        System.out.println("'Debug|Win32'=='Debug|Win32' (true)");
        Condition instance2 = new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'");
        boolean expResult2 = true;
        ConditionContext conditionContext2 = new ConditionContext();
        conditionContext2.set("$(Configuration)", "Debug");
        conditionContext2.set("$(Platform)", "Win32");
        boolean result2 = instance2.evaluate(conditionContext2);
        assertEquals(expResult2, result2);
        
        System.out.println("'Release|Win32'!='Debug|Win32' (true)");
        Condition instance3 = new Condition("'$(Configuration)|$(Platform)'!='Debug|Win32'");
        boolean expResult3 = true;
        ConditionContext conditionContext3 = new ConditionContext();
        conditionContext3.set("$(Configuration)", "Release");
        conditionContext3.set("$(Platform)", "Win32");
        boolean result3 = instance3.evaluate(conditionContext3);
        assertEquals(expResult3, result3);
    }
    
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
    
    public void testEvaluateAndOr() throws Exception {
        System.out.println("'$(Configuration)'=='Debug' AND '$(Platform)'=='Win32' (true)");
        Condition instance1 = new Condition("'$(Configuration)'=='Debug' and '$(Platform)'=='Win32'");
        boolean expResult1 = true;
        ConditionContext conditionContext1 = new ConditionContext();
        conditionContext1.set("$(Configuration)", "Debug");
        conditionContext1.set("$(Platform)", "Win32");
        boolean result1 = instance1.evaluate(conditionContext1);
        assertEquals(expResult1, result1);
        
        System.out.println("'$(Configuration)'=='Debug' OR '$(Platform)'=='Win32' (true)");
        Condition instance2 = new Condition("'$(Configuration)'=='Debug' or '$(Platform)'=='Win32'");
        boolean expResult2 = true;
        ConditionContext conditionContext2 = new ConditionContext();
        conditionContext2.set("$(Configuration)", "Release");
        conditionContext2.set("$(Platform)", "Win32");
        boolean result2 = instance2.evaluate(conditionContext2);
        assertEquals(expResult2, result2);
        
        System.out.println("'$(Configuration)'=='Debug' OR '$(Platform)'=='Win32' (false)");
        Condition instance3 = new Condition("'$(Configuration)'=='Debug' or '$(Platform)'=='Win32'");
        boolean expResult3 = false;
        ConditionContext conditionContext3 = new ConditionContext();
        conditionContext3.set("$(Configuration)", "Release");
        conditionContext3.set("$(Platform)", "x64");
        boolean result3 = instance3.evaluate(conditionContext3);
        assertEquals(expResult3, result3);
    }

    /**
     * Test of isEmpty method, of class Condition.
     */
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
    public void testToString() {
        System.out.println("toString");
        Condition instance = new Condition("'A'=='B'");
        String expResult = "'A'=='B'";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
