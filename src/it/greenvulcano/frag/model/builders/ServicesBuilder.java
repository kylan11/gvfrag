package it.greenvulcano.frag.model.builders;

import it.greenvulcano.frag.Main;
import it.greenvulcano.frag.model.files.File;
import it.greenvulcano.frag.model.fs.PathResolver;
import it.greenvulcano.frag.util.Utils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Objects;

public class ServicesBuilder extends Builder {

    final String OUTPUT_BASE_PATH = PathResolver.get(Main.BASE_PATH, "GVServices");
    final String BASE_XPATH = "/GVCore/GVServices";

    @Override
    public void build(Document doc) throws XPathExpressionException, ParserConfigurationException,
            TransformerFactoryConfigurationError, TransformerException, IOException {

        Node firstNode = (Node) xpath.compile(BASE_XPATH).evaluate(doc, XPathConstants.NODE);
        NodeList services = (NodeList) xpath.compile("Services/Service").evaluate(firstNode,
                XPathConstants.NODESET);
        for (Node service : Utils.iterable(services)) {
            String currentGroup = ((Element) service).getAttribute("group-name");
            String currentServiceId = ((Element) service).getAttribute("id-service");
            NodeList operations = (NodeList) xpath.compile("Operation").evaluate(service, XPathConstants.NODESET);
            NodeList conditions = (NodeList) xpath.compile("Conditions").evaluate(service, XPathConstants.NODESET);

            for(Node condition: Utils.iterable(conditions)) {
                outputFiles.add(new File(Utils.nodeToDocument(condition),
                        PathResolver.get(OUTPUT_BASE_PATH, currentGroup, currentServiceId), "Conditions.xml"));
            }

            for (Node operation : Utils.iterable(operations)) {
                String fileName = ((Element) operation).getAttribute("name") + ".xml";
                outputFiles.add(new File(Utils.nodeToDocument(operation),
                        PathResolver.get(OUTPUT_BASE_PATH, currentGroup, currentServiceId), fileName));
            }
        }
        removeFromSkeleton(firstNode);
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        java.io.File rootDir = new java.io.File(PathResolver.get(path, "GVServices"));

        // first of all, creates root node.
        Element GVServices = gvfrag.createElement("GVServices");
        GVServices.setAttribute("name", "SERVICES");
        GVServices.setAttribute("type", "module");

        java.io.File[] groupDirs = rootDir.listFiles();
        if (groupDirs != null && groupDirs.length != 0) {

            // <Groups> is created here and appended to GVServices
            GVServices.appendChild(createGroups(gvfrag, groupDirs));

            // <Services> is a sibling of <Groups>. Created here.
            GVServices.appendChild(createServices(gvfrag, groupDirs));
        }
        gvfrag.getFirstChild().appendChild(GVServices);
    }

    public Element createGroups(Document gvfrag, java.io.File[] groupDirs) {
        Element groups = gvfrag.createElement("Groups");
        Element description = gvfrag.createElement("Description");
        Text descriptionText = gvfrag.createTextNode("This section contains all the service groups.");
        description.appendChild(descriptionText);
        groups.appendChild(description);
        for (java.io.File dir : groupDirs) {
            Element currentGroup = gvfrag.createElement("Group");
            currentGroup.setAttribute("group-activation", "on");
            currentGroup.setAttribute("id-group", dir.getName());
            groups.appendChild(currentGroup);
        }
        return groups;
    }

    public Element createServices(Document gvfrag, java.io.File[] groupDirs) throws ParserConfigurationException, SAXException, IOException {
        Element Services = gvfrag.createElement("Services");

        // loops over each group Dir to find Service dirs.
        for (java.io.File currentGroup : groupDirs) {
            java.io.File[] serviceDirs = currentGroup.listFiles();

            // loops over each service folder to build Service each with its operations
            for (java.io.File currentService : Objects.requireNonNull(serviceDirs)) {
                Element Service = gvfrag.createElement("Service");
                Service.setAttribute("group-name", currentGroup.getName());
                Service.setAttribute("id-service", currentService.getName());
                Service.setAttribute("service-activation", "on");
                Service.setAttribute("statistics", "off");
                java.io.File[] operations = currentService.listFiles();
                for (java.io.File currentOperationFile : Objects.requireNonNull(operations)) {
                    // WARNING: This also appends Conditions.
                    Node operation = Utils.fileToDocument(currentOperationFile).getFirstChild();
                    Service.appendChild(gvfrag.adoptNode(operation));
                }
                Services.appendChild(Service);
            }
        }
        return Services;
    }

}