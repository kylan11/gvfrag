package model.builders;

import model.OutputFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import util.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PolicyBuilder extends Builder {

    static {
        OUTPUT_BASE_PATH = "output/GVPolicy";
        BASE_XPATH = "/GVCore/GVPolicy";
    }

    @Override
    public List<OutputFile> build(Document doc) throws XPathExpressionException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        Node policy = (Node) xpath.compile(BASE_XPATH).evaluate(doc, XPathConstants.NODE);
        String fileName = policy.getNodeName() + ".xml";
        outputFiles.add(new OutputFile(Utils.nodeToDocument(policy), OUTPUT_BASE_PATH, fileName));
        removeFromSkeleton(policy);
        return outputFiles;
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        Node policy = Utils.fileToDocument(new File(path + "/" + "GVPolicy/GVPolicy.xml")).getFirstChild();
        gvfrag.getFirstChild().appendChild(gvfrag.adoptNode(policy));
    }

}
