package adapters.out.parsing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class MissionParserFactory {
    private static final Logger logger = LoggerFactory.getLogger(MissionParserFactory.class);

    private static final Map<ParserType, Supplier<MissionParser>> registry = new EnumMap<>(ParserType.class);

    static {
        logger.debug("Регистрация стандартных парсеров...");
        register(ParserType.JSON, JsonMissionParser::new);
        register(ParserType.XML, XmlMissionParser::new);
        register(ParserType.TXT, TxtMissionParser::new);
        register(ParserType.YAML, YamlMissionParser::new);
        register(ParserType.BIN, BinMissionParser::new);
    }

    public static void register(ParserType type, Supplier<MissionParser> supplier) {
        if (type == null || supplier == null) {
            logger.error("Попытка регистрации парсера с null-параметрами");
            throw new IllegalArgumentException();
        }
        logger.trace("Зарегистрирован парсер для типа: {}", type);
        registry.put(type, supplier);
    }

    public static MissionParser getParserByFileName(String fileName) {
        logger.debug("Выбор парсера для файла: {}", fileName);
        String ext = extractExtension(fileName);
        ParserType type = ParserType.fromExtension(ext);
        if (type == null) {
            logger.warn("Не удалось определить тип парсера для файла: {}", fileName);
            throw new IllegalArgumentException("Неподдерживаемый формат файла: " + fileName);
        }
        return getParser(type);
    }

    public static MissionParser getParser(ParserType type) {
        if (type == null) {
            logger.error("Запрос парсера для null-типа");
            throw new IllegalArgumentException("Тип парсера не может быть null");
        }
        Supplier<MissionParser> supplier = registry.get(type);
        if (supplier == null) {
            logger.error("Парсер для типа {} не зарегистрирован", type);
            throw new IllegalArgumentException("Неизвестный тип парсера: " + type);
        }
        logger.trace("Создан экземпляр парсера для типа: {}", type);
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