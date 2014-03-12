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
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.bromix.msbuild.elements.Element;
import org.bromix.msbuild.elements.Import;
import org.bromix.msbuild.elements.ImportGroup;
import org.bromix.msbuild.elements.Item;
import org.bromix.msbuild.elements.ItemDefinition;
import org.bromix.msbuild.elements.ItemDefinitionGroup;
import org.bromix.msbuild.elements.ItemGroup;
import org.bromix.msbuild.elements.ItemMetadata;
import org.bromix.msbuild.elements.Property;
import org.bromix.msbuild.elements.PropertyGroup;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.LocatedElement;
import org.jdom2.located.LocatedJDOMFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author braincrusher
 */
public class ProjectReaderTest {
    File projectFile = null;
    
    public ProjectReaderTest() throws URISyntaxException {
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
    
    private org.jdom2.located.LocatedElement parse(String xml) throws JDOMException, IOException{
        SAXBuilder builder = new SAXBuilder();
        builder.setJDOMFactory(new LocatedJDOMFactory());
        
        Document document = builder.build(StringToInputStream(xml));
        
        return (LocatedElement)document.getRootElement();
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
        
        ProjectReader reader = new ProjectReader();
        Project project = reader.read(StringToInputStream(xml));
        
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
        
        ProjectReader reader = new ProjectReader();
        ItemGroup itemGroup = (ItemGroup)reader.readElement(parse(xml), ItemGroup.class);
        
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
        
        ProjectReader reader = new ProjectReader();
        Item item = (Item)reader.readElement(parse(xml), Item.class);
        
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
        
        ProjectReader reader = new ProjectReader();
        PropertyGroup propertyGroup = (PropertyGroup)reader.readElement(parse(xml), PropertyGroup.class);
        
        assertEquals("Globals", propertyGroup.getLabel());
        assertEquals(3, propertyGroup.getChildren().size());
        assertEquals(3, propertyGroup.getProperties().size());
    }
    
    @Test
    public void testProperty() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<ProjectGuid>{9EFDFFFB-0D2A-4A0E-A5C8-B460D0FE413A}</ProjectGuid>";
        
        ProjectReader reader = new ProjectReader();
        Property property = (Property)reader.readElement(parse(xml), Property.class);
        
        assertEquals("{9EFDFFFB-0D2A-4A0E-A5C8-B460D0FE413A}", property.getValue());
        assertEquals("ProjectGuid", property.getElementName());
        assertEquals("ProjectGuid", property.getName());
    }
    
    @Test
    public void testImportGroup() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<ImportGroup Label=\"PropertySheets\" Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\">\n" +
                "   <Import Project=\"$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props\" Condition=\"exists('$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props')\" Label=\"LocalAppDataPlatform\" />\n" +
                "</ImportGroup>";
        
        ProjectReader reader = new ProjectReader();
        ImportGroup importGroup = (ImportGroup)reader.readElement(parse(xml), ImportGroup.class);
        
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
        
        ProjectReader reader = new ProjectReader();
        Import _import = (Import)reader.readElement(parse(xml), Import.class);
        
        assertEquals("$(VCTargetsPath)\\Microsoft.Cpp.Default.props", _import.getProject());
    }
    
    @Test
    public void testItemDefinitionGroup() throws ProjectIOException, JDOMException, IOException{
        String xml =
                "<ItemDefinitionGroup Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\">\n" +
                "   <ClCompile/>\n" +
                "   <Link/>\n" +
                "</ItemDefinitionGroup>";
        
        ProjectReader reader = new ProjectReader();
        ItemDefinitionGroup itemDefinitionGroup = (ItemDefinitionGroup)reader.readElement(parse(xml), ItemDefinitionGroup.class);
        
        assertEquals(false, itemDefinitionGroup.getCondition().isEmpty());
        assertEquals("'$(Configuration)|$(Platform)'=='Debug|Win32'", itemDefinitionGroup.getCondition().toString());
        assertEquals(2, itemDefinitionGroup.getChildren().size());
        assertEquals(2, itemDefinitionGroup.getItems().size());
    }
}
