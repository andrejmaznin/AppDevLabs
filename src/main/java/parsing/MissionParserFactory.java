package parsing;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class MissionParserFactory {

    private static final Map<ParserType, Supplier<MissionParser>> registry = new EnumMap<>(ParserType.class);

    static {
        registry.put(ParserType.JSON, JsonMissionParser::new);
        registry.put(ParserType.XML, XmlMissionParser::new);
        registry.put(ParserType.TXT, TxtMissionParser::new);
    }

    public static void register(ParserType type, Supplier<MissionParser> supplier) {
        if (type == null || supplier == null) {
            throw new IllegalArgumentException();
        }
        registry.put(type, supplier);
    }

    public static MissionParser getParserByFileName(String fileName) {
        String ext = extractExtension(fileName);
        ParserType type = ParserType.fromExtension(ext);
        if (type == null) {
            throw new IllegalArgumentException("Неподдерживаемый формат файла: " + fileName);
        }
        return getParser(type);
    }

    public static MissionParser getParser(ParserType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип парсера не может быть null");
        }
        Supplier<MissionParser> supplier = registry.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Неизвестный тип парсера: " + type);
        }
        return supplier.get();
    }

    private static String extractExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int i = fileName.lastIndexOf('.');
        return (i >= 0) ? fileName.substring(i + 1).toLowerCase() : "";
    }
}