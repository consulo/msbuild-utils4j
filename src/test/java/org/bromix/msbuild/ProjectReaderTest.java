/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.io.File;
import java.net.URI;
import java.util.List;
import junit.framework.TestCase;
import org.bromix.msbuild.elements.Element;
import org.bromix.msbuild.elements.Import;
import org.bromix.msbuild.elements.ImportGroup;
import org.bromix.msbuild.elements.ItemDefinitionGroup;
import org.bromix.msbuild.elements.Item;
import org.bromix.msbuild.elements.ItemGroup;
import org.bromix.msbuild.elements.ItemMetadata;
import org.bromix.msbuild.elements.Property;
import org.bromix.msbuild.elements.PropertyGroup;

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
        //URI uri = this.getClass().getResource("/vs2010/CsStaticLib/CsStaticLib.csproj").toURI();
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
    public void testBasicStructureOfElements() throws Exception {
        System.out.println("read and test the basic structure");
        ProjectReader instance = new ProjectReader();
        Project project = instance.read(projectFile);
        
        // Project
        List<Element> elements = project.getChildren();
        assertEquals(18, elements.size());
        
        // ItemGroup (ProjectConfigurations)
        Element element = elements.get(0);
        assertEquals("ItemGroup", element.getElementName());
        testItemGroupProjectConfigurations((ItemGroup)element);
        
        // PropertyGroup (Globals)
        element = elements.get(1);
        assertEquals("PropertyGroup", element.getElementName());
        assertEquals("Globals", element.getLabel());
        testPropertyGroupGlobals((PropertyGroup)element);
        
        // Import (Index 2)
        element = elements.get(2);
        assertEquals("Import", element.getElementName());
        
        // PropertyGroup (Configuration=Debug|Win32)
        element = elements.get(3);
        assertEquals("PropertyGroup", element.getElementName());
        testPropertyGroupConfiguration((PropertyGroup)element, true);
        
        // PropertyGroup (Configuration=Release|Win32)
        element = elements.get(4);
        assertEquals("PropertyGroup", element.getElementName());
        testPropertyGroupConfiguration((PropertyGroup)element, false);
        
        // Import (Index 5)
        element = elements.get(5);
        assertEquals("Import", element.getElementName());
        
        // ImportGroup (ExtensionSettings) (Index 6)
        element = elements.get(6);
        assertEquals("ImportGroup", element.getElementName());
        assertEquals("ExtensionSettings", element.getLabel());
        
        // ImportGroup (LocalAppDataPlatform=Debug|Win32)
        element = elements.get(7);
        assertEquals("ImportGroup", element.getElementName());
        assertEquals("PropertySheets", element.getLabel());
        testImportGroupLocalAppDataPlatform((ImportGroup)element, true);
        
        // ImportGroup (LocalAppDataPlatform=Release|Win32)
        element = elements.get(8);
        assertEquals("ImportGroup", element.getElementName());
        assertEquals("PropertySheets", element.getLabel());
        testImportGroupLocalAppDataPlatform((ImportGroup)element, false);
        
        // PropertyGroup (UserMacros) (Index 9)
        element = elements.get(9);
        assertEquals("PropertyGroup", element.getElementName());
        assertEquals("UserMacros", element.getLabel());
        
        // PropertyGroup (Index 10)
        element = elements.get(10);
        assertEquals("PropertyGroup", element.getElementName());
        assertEquals("", element.getLabel());
        
        // ItemDefinitionGroup (Debug|Win32)
        element = elements.get(11);
        assertEquals("ItemDefinitionGroup", element.getElementName());
        testItemDefinitionGroup((ItemDefinitionGroup)element, true);
        
        // ItemDefinitionGroup (Release|Win32)
        element = elements.get(12);
        assertEquals("ItemDefinitionGroup", element.getElementName());
        testItemDefinitionGroup((ItemDefinitionGroup)element, false);
        
        // ItemGroup (Index 13)
        element = elements.get(13);
        assertEquals("ItemGroup", element.getElementName());
        
        // ItemGroup (Index 14)
        element = elements.get(14);
        assertEquals("ItemGroup", element.getElementName());
        
        // ItemGroup (ClCompile)
        element = elements.get(15);
        assertEquals("ItemGroup", element.getElementName());
        testItemGroupClCompile((ItemGroup)element);
        
        // Import (Index 16)
        element = elements.get(16);
        assertEquals("Import", element.getElementName());
        
        // ImportGroup (ExtensionTargets) (Index 17)
        element = elements.get(17);
        assertEquals("ImportGroup", element.getElementName());
        assertEquals("ExtensionTargets", element.getLabel());
    }
    
    private void testItemGroupProjectConfigurations(ItemGroup itemGroup){
        List<Item> items = itemGroup.getItems();
        assertEquals(2, items.size());
        
        for(Item item : items){
            assertEquals("ProjectConfiguration", item.getElementName());
            List<ItemMetadata> metadataList = item.getMetadataList();
            assertEquals(2, metadataList.size());
        }
    }

    private void testPropertyGroupGlobals(PropertyGroup propertyGroup) {
        List<Property> properties = propertyGroup.getProperties();
        assertEquals(3, properties.size());
    }

    private void testPropertyGroupConfiguration(PropertyGroup propertyGroup, boolean debug) {
        if(debug){
            List<Property> properties = propertyGroup.getProperties();
            assertEquals(3, properties.size());
        }
        else{
            List<Property> properties = propertyGroup.getProperties();
            assertEquals(4, properties.size());
        }
    }

    private void testImportGroupLocalAppDataPlatform(ImportGroup importGroup, boolean debug) {
        List<Import> imports = importGroup.getImports();
        assertEquals(1, imports.size());
    }

    private void testItemDefinitionGroup(ItemDefinitionGroup itemDefinitionGroup, boolean debug) {
        if(debug){
            List<Item> items = itemDefinitionGroup.getItems();
            assertEquals(2, items.size());
            
            // ClCompile
            Item item = items.get(0);
            List<ItemMetadata> metadataList = item.getMetadataList();
            assertEquals(4, metadataList.size());
            
            // Link
            item = items.get(1);
            metadataList = item.getMetadataList();
            assertEquals(2, metadataList.size());
        }
        else{
            List<Item> items = itemDefinitionGroup.getItems();
            assertEquals(2, items.size());
            
            // ClCompile
            Item item = items.get(0);
            List<ItemMetadata> metadataList = item.getMetadataList();
            assertEquals(6, metadataList.size());
            
            // Link
            item = items.get(1);
            metadataList = item.getMetadataList();
            assertEquals(4, metadataList.size());
        }
    }

    private void testItemGroupClCompile(ItemGroup itemGroup) {
        List<Item> items = itemGroup.getItems();
        assertEquals(1, items.size());
        
        // PrecompiledHeader
        Item item = items.get(0);
        
        List<ItemMetadata> metadataList = item.getMetadataList();
        assertEquals(2, metadataList.size());
    }
}
