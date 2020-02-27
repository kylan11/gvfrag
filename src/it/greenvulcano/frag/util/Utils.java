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
import java.util.*;

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

    // takes variable num of strings and joins them to form a path
    public static String pathBuilder(String... paths) {
        return String.join("/", paths) + "/";
    }

    // /home/supermario/file.txt -> /home/supermario
    public static String absoluteToPath(String absolutePath) {
        List<String> l = new ArrayList<>(Arrays.asList(absolutePath.split("/")));
        l.remove(l.size() - 1);
        return absolutePath.startsWith("/") ? "/" + String.join("/", l) : String.join("/", l);
    }

    public static void invalidArgs() {
        System.out.println("Invalid arguments. Usage: java -jar gvfrag.jar [-S|-M] "
                + "/path/to/[GVCore.xml|GVFrag.xml] [/optional/output/folder]\n" +
                "Use --help for additional informations.");
    }

    public static void help() {
        System.out.println("Usage: java -jar gvfrag.jar [-S|-M] /path/to/[GVCore.xml|GVFrag.xml]"
                + "[/optional/output/folder]\n"
                + "[-S]: Split GVCore into folder components and produces GVFrag.xml\n"
                + "[-M]: Produce GVCore.xml from GVFrag.xml and attached folder components.\n"
                + "[--help]: Displays this message.");
    }

    public static void genericError() {
        System.out.println("Something went wrong. Program will now exit.");
    }

    public static void fileNotFound() {
        System.out.println("Required XML file was not found in given path.");
    }

    public static String getBasePath(String[] args) {
        try {
            return args[2];
        } catch (IndexOutOfBoundsException e) {
            return Main.BASE_PATH;
        }
    }
}
