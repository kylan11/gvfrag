package it.greenvulcano.frag.model.builders;

import it.greenvulcano.frag.Main;
import it.greenvulcano.frag.model.files.File;
import it.greenvulcano.frag.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class AdaptersBuilder extends Builder {

    final String OUTPUT_BASE_PATH = Main.BASE_PATH + "/GVAdapters";
    final String BASE_XPATH = "/GVCore/GVAdapters";

    @Override
    public void build(Document doc) throws XPathExpressionException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        Node adapters = (Node) xpath.compile(BASE_XPATH).evaluate(doc, XPathConstants.NODE);
        String fileName = adapters.getNodeName() + ".xml";
        outputFiles.add(new File(Utils.nodeToDocument(adapters), OUTPUT_BASE_PATH, fileName));
        removeFromSkeleton(adapters);
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        Node adapters = Utils.fileToDocument(new java.io.File(path + "/" + "GVAdapters/GVAdapters.xml")).getFirstChild();
        gvfrag.getFirstChild().appendChild(gvfrag.adoptNode(adapters));
    }
}
