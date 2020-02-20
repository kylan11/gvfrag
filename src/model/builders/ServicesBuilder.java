package model.builders;

import model.OutputFile;
import org.w3c.dom.*;
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

public class ServicesBuilder extends Builder {

    static {
        OUTPUT_BASE_PATH = "output/GVServices";
        BASE_XPATH = "/GVCore/GVServices";
    }

    @Override
    public List<OutputFile> build(Document doc) throws XPathExpressionException, ParserConfigurationException,
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
                outputFiles.add(new OutputFile(Utils.nodeToDocument(condition),
                        Utils.pathBuilder(OUTPUT_BASE_PATH, currentGroup, currentServiceId), "Conditions.xml"));
            }

            for (Node operation : Utils.iterable(operations)) {
                String fileName = ((Element) operation).getAttribute("name") + ".xml";
                outputFiles.add(new OutputFile(Utils.nodeToDocument(operation),
                        Utils.pathBuilder(OUTPUT_BASE_PATH, currentGroup, currentServiceId), fileName));
            }
        }
        removeFromSkeleton(firstNode);
        return outputFiles;
    }

    @Override
    public void remake(Document gvfrag, String path) throws ParserConfigurationException, SAXException, IOException {
        File rootDir = new File(path + "/GVServices");

        // first of all, creates root node.
        Element GVServices = gvfrag.createElement("GVServices");
        GVServices.setAttribute("name", "SERVICES");
        GVServices.setAttribute("type", "module");

        File[] groupDirs = rootDir.listFiles();
        if (groupDirs != null && groupDirs.length != 0) {

            // <Groups> is created here and appended to GVServices
            GVServices.appendChild(createGroups(gvfrag, groupDirs));

            // <Services> is a sibling of <Groups>. Created here.
            GVServices.appendChild(createServices(gvfrag, groupDirs));
        }
        gvfrag.getFirstChild().appendChild(GVServices);
    }

    public Element createGroups(Document gvfrag, File[] groupDirs) {
        Element groups = gvfrag.createElement("Groups");
        Element description = gvfrag.createElement("Description");
        Text descriptionText = gvfrag.createTextNode("This section contains all the service groups.");
        description.appendChild(descriptionText);
        groups.appendChild(description);
        for (File dir : groupDirs) {
            Element currentGroup = gvfrag.createElement("Group");
            currentGroup.setAttribute("group-activation", "on");
            currentGroup.setAttribute("id-group", dir.getName());
            groups.appendChild(currentGroup);
        }
        return groups;
    }

    public Element createServices(Document gvfrag, File[] groupDirs) throws ParserConfigurationException, SAXException, IOException {
        Element Services = gvfrag.createElement("Services");

        // loops over each group Dir to find Service dirs.
        for (File currentGroup : groupDirs) {
            File[] serviceDirs = currentGroup.listFiles();

            // loops over each service folder to build Service each with its operations
            for (File currentService : Objects.requireNonNull(serviceDirs)) {
                Element Service = gvfrag.createElement("Service");
                Service.setAttribute("group-name", currentGroup.getName());
                Service.setAttribute("id-service", currentService.getName());
                Service.setAttribute("service-activation", "on");
                Service.setAttribute("statistics", "off");
                File[] operations = currentService.listFiles();
                for (File currentOperationFile : Objects.requireNonNull(operations)) {
                    // WARNING: This also takes Conditions.
                    Node operation = Utils.fileToDocument(currentOperationFile).getFirstChild();
                    Service.appendChild(gvfrag.adoptNode(operation));
                }
                Services.appendChild(Service);
            }
        }
        return Services;
    }

}