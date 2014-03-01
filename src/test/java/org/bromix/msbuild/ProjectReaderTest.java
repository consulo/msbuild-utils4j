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
import org.bromix.msbuild.elements.ImportElement;
import org.bromix.msbuild.elements.ImportGroupElement;
import org.bromix.msbuild.elements.ItemDefinitionGroupElement;
import org.bromix.msbuild.elements.ItemElement;
import org.bromix.msbuild.elements.ItemGroupElement;
import org.bromix.msbuild.elements.ItemMetadataElement;
import org.bromix.msbuild.elements.PropertyElement;
import org.bromix.msbuild.elements.PropertyGroupElement;

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
        testItemGroupProjectConfigurations((ItemGroupElement)element);
        
        // PropertyGroup (Globals)
        element = elements.get(1);
        testPropertyGroupGlobals((PropertyGroupElement)element);
        
        // ignore Import (Index 2)
        
        // PropertyGroup (Configuration=Debug|Win32)
        element = elements.get(3);
        testPropertyGroupConfiguration((PropertyGroupElement)element, true);
        
        // PropertyGroup (Configuration=Release|Win32)
        element = elements.get(4);
        testPropertyGroupConfiguration((PropertyGroupElement)element, false);
        
        // ignore Import (Index 5)
        
        // ignore ImportGroup (ExtensionSettings) (Index 6)
        
        // ImportGroup (LocalAppDataPlatform=Debug|Win32)
        element = elements.get(7);
        testImportGroupLocalAppDataPlatform((ImportGroupElement)element, true);
        
        // ImportGroup (LocalAppDataPlatform=Release|Win32)
        element = elements.get(8);
        testImportGroupLocalAppDataPlatform((ImportGroupElement)element, false);
        
        // ignore PropertyGroup (UserMacros) (Index 9)
        
        // ignore PropertyGroup (Index 10)
        
        // ItemDefinitionGroup (Debug|Win32)
        element = elements.get(11);
        testItemDefinitionGroup((ItemDefinitionGroupElement)element, true);
        
        // ItemDefinitionGroup (Release|Win32)
        element = elements.get(12);
        testItemDefinitionGroup((ItemDefinitionGroupElement)element, false);
        
        // ignore ItemGroup (Index 13)
        
        // ignore ItemGroup (Index 14)
        
        // ItemGroup (ClCompile)
        element = elements.get(15);
        testItemGroupClCompile((ItemGroupElement)element);
        
        // ignore the rest
    }
    
    private void testItemGroupProjectConfigurations(ItemGroupElement itemGroup){
        List<ItemElement> items = itemGroup.getItems();
        assertEquals(2, items.size());
        
        for(ItemElement item : items){
            List<ItemMetadataElement> metadataList = item.getMetadataList();
            assertEquals(2, metadataList.size());
        }
    }

    private void testPropertyGroupGlobals(PropertyGroupElement propertyGroup) {
        List<PropertyElement> properties = propertyGroup.getProperties();
        assertEquals(3, properties.size());
    }

    private void testPropertyGroupConfiguration(PropertyGroupElement propertyGroup, boolean debug) {
        if(debug){
            List<PropertyElement> properties = propertyGroup.getProperties();
            assertEquals(3, properties.size());
        }
        else{
            List<PropertyElement> properties = propertyGroup.getProperties();
            assertEquals(4, properties.size());
        }
    }

    private void testImportGroupLocalAppDataPlatform(ImportGroupElement importGroup, boolean debug) {
        List<ImportElement> imports = importGroup.getImports();
        assertEquals(1, imports.size());
    }

    private void testItemDefinitionGroup(ItemDefinitionGroupElement itemDefinitionGroup, boolean debug) {
        if(debug){
            List<ItemElement> items = itemDefinitionGroup.getItems();
            assertEquals(2, items.size());
            
            // ClCompile
            ItemElement item = items.get(0);
            List<ItemMetadataElement> metadataList = item.getMetadataList();
            assertEquals(4, metadataList.size());
            
            // Link
            item = items.get(1);
            metadataList = item.getMetadataList();
            assertEquals(2, metadataList.size());
        }
        else{
            List<ItemElement> items = itemDefinitionGroup.getItems();
            assertEquals(2, items.size());
            
            // ClCompile
            ItemElement item = items.get(0);
            List<ItemMetadataElement> metadataList = item.getMetadataList();
            assertEquals(6, metadataList.size());
            
            // Link
            item = items.get(1);
            metadataList = item.getMetadataList();
            assertEquals(4, metadataList.size());
        }
    }

    private void testItemGroupClCompile(ItemGroupElement itemGroup) {
        List<ItemElement> items = itemGroup.getItems();
        assertEquals(1, items.size());
        
        // PrecompiledHeader
        ItemElement item = items.get(0);
        
        List<ItemMetadataElement> metadataList = item.getMetadataList();
        assertEquals(2, metadataList.size());
    }
}
