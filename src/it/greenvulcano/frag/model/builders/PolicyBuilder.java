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

public class PolicyBuilder extends Builder {

    final String OUTPUT_BASE_PATH = Main.BASE_PATH + "/GVPolicy";
    final String BASE_XPATH = "/GVCore/GVPolicy";

    @Override
    public void build(Document doc) throws XPathExpressionException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        Node policy = (Node) xpath.compile(BASE_XPATH).evaluate(doc, XPathConstants.NODE);
        String fileName = policy.getNodeName() + ".xml";
        outputFiles.add(new File(Utils.nodeToDocument(policy), OUTPUT_BASE_PATH, fileName));
        removeFromSkeleton(policy);
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        Node policy = Utils.fileToDocument(new java.io.File(path + "/" + "GVPolicy/GVPolicy.xml")).getFirstChild();
        gvfrag.getFirstChild().appendChild(gvfrag.adoptNode(policy));
    }

}
