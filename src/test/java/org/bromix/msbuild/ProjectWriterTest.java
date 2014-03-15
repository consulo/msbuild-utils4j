/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.io.IOException;
import java.io.OutputStream;
import static junit.framework.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectWriterTest {
    /**
     * Dummy class to present the xml output.
     */
    public class XmlOutputStream extends OutputStream{
        private String xml = "";
        
        @Override
        public void write(int b) throws IOException {    
            xml+=Character.toString((char)b);
        }
        
        /**
         * Returns the xml as string.
         * @return xml content as string.
         */
        public String getXml(){
            return xml;
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
    public void testProject() throws ProjectIOException{
        Project project = new Project();
        project.setToolsVersion("4.0");
        project.setDefaultTargets("Build");
        
        ItemGroup itemGroup = project.addItemGroup();
        itemGroup.setLabel("ProjectConfigurations");
        
        Item item = itemGroup.addItem("ProjectConfiguration", "Debug|Win32", new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'"));
        
        item.addMetadata("Configuration", "Debug");
        item.addMetadata("Platform", "Win32");
        
        PropertyGroup propertyGroup = project.addPropertyGroup();
        propertyGroup.setLabel("Globals");
        propertyGroup.addProperty("ProjectGuid", "{9EFDFFFB-0D2A-4A0E-A5C8-B460D0FE413A}");
        
        project.addImport("$(VCTargetsPath)\\Microsoft.Cpp.Default.props");
        
        propertyGroup = project.addPropertyGroup(new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'"));
        propertyGroup.setLabel("Configuration");
        propertyGroup.addProperty("ConfigurationType", "StaticLibrary");
        
        ImportGroup importGroup = project.addImportGroup();
        importGroup.addImport("$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props");
        
        ItemDefinitionGroup itemDefinitionGroup = project.addItemDefinitionGroup(new Condition("'$(Configuration)|$(Platform)'=='Debug|Win32'"));
        ItemDefinition itemDefinition = itemDefinitionGroup.addItemDefinition("ClCompile");
        itemDefinition.addMetadata("WarningLevel", "Level3");
        
        ProjectWriter writer = new ProjectWriter();
        XmlOutputStream stream = new XmlOutputStream();
        writer.write(project, stream);
        
        String xml = stream.getXml();
        
        assertEquals(false, xml.isEmpty());
    }
}
