package it.greenvulcano.frag.model.files;

import it.greenvulcano.frag.Main;
import it.greenvulcano.frag.util.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class GVFrag {

    private static final String name = "GVFrag.xml";

    public static Document read(String path) throws ParserConfigurationException, SAXException, IOException {
        return Utils.fileToDocument(new java.io.File(path));
    }

    public static void create(Document skeleton) throws TransformerException, IOException {
        new File(skeleton, Main.BASE_PATH, name).create();
        System.out.println(String.format("Generated %s/%s",
                Main.BASE_PATH, name));
    }


}
