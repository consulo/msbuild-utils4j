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
import org.bromix.msbuild.elements.ItemDefinition;
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
    public void testReadStructureElementCount() throws Exception {
        System.out.println("read (structure: validate element count)");
        ProjectReader instance = new ProjectReader();
        Project project = instance.read(projectFile);
        
        // Project
        List<Element> elements = project.getChildren();
        assertEquals(18, elements.size());
        
        // ItemGroup (ProjectConfigurations)
        Element element = elements.get(0);
        testItemGroupProjectConfigurations((ItemGroup)element);
        
        // PropertyGroup (Globals)
        element = elements.get(1);
        testPropertyGroupGlobals((PropertyGroup)element);
        
        // ignore Import (Index 2)
        
        // PropertyGroup (Configuration=Debug|Win32)
        element = elements.get(3);
        testPropertyGroupConfiguration((PropertyGroup)element, true);
        
        // PropertyGroup (Configuration=Release|Win32)
        element = elements.get(4);
        testPropertyGroupConfiguration((PropertyGroup)element, false);
        
        // ignore Import (Index 5)
        
        // ignore ImportGroup (ExtensionSettings) (Index 6)
        
        // ImportGroup (LocalAppDataPlatform=Debug|Win32)
        element = elements.get(7);
        testImportGroupLocalAppDataPlatform((ImportGroup)element, true);
        
        // ImportGroup (LocalAppDataPlatform=Release|Win32)
        element = elements.get(8);
        testImportGroupLocalAppDataPlatform((ImportGroup)element, false);
        
        // ignore PropertyGroup (UserMacros) (Index 9)
        
        // ignore PropertyGroup (Index 10)
        
        // ItemDefinitionGroup (Debug|Win32)
        element = elements.get(11);
        testItemDefinitionGroup((ItemDefinition)element, true);
        
        // ItemDefinitionGroup (Release|Win32)
        element = elements.get(12);
        testItemDefinitionGroup((ItemDefinition)element, false);
        
        // ignore ItemGroup (Index 13)
        
        // ignore ItemGroup (Index 14)
        
        // ItemGroup (ClCompile)
        element = elements.get(15);
        testItemGroupClCompile((ItemGroup)element);
        
        // ignore the rest
    }
    
    private void testItemGroupProjectConfigurations(ItemGroup itemGroup){
        List<Item> items = itemGroup.getItems();
        assertEquals(2, items.size());
        
        for(Item item : items){
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

    private void testItemDefinitionGroup(ItemDefinition itemDefinitionGroup, boolean debug) {
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
