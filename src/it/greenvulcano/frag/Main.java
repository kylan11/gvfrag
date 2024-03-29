package it.greenvulcano.frag;

import it.greenvulcano.frag.model.builders.*;
import it.greenvulcano.frag.model.files.GVCore;
import it.greenvulcano.frag.model.files.GVFrag;
import it.greenvulcano.frag.util.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static final String CURRENT_VERSION = "0.1.3";
    public static final String GAIA_VERSION = "4.1.0";
    public static String BASE_PATH = "output";

    public static void main(String[] args) {
        try {
            // if present, overrides default base path
            BASE_PATH = Utils.getBasePath(args);
	    System.out.println("Test michele");

            switch (args[0].toLowerCase()) {
                case "-m":
                    makeCore(args[1]);
                    break;
                case "-s":
                    splitCore(args[1]);
                    break;
                case "-h":
                case "--help":
                    Utils.help();
                    break;
                default:
                    Utils.invalidArgs();
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            Utils.invalidArgs();
        } catch (IOException e) {
            Utils.fileNotFound();
        } catch (Exception e) {
            Utils.genericError();
        }
    }

    public static void splitCore(String pathToCore) throws SAXException, IOException, ParserConfigurationException,
            XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
        Document gvCore = GVCore.read(pathToCore);
        PolicyBuilder policy = new PolicyBuilder();
        ServicesBuilder services = new ServicesBuilder();
        SystemsBuilder systems = new SystemsBuilder();
        AdaptersBuilder adapters = new AdaptersBuilder();

        // generates docs and directory trees from GVCore.xml root document
        Builder.massBuild(gvCore, policy, services, systems, adapters);

        // saves in-memory built files to disk
        Builder.saveFiles(policy, services, systems, adapters);

        // builds GVFrag.xml skeleton
        GVFrag.create(gvCore);

        System.out.println("All done!");
    }

    public static void makeCore(String pathToFrag) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        Document gvFrag = GVFrag.read(pathToFrag);
        final String basePath = Paths.get(pathToFrag).getParent().toString();

        PolicyBuilder policy = new PolicyBuilder();
        ServicesBuilder services = new ServicesBuilder();
        SystemsBuilder systems = new SystemsBuilder();
        AdaptersBuilder adapters = new AdaptersBuilder();

        // rebuilds GVCore.xml from skeleton and file/dir structure
        Builder.massRemake(gvFrag, basePath, policy, services, systems, adapters);

        // writes in-memory GVCore.xml to output
        GVCore.create(gvFrag);

        System.out.println("All done!");
    }
}
