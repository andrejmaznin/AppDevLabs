package parsing;

public class MissionParserFactory {

    public enum ParserType {
        JSON, XML, TXT
    }

    public static MissionParser getParser(ParserType type) {
        return switch (type) {
            case JSON -> new JsonMissionParser();
            case XML -> new XmlMissionParser();
            case TXT -> new TxtMissionParser();
        };
    }


    public static MissionParser getParserByFileName(String fileName) {
        if (fileName.endsWith(".json")) return getParser(ParserType.JSON);
        if (fileName.endsWith(".xml")) return getParser(ParserType.XML);
        if (fileName.endsWith(".txt")) return getParser(ParserType.TXT);
        throw new IllegalArgumentException("Неподдерживаемый формат файла: " + fileName);
    }
}