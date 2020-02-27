package it.greenvulcano.frag.model.builders;

import it.greenvulcano.frag.model.files.File;
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

    public List<File> outputFiles = new ArrayList<>();

    // builds xml document from required xpath then returns a list of File objects it created.
    public abstract void build(Document doc) throws XPathExpressionException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException;

    public abstract void remake(Document doc, String path) throws ParserConfigurationException, SAXException, IOException;

    public static void removeFromSkeleton(Node firstNode) {
        firstNode.getParentNode().removeChild(firstNode);
    }

    public static void saveFiles(Builder... builders) throws IOException {
        for (Builder builder : builders) {
            for (File currentFile : builder.outputFiles) {
                currentFile.create();
                System.out.println(String.format("Generated %s/%s",
                        currentFile.getPathString(), currentFile.getName()));
            }
        }
        System.out.println("All done!");
    }

    public static void massBuild(Document rootDoc, Builder... builders) throws ParserConfigurationException, IOException, XPathExpressionException, TransformerException {
        for (Builder builder : builders) {
            builder.build(rootDoc);
        }
    }

    public static void massRemake(Document gvfrag, String basePath, Builder... builders) throws IOException, SAXException, ParserConfigurationException {
        for (Builder builder : builders) {
            builder.remake(gvfrag, basePath);
        }
    }
}
