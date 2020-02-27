package it.greenvulcano.frag.model.files;

import it.greenvulcano.frag.Main;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class GVFrag {

    private static final String name = "GVFrag.xml";

    public static void create(Document skeleton) throws TransformerException, IOException {
        new File(skeleton, Main.BASE_PATH, name).create();
    }
}
