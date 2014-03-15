/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author braincrusher
 */
public class ElementQueryTest {
    Project project;
    
    public ElementQueryTest() throws ProjectIOException {
        String xml = 
                "<Project DefaultTargets=\"Build\" ToolsVersion=\"4.0\" xmlns=\"http://schemas.microsoft.com/developer/msbuild/2003\">\n" +
                "  <ItemGroup Label=\"ProjectConfigurations\">\n" +
                "    <ProjectConfiguration Include=\"Debug|Win32\">\n" +
                "      <Configuration>Debug</Configuration>\n" +
                "      <Platform>Win32</Platform>\n" +
                "    </ProjectConfiguration>\n" +
                "    <ProjectConfiguration Include=\"Release|Win32\">\n" +
                "      <Configuration>Release</Configuration>\n" +
                "      <Platform>Win32</Platform>\n" +
                "    </ProjectConfiguration>\n" +
                "  </ItemGroup>\n" +
                "  <PropertyGroup Label=\"Globals\">\n" +
                "    <ProjectGuid>{9EFDFFFB-0D2A-4A0E-A5C8-B460D0FE413A}</ProjectGuid>\n" +
                "    <Keyword>Win32Proj</Keyword>\n" +
                "    <RootNamespace>SomeStaticLib</RootNamespace>\n" +
                "  </PropertyGroup>\n" +
                "  <Import Project=\"$(VCTargetsPath)\\Microsoft.Cpp.Default.props\" />\n" +
                "  <PropertyGroup Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\" Label=\"Configuration\">\n" +
                "    <ConfigurationType>StaticLibrary</ConfigurationType>\n" +
                "    <UseDebugLibraries>true</UseDebugLibraries>\n" +
                "    <CharacterSet>Unicode</CharacterSet>\n" +
                "  </PropertyGroup>\n"+
                "</Project>";
        
        MSBuildReader reader = new MSBuildReader();
        project = (Project)reader.readElement(xml, Project.class);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Test
    public void testProjectGuid(){
        ElementQuery query = new ElementQuery(project);
        
        List<Item> projectConfigurations = query.collect(Item.class, "ProjectConfiguration");
        for(Item item : projectConfigurations){
            query = new ElementQuery(item);
            
            ItemMetadata config = query.findFirst(ItemMetadata.class, "Configuration");
            ItemMetadata platform = query.findFirst(ItemMetadata.class, "Platform");
            int y=0;
        }
        int x=0;
    }
}
