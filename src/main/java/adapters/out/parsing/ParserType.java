package adapters.out.parsing;

import java.util.Locale;

public enum ParserType {
    JSON("json"), XML("xml"), TXT("txt"), YAML("yaml", "yml"), BIN("", "bin");

    private final String[] extensions;

    ParserType(String... extensions) {
        this.extensions = extensions;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public static ParserType fromExtension(String ext) {
        if (ext == null) return null;

        String e = ext.toLowerCase(Locale.ROOT);
        for (ParserType p : values()) {
            for (String pExt : p.extensions) {
                if (pExt.equals(e)) return p;
            }
        }
        return null;
    }
}