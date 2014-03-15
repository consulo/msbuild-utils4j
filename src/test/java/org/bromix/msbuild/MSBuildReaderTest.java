/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import static junit.framework.Assert.assertEquals;
import org.jdom2.JDOMException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Matthias Bromisch
 */
public class MSBuildReaderTest {
    File projectFile = null;
    
    public MSBuildReaderTest() throws URISyntaxException {
        URI uri = this.getClass().getResource("/vs2010/SomeStaticLib/SomeStaticLib.vcxproj").toURI();
        //URI uri = this.getClass().getResource("/vs2010/CsStaticLib/CsStaticLib.csproj").toURI();
        projectFile = new File(uri);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Helper method to convert a string into an <code>InputStream</code>.
     * @param xml part of xml.
     * @return <code>InputStream</code> of the given string.
     */
    private InputStream StringToInputStream(String xml){
        return new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
    }
    
    /**
     * Test for reading a Project Element (with only one child element).
     * @throws ProjectIOException 
     */
    @Test
    public void testProject() throws ProjectIOException{
        String xml =
                "<Project DefaultTargets=\"Build\" ToolsVersion=\"4.0\" xmlns=\"http://schemas.microsoft.com/developer/msbuild/2003\">" +
                "   <ItemGroup Label=\"ProjectConfigurations\"/>" +
                "</Project>";
        
        MSBuildReader reader = new MSBuildReader();
        Project project = reader.readProject(StringToInputStream(xml));
        
        assertEquals(Element.Type.Project, project.getElementType());
        assertEquals("Project", project.getElementName());
        assertEquals("Build", project.getDefaultTargets());
        assertEquals("4.0", project.getToolsVersion());
        assertEquals(1, project.getChildren().size());
    }
    
    @Test
    public void testItemGroup() throws IOException, JDOMException, ProjectIOException{
        String xml =
                "<ItemGroup Label=\"ProjectConfigurations\">\n" +
                "    <ProjectConfiguration Include=\"Debug|Win32\"/>\n" +
                "    <ProjectConfiguration Include=\"Release|Win32\"/>\n" +
                "</ItemGroup>";
        
        MSBuildReader reader = new MSBuildReader();
        ItemGroup itemGroup = reader.readElement(xml, ItemGroup.class);
        
        assertEquals(Element.Type.ItemGroup, itemGroup.getElementType());
        assertEquals("ItemGroup", itemGroup.getElementName());
        assertEquals("ProjectConfigurations", itemGroup.getLabel());
        assertEquals(2, itemGroup.getChildren().size());
        assertEquals(2, itemGroup.getItems().size());
    }
    
    @Test
    public void testItem() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<ProjectConfiguration Include=\"Debug|Win32\">\n" +
                "   <Configuration>Debug</Configuration>\n" +
                "   <Platform>Win32</Platform>\n" +
                "</ProjectConfiguration>";
        
        MSBuildReader reader = new MSBuildReader();
        Item item = reader.readElement(xml, Item.class);
        
        assertEquals(Element.Type.Item, item.getElementType());
        assertEquals("ProjectConfiguration", item.getElementName());
        assertEquals("ProjectConfiguration", item.getName());
        assertEquals("Debug|Win32", item.getInclude());
        assertEquals(2, item.getChildren().size());
        assertEquals(2, item.getMetadataList().size());
    }
    
    @Test
    public void testPropertyGroup() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<PropertyGroup Label=\"Globals\">\n" +
                "   <ProjectGuid>{9EFDFFFB-0D2A-4A0E-A5C8-B460D0FE413A}</ProjectGuid>\n" +
                "   <Keyword>Win32Proj</Keyword>\n" +
                "   <RootNamespace>SomeStaticLib</RootNamespace>\n" +
                "</PropertyGroup>";
        
        MSBuildReader reader = new MSBuildReader();
        PropertyGroup propertyGroup = reader.readElement(xml, PropertyGroup.class);
        
        assertEquals(Element.Type.PropertyGroup, propertyGroup.getElementType());
        assertEquals("PropertyGroup", propertyGroup.getElementName());
        assertEquals("Globals", propertyGroup.getLabel());
        assertEquals(3, propertyGroup.getChildren().size());
        assertEquals(3, propertyGroup.getProperties().size());
    }
    
    @Test
    public void testProperty() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<ProjectGuid>{9EFDFFFB-0D2A-4A0E-A5C8-B460D0FE413A}</ProjectGuid>";
        
        MSBuildReader reader = new MSBuildReader();
        Property property = reader.readElement(xml, Property.class);
        
        assertEquals(Element.Type.Property, property.getElementType());
        assertEquals("ProjectGuid", property.getElementName());
        assertEquals("{9EFDFFFB-0D2A-4A0E-A5C8-B460D0FE413A}", property.getValue());
        assertEquals("ProjectGuid", property.getName());
    }
    
    @Test
    public void testImportGroup() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<ImportGroup Label=\"PropertySheets\" Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\">\n" +
                "   <Import Project=\"$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props\" Condition=\"exists('$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props')\" Label=\"LocalAppDataPlatform\" />\n" +
                "</ImportGroup>";
        
        MSBuildReader reader = new MSBuildReader();
        ImportGroup importGroup = reader.readElement(xml, ImportGroup.class);
        
        assertEquals(Element.Type.ImportGroup, importGroup.getElementType());
        assertEquals("ImportGroup", importGroup.getElementName());
        assertEquals(false, importGroup.getCondition().isEmpty());
        assertEquals("'$(Configuration)|$(Platform)'=='Debug|Win32'", importGroup.getCondition().toString());
        assertEquals("PropertySheets", importGroup.getLabel());
        assertEquals(1, importGroup.getChildren().size());
        assertEquals(1, importGroup.getImports().size());
    }
    
    @Test
    public void testImport() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<Import Project=\"$(VCTargetsPath)\\Microsoft.Cpp.Default.props\" />";
        
        MSBuildReader reader = new MSBuildReader();
        Import _import = reader.readElement(xml, Import.class);
        
        assertEquals(Element.Type.Import, _import.getElementType());
        assertEquals("Import", _import.getElementName());
        assertEquals("$(VCTargetsPath)\\Microsoft.Cpp.Default.props", _import.getProject());
    }
    
    @Test
    public void testItemDefinitionGroup() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<ItemDefinitionGroup Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\">\n" +
                "   <ClCompile/>\n" +
                "   <Link/>\n" +
                "</ItemDefinitionGroup>";
        
        MSBuildReader reader = new MSBuildReader();
        ItemDefinitionGroup itemDefinitionGroup = reader.readElement(xml, ItemDefinitionGroup.class);
        
        assertEquals(Element.Type.ItemDefinitionGroup, itemDefinitionGroup.getElementType());
        assertEquals("ItemDefinitionGroup", itemDefinitionGroup.getElementName());
        assertEquals(false, itemDefinitionGroup.getCondition().isEmpty());
        assertEquals("'$(Configuration)|$(Platform)'=='Debug|Win32'", itemDefinitionGroup.getCondition().toString());
        assertEquals(2, itemDefinitionGroup.getChildren().size());
        assertEquals(2, itemDefinitionGroup.getItems().size());
    }
}
