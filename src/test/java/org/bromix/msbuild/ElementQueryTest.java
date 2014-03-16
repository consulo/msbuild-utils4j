/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.util.List;
import java.util.Map;
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
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
                "  </PropertyGroup>\n" +
                "  <PropertyGroup Condition=\"'$(Configuration)|$(Platform)'=='Release|Win32'\" Label=\"Configuration\">\n" +
                "    <ConfigurationType>StaticLibrary</ConfigurationType>\n" +
                "    <UseDebugLibraries>false</UseDebugLibraries>\n" +
                "    <WholeProgramOptimization>true</WholeProgramOptimization>\n" +
                "    <CharacterSet>Unicode</CharacterSet>\n" +
                "  </PropertyGroup>\n" +
                "  <Import Project=\"$(VCTargetsPath)\\Microsoft.Cpp.props\" />\n" +
                "  <ImportGroup Label=\"ExtensionSettings\">\n" +
                "  </ImportGroup>\n" +
                "  <ImportGroup Label=\"PropertySheets\" Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\">\n" +
                "    <Import Project=\"$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props\" Condition=\"exists('$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props')\" Label=\"LocalAppDataPlatform\" />\n" +
                "  </ImportGroup>\n" +
                "  <ImportGroup Label=\"PropertySheets\" Condition=\"'$(Configuration)|$(Platform)'=='Release|Win32'\">\n" +
                "    <Import Project=\"$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props\" Condition=\"exists('$(UserRootDir)\\Microsoft.Cpp.$(Platform).user.props')\" Label=\"LocalAppDataPlatform\" />\n" +
                "  </ImportGroup>\n" +
                "  <PropertyGroup Label=\"UserMacros\" />\n" +
                "  <PropertyGroup />\n" +
                "  <ItemDefinitionGroup Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\">\n" +
                "    <ClCompile>\n" +
                "      <PrecompiledHeader>Use</PrecompiledHeader>\n" +
                "      <WarningLevel>Level3</WarningLevel>\n" +
                "      <Optimization>Disabled</Optimization>\n" +
                "      <PreprocessorDefinitions>WIN32;_DEBUG;_LIB;%(PreprocessorDefinitions)</PreprocessorDefinitions>\n" +
                "    </ClCompile>\n" +
                "    <Link>\n" +
                "      <SubSystem>Windows</SubSystem>\n" +
                "      <GenerateDebugInformation>true</GenerateDebugInformation>\n" +
                "    </Link>\n" +
                "  </ItemDefinitionGroup>\n" +
                "  <ItemDefinitionGroup Condition=\"'$(Configuration)|$(Platform)'=='Release|Win32'\">\n" +
                "    <ClCompile>\n" +
                "      <WarningLevel>Level3</WarningLevel>\n" +
                "      <PrecompiledHeader>Use</PrecompiledHeader>\n" +
                "      <Optimization>MaxSpeed</Optimization>\n" +
                "      <FunctionLevelLinking>true</FunctionLevelLinking>\n" +
                "      <IntrinsicFunctions>true</IntrinsicFunctions>\n" +
                "      <PreprocessorDefinitions>WIN32;NDEBUG;_LIB;%(PreprocessorDefinitions)</PreprocessorDefinitions>\n" +
                "    </ClCompile>\n" +
                "    <Link>\n" +
                "      <SubSystem>Windows</SubSystem>\n" +
                "      <GenerateDebugInformation>true</GenerateDebugInformation>\n" +
                "      <EnableCOMDATFolding>true</EnableCOMDATFolding>\n" +
                "      <OptimizeReferences>true</OptimizeReferences>\n" +
                "    </Link>\n" +
                "  </ItemDefinitionGroup>\n" +
                "  <ItemGroup>\n" +
                "    <None Include=\"ReadMe.txt\" />\n" +
                "  </ItemGroup>\n" +
                "  <ItemGroup>\n" +
                "    <ClInclude Include=\"stdafx.h\" />\n" +
                "    <ClInclude Include=\"targetver.h\" />\n" +
                "  </ItemGroup>\n" +
                "  <ItemGroup>\n" +
                "    <ClCompile Include=\"stdafx.cpp\">\n" +
                "      <PrecompiledHeader Condition=\"'$(Configuration)|$(Platform)'=='Debug|Win32'\">Create</PrecompiledHeader>\n" +
                "      <PrecompiledHeader Condition=\"'$(Configuration)|$(Platform)'=='Release|Win32'\">Create</PrecompiledHeader>\n" +
                "    </ClCompile>\n" +
                "  </ItemGroup>\n" +
                "  <Import Project=\"$(VCTargetsPath)\\Microsoft.Cpp.targets\" />\n" +
                "  <ImportGroup Label=\"ExtensionTargets\">\n" +
                "  </ImportGroup>\n" +
                "</Project>\n";
        
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
    public void testProjectGuid() throws ConditionException{
        ProjectQuery query = new ProjectQuery(project);
        
        ProjectQuery.Context context = new ProjectQuery.Context();
        query.query(context);
        
        context = new ProjectQuery.Context();
        context.getProperties().put("Configuration", "Release");
        context.getProperties().put("Platform", "Win32");
        query.query(context);
        int x=0;
    }
}
