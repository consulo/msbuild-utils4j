package org.bromix.msbuild;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.located.LocatedJDOMFactory;

/**
 *
 * @author Matthias Bromisch
 */
public class ProjectReader {
    public Project read(File file) throws ProjectIOException{
        
        SAXBuilder builder = new SAXBuilder();
        builder.setJDOMFactory(new LocatedJDOMFactory());
        Document document = null;
        try {
            document = builder.build(file);
        } catch (JDOMException ex) {
            throw new ProjectIOException(ex);
        } catch (IOException ex) {
            throw new ProjectIOException(ex);
        }
        
        List<Namespace> list = document.getNamespacesInScope();
        list = document.getNamespacesIntroduced();
        
        Element root = document.getRootElement();
        
        return null;
    }
}
