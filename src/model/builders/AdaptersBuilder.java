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
import java.io.IOException;
import java.util.List;

public class AdaptersBuilder extends Builder {
    static {
        OUTPUT_BASE_PATH = "output/GVAdapters";
        BASE_XPATH = "/GVCore/GVAdapters";
    }

    @Override
    public List<OutputFile> build(Document doc) throws XPathExpressionException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        Node adapters = (Node) xpath.compile(BASE_XPATH).evaluate(doc, XPathConstants.NODE);
        String fileName = adapters.getNodeName() + ".xml";
        outputFiles.add(new OutputFile(Utils.nodeToDocument(adapters), OUTPUT_BASE_PATH, fileName));
        removeFromSkeleton(adapters);
        return outputFiles;
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        Node adapters = Utils.fileToDocument(new java.io.File(path + "/" + "GVAdapters/GVAdapters.xml")).getFirstChild();
        gvfrag.getFirstChild().appendChild(gvfrag.adoptNode(adapters));
    }
}
