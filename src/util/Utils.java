package util;

import model.OutputFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Utils {

    private static final String TO_REPLACE = "<!DOCTYPE GVCore SYSTEM \"http://www.greenvulcano.com/gvesb/dtds/GVCore.dtd\">";

    // returns an iterator of NodeList
    public static Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {

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

    public static Document readGVCore(String path) throws IOException, ParserConfigurationException, SAXException {
        String gvcore = new String(Files.readAllBytes(Paths.get(path))).replace(TO_REPLACE, "\n");
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(gvcore));
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    }

    public static String writeGVCore(Document doc) throws TransformerException, IOException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.greenvulcano.com/gvesb/dtds/GVCore.dtd");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(new DOMSource(doc), result);
        BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
        StringBuilder buf = new StringBuilder();
        try {
            final String NL = System.getProperty("line.separator", "\r\n");
            String line;
            while( (line=reader.readLine())!=null ) {
                if (!line.trim().isEmpty()) {
                    buf.append(line);
                    buf.append(NL);
                }
            }
        } finally {
            reader.close();
        }
        return buf.toString();
    }

    // utility method to convert a file into a document
    public static Document fileToDocument(File gvcore)
            throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(gvcore);
    }

    // utility methot to convert a string into a document
    public static Document toDocument(String data)
            throws SAXException, IOException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(data);
    }

    public static Document newDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        return builder.newDocument();
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

    public static Element createElement(Element parent, String name) {
        Document document;
        Element element;

        document = parent.getOwnerDocument();
        element  = document.createElement(name);

        parent.appendChild(element);
        return element;
    }
}
