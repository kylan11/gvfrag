package it.greenvulcano.frag.model.files;

import it.greenvulcano.frag.Main;
import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class GVCore {

    private static final String name = "GVCore.xml";

    private static final String doctype = "http://www.greenvulcano.com/gvesb/dtds/GVCore.dtd";

    public static void create(Document gvfrag) throws TransformerException, IOException {
        new File(docToString(gvfrag), Main.BASE_PATH, name).create();
    }

    public static String docToString(Document doc) throws TransformerFactoryConfigurationError, TransformerException, IOException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype);
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(new DOMSource(doc), result);
        BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
        StringBuilder buf = new StringBuilder();
        try {
            final String NL = System.getProperty("line.separator", "\r\n");
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    buf.append(line);
                    buf.append(NL);
                }
            }
        } finally {
            reader.close();
        }
        return buf.toString();
    }
}
