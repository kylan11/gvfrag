package it.greenvulcano.frag.model.fs;

public class PathResolver {

    public static String separator = java.nio.file.FileSystems.getDefault().getSeparator();

    public static String get(String... path) {
        return String.join(separator, path);
    }

    public static String prependGet(String... path) {
        return separator + String.join(separator, path);
    }

    public static String appendGet(String... path) {
        return String.join(separator, path) + separator;
    }
}
