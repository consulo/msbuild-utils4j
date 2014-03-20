/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author braincrusher
 */
public class TextExtenderTest {
    
    public TextExtenderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of extend method, of class TextExtender.
     */
    @Test
    public void testExtendAllKnownVariables() {
        System.out.println("extend");
        String text = "'$(Configuration)|$(Platform)'";
        ProjectContext context = new ProjectContext();
        context.getProperties().put("Platform", "Win32");
        context.getProperties().put("Configuration", "Debug");
        TextExtender instance = new TextExtender(context);
        String expResult = "'Debug|Win32'";
        String result = instance.extend(text);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testExtendWithMissingVariable() {
        System.out.println("extend");
        String text = "$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props";
        ProjectContext context = new ProjectContext();
        context.getProperties().put("Platform", "Win32");
        context.getProperties().put("Configuration", "Debug");
        TextExtender instance = new TextExtender(context);
        String expResult = "\\Microsoft.Cpp.Win32.user.props";
        String result = instance.extend(text);
        assertEquals(expResult, result);
    }
}
