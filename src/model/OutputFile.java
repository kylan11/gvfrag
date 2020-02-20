package model;

import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

// Utility wrapper for File type. A file has a name, a path, and a string content.

public class OutputFile {

    private int INDENT_FACTOR = 4;

    // string contents of file
    private String content = null;

    // file name
    private String name = null;

    // path to file
    private Path path = null;

    public OutputFile() {}

    public OutputFile(java.io.File file) throws IOException {
        if(file.exists() && file.canRead()) {
            path = file.toPath();
            name = file.getName();
            content = new String(Files.readAllBytes(path));
        }
        else throw new IOException();
    }

    public OutputFile(String content, String path, String name) {
        this.content = content;
        this.name = name;
        setPath(path);
    }

    public OutputFile(String content, Path path, String name) {
        this.content = content;
        this.path = path;
        this.name = name;
    }

    public OutputFile(Document doc, String path, String name) throws TransformerFactoryConfigurationError, TransformerException, IOException {
        this.content = docToString(doc, INDENT_FACTOR);
        this.name = name;
        setPath(path);
    }

    public OutputFile(Document doc, Path path, String name) throws TransformerFactoryConfigurationError, TransformerException, IOException {
        this.content = docToString(doc, INDENT_FACTOR);
        this.path = path;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Path getPath() {
        return path;
    }

    public String getPathString() {
        return path.toString();
    }

    public void setPath(String path) {
        this.path = Paths.get(path);
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public static String docToString(Document doc, int indent) throws TransformerFactoryConfigurationError, TransformerException, IOException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(indent));
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

    public String getFilePath() {
        return getPathString() + "/" + name;
    }

    // Creates the file in given path, with given name, and with its string content.
    public void create() throws IOException {
        if(path == null || name == null || content == null)
            throw new InvalidParameterException("Null file path, name or content.");
        Files.createDirectories(path);
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(getFilePath()), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }
}
