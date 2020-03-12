package it.greenvulcano.frag.model.builders;

import it.greenvulcano.frag.Main;
import it.greenvulcano.frag.model.files.File;
import it.greenvulcano.frag.model.fs.PathResolver;
import it.greenvulcano.frag.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Objects;

public class SystemsBuilder extends Builder {

    final String OUTPUT_BASE_PATH = PathResolver.get(Main.BASE_PATH, "GVSystems");
    final String BASE_XPATH = "/GVCore/GVSystems";

    @Override
    public void build(Document doc) throws XPathExpressionException, ParserConfigurationException,
            TransformerFactoryConfigurationError, TransformerException, IOException {

        Node firstNode = (Node) xpath.compile(BASE_XPATH).evaluate(doc, XPathConstants.NODE);
        NodeList systems = (NodeList) xpath.compile("Systems/System").evaluate(firstNode, XPathConstants.NODESET);
        for (Node system : Utils.iterable(systems)) {
            String currentSystem = ((Element) system).getAttribute("id-system");
            NodeList channels = (NodeList) xpath.compile("Channel").evaluate(system, XPathConstants.NODESET);
            for (Node channel : Utils.iterable(channels)) {
                String fileName = ((Element) channel).getAttribute("id-channel") + ".xml";
                outputFiles.add(new File(Utils.nodeToDocument(channel), PathResolver.get(OUTPUT_BASE_PATH, currentSystem),
                        fileName));
            }
        }
        removeFromSkeleton(firstNode);
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        java.io.File rootDir = new java.io.File(PathResolver.get(path, "GVSystems"));
        Element GVSystems = gvfrag.createElement("GVSystems");
        GVSystems.setAttribute("name", "SYSTEMS");
        GVSystems.setAttribute("type", "module");

        java.io.File[] systemDirs = rootDir.listFiles();
        if(systemDirs != null && systemDirs.length != 0) {
            GVSystems.appendChild(createSystems(gvfrag, systemDirs));
        }
        gvfrag.getFirstChild().appendChild(GVSystems);
    }

    public Element createSystems(Document gvfrag, java.io.File[] systemDirs) throws ParserConfigurationException, SAXException, IOException {
        Element Systems = gvfrag.createElement("Systems");
        for (java.io.File currentSystem : systemDirs) {
            Element System = gvfrag.createElement("System");
            System.setAttribute("id-system", currentSystem.getName());
            System.setAttribute("system-activation", "on");

            java.io.File[] channelFiles = currentSystem.listFiles();

            for (java.io.File channelFile : Objects.requireNonNull(channelFiles)) {
                Node channel = Utils.fileToDocument(channelFile).getFirstChild();
                System.appendChild(gvfrag.adoptNode(channel));
            }
            Systems.appendChild(System);
        }
        return Systems;
    }

}
