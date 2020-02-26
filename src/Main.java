import model.OutputFile;
import model.builders.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import util.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static String OUTPUT_BASEPATH = "output";

    public static void main(String[] args) {
        try {
            if (args[0].equalsIgnoreCase("-M")) {
                makeCore(args[1]);
            }
            else if (args[0].equalsIgnoreCase("-S")) {
                splitCore(args[1]);
            }
            else if (args[0].equalsIgnoreCase("--help")) {
                System.out.println("Usage: java -jar gvfrag.jar <-S|-M> /path/to/<GVCore.xml|GVFrag.xml>\n"
                        + "[-S]: Split GVCore into folder components and produces GVFrag.xml\n"
                        + "[-M]: Produce GVCore.xml from GVFrag.xml and attached folder components.\n"
                        + "[--help]: Displays this message.");
            }
            else {
                throw new ArrayIndexOutOfBoundsException();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid arguments. Usage: java -jar gvfrag.jar <-S|-M> "
                    + "/path/to/<GVCore.xml|GVFrag.xml>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void splitCore(String path) throws SAXException, IOException, ParserConfigurationException,
            XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
        Document rootDoc = Utils.readGVCore(path);

        PolicyBuilder policy = new PolicyBuilder();
        ServicesBuilder services = new ServicesBuilder();
        SystemsBuilder systems = new SystemsBuilder();
        AdaptersBuilder adapters = new AdaptersBuilder();

        policy.build(rootDoc);
        services.build(rootDoc);
        systems.build(rootDoc);
        adapters.build(rootDoc);

        // saves in-memory built files to disk
        Builder.saveFiles(policy, services, systems, adapters);

        // builds GVFrag.xml skeleton
        OutputFile gvfrag = new OutputFile(rootDoc, OUTPUT_BASEPATH, "GVFrag.xml");
        gvfrag.create();
    }

    public static void makeCore(String pathToFrag) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        Document gvfrag = Utils.fileToDocument(new File(pathToFrag));
        final String basePath = Utils.absoluteToPath(pathToFrag);

        new ServicesBuilder().remake(gvfrag, basePath);
        new SystemsBuilder().remake(gvfrag, basePath);
        new AdaptersBuilder().remake(gvfrag, basePath);
        new PolicyBuilder().remake(gvfrag, basePath);

        // rebuilds GVCore.xml from fragments
        OutputFile gvcore = new OutputFile(Utils.writeGVCore(gvfrag), OUTPUT_BASEPATH, "GVCore.xml");
        gvcore.create();
    }
}
