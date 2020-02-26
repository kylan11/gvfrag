package model.builders;

import model.OutputFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.Objects;

public class SystemsBuilder extends Builder {

    static {
        OUTPUT_BASE_PATH = "output/GVSystems";
        BASE_XPATH = "/GVCore/GVSystems";
    }

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
                outputFiles.add(new OutputFile(Utils.nodeToDocument(channel), Utils.pathBuilder(OUTPUT_BASE_PATH, currentSystem),
                        fileName));
            }
        }
        removeFromSkeleton(firstNode);
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        File rootDir = new File(path + "/GVSystems");
        Element GVSystems = gvfrag.createElement("GVSystems");
        GVSystems.setAttribute("name", "SYSTEMS");
        GVSystems.setAttribute("type", "module");

        File[] systemDirs = rootDir.listFiles();
        if(systemDirs != null && systemDirs.length != 0) {
            GVSystems.appendChild(createSystems(gvfrag, systemDirs));
        }
        gvfrag.getFirstChild().appendChild(GVSystems);
    }

    public Element createSystems(Document gvfrag, File[] systemDirs) throws ParserConfigurationException, SAXException, IOException {
        Element Systems = gvfrag.createElement("Systems");
        for(File currentSystem: systemDirs) {
            Element System = gvfrag.createElement("System");
            System.setAttribute("id-system", currentSystem.getName());
            System.setAttribute("system-activation", "on");

            File[] channelFiles = currentSystem.listFiles();

            for(File channelFile: Objects.requireNonNull(channelFiles)) {
                Node channel = Utils.fileToDocument(channelFile).getFirstChild();
                System.appendChild(gvfrag.adoptNode(channel));
            }
            Systems.appendChild(System);
        }
        return Systems;
    }

}
