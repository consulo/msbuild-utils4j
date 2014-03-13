/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.io.IOException;
import java.io.OutputStream;
import org.bromix.msbuild.elements.Item;
import org.bromix.msbuild.elements.ItemGroup;
import org.bromix.msbuild.elements.ItemMetadata;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectWriterTest {
    
    public class XmlOutputStream extends OutputStream{
        private String xml = "";
        
        @Override
        public void write(int b) throws IOException {    
            xml+=Character.toString((char)b);
        }
    };
    
    public ProjectWriterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Test
    public void testWrite() throws ProjectIOException{
        Project project = new Project();
        project.setToolsVersion("4.0");
        project.setDefaultTargets("Build");
        
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setLabel("ProjectConfigurations");
        project.add(itemGroup);
        
        Item item = new Item("ProjectConfiguration", "Debug|Win32", new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'"));
        itemGroup.add(item);
        
        ProjectWriter writer = new ProjectWriter();
        OutputStream stream = new XmlOutputStream();
        writer.write(project, stream);
        int x=0;
    }
}
