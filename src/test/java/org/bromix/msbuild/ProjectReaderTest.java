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
public class ProjectReaderTest extends TestCase {
    File projectFile = null;
    
    public ProjectReaderTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        URI uri = this.getClass().getResource("/vs2010/SomeStaticLib/SomeStaticLib.vcxproj").toURI();
        projectFile = new File(uri);
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of read method, of class ProjectReader.
     * @throws java.lang.Exception
     */
    public void testRead() throws Exception {
        System.out.println("read");
        ProjectReader instance = new ProjectReader();
        Project project = instance.read(projectFile);
        int x=0;
    }
    
}
