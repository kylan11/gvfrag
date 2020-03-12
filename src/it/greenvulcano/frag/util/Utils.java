package it.greenvulcano.frag.util;

import it.greenvulcano.frag.Main;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Utils {

    // returns an iterator of NodeList
    public static Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }

    // utility method to convert a file into a document
    public static Document fileToDocument(File gvcore)
            throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(gvcore);
    }

    // utility method to convert a node into a new document
    public static Document nodeToDocument(Node node) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Node importedNode = doc.importNode(node, true);
        doc.appendChild(importedNode);
        return doc;
    }


    public static void invalidArgs() {
        System.out.println("Invalid arguments. Usage: java -jar gvfrag.jar [-S|-M] "
                + "/path/to/[GVCore.xml|GVFrag.xml] [/optional/output/dir]\n" +
                "Use --help for additional info.");
    }

    public static void help() {
        System.out.println("Usage: java -jar gvfrag.jar [-S|-M] /path/to/[GVCore.xml|GVFrag.xml]"
                + "[/optional/output/dir]\n"
                + "[-s]: Split GVCore into folder components and produces GVFrag.xml\n"
                + "[-m]: Produce GVCore.xml from GVFrag.xml and attached folder components.\n"
                + "[-h|--help]: Displays this message.\n\n");
        System.out.println(String.format("GVFrag v%s\n" +
                "Java v%s\n" +
                "GreenVulcano ESB (GAIA) v%s", Main.CURRENT_VERSION, getJavaVersion(), Main.GAIA_VERSION));
    }

    public static void genericError() {
        System.out.println("Something went wrong. Program will now exit.");
    }

    public static void fileNotFound() {
        System.out.println("Required .xml file was not found in given path. Program will now exit.");
    }

    public static String getBasePath(String[] args) {
        try {
            return args[2];
        } catch (IndexOutOfBoundsException e) {
            return Main.BASE_PATH;
        }
    }

    public static String getJavaVersion() {
        return Runtime.class.getPackage().getImplementationVersion();
    }
}
