package model.builders;

import model.OutputFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Builder {

    public static final XPath xpath = XPathFactory.newInstance().newXPath();

    public static List<OutputFile> outputFiles = new ArrayList<>();

    public static String BASE_XPATH = null;

    public static String OUTPUT_BASE_PATH = null;

    // builds xml document from required xpath then returns a list of File objects it created.
    public abstract List<OutputFile> build(Document doc) throws XPathExpressionException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException;

    public abstract void remake(Document doc, String path) throws ParserConfigurationException, SAXException, IOException;

    public static void removeFromSkeleton(Node firstNode) {
        firstNode.getParentNode().removeChild(firstNode);
    }

}
