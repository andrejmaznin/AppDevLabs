package parsing;

import java.util.Locale;

public enum ParserType {
    JSON("json"), XML("xml"), TXT("txt"), YAML("yaml"), BIN("");

    private final String extension;

    ParserType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static ParserType fromExtension(String ext) {
        if (ext == null || ext.isEmpty()) return null;
        String e = ext.toLowerCase(Locale.ROOT);
        for (ParserType p : values()) {
            if (p.getExtension().equals(e)) return p;
        }
        return null;
    }
}
