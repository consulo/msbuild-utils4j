/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bromix.msbuild;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.OutputStream;
import static junit.framework.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.awt.datatransfer.TransferableProxy;

/**
 *
 * @author Matthias Bromisch
 */
public class MSBuildWriterTest {
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
    
    private void copyString(String string){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(string);
        clipboard.setContents(stringSelection, null);
    }
    
    private String normalizeString(String string){
        String sResult = string;
        
        sResult = sResult.replace("\r", "");
        sResult = sResult.replace("\n", "");
        sResult = sResult.replace("\t", "");
        
        return sResult;
    }
    
    public MSBuildWriterTest() {
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
        
        MSBuildWriter writer = new MSBuildWriter();
        XmlOutputStream stream = new XmlOutputStream();
        writer.writeProject(project, stream);
        
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Project xmlns=\"http://schemas.microsoft.com/developer/msbuild/2003\" DefaultTargets=\"Build\" ToolsVersion=\"4.0\" />\n";
        assertEquals(normalizeString(xml), normalizeString(stream.getXml()));
    }
    
    @Test
    public void testItemGroup() throws ProjectIOException{
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setLabel("ProjectConfigurations");
        
        Item item = itemGroup.addItem("ProjectConfiguration", "Debug|Win32");
        ItemMetadata itemMetadata = item.addMetadata("Configuration", "Debug");
        
        MSBuildWriter writer = new MSBuildWriter();
        XmlOutputStream stream = new XmlOutputStream();
        writer.writeElement(itemGroup, stream);
        
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ItemGroup Label=\"ProjectConfigurations\">\n" +
                "  <ProjectConfiguration Include=\"Debug|Win32\">\n" +
                "    <Configuration>Debug</Configuration>\n" +
                "  </ProjectConfiguration>\n" +
                "</ItemGroup>";
        assertEquals(normalizeString(xml), normalizeString(stream.getXml()));
    }
}
