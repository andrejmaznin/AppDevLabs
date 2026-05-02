package adapters.out.parsing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MissionParserFactoryTest {

    @ParameterizedTest
    @CsvSource({
        "mission.json, JSON",
        "mission.xml, XML",
        "mission.txt, TXT",
        "mission.yaml, YAML",
        "mission.yml, YAML",
        "mission.BIN, BIN",
        "mission, BIN"
    })
    void testGetParserByFileName_Success(String fileName, ParserType expectedType) {
        // MissionParserFactory relies on ParserType.fromExtension
        // Let's check if the factory returns the correct parser class
        MissionParser parser = MissionParserFactory.getParserByFileName(fileName);
        assertNotNull(parser);
        
        switch (expectedType) {
            case JSON -> assertTrue(parser instanceof JsonMissionParser);
            case XML -> assertTrue(parser instanceof XmlMissionParser);
            case TXT -> assertTrue(parser instanceof TxtMissionParser);
            case YAML -> assertTrue(parser instanceof YamlMissionParser);
            case BIN -> assertTrue(parser instanceof BinMissionParser);
        }
    }

    @Test
    void testGetParserByFileName_Unsupported() {
        assertThrows(IllegalArgumentException.class, () -> 
            MissionParserFactory.getParserByFileName("mission.unknown")
        );
    }

    @Test
    void testGetParser_NullType() {
        assertThrows(IllegalArgumentException.class, () -> 
            MissionParserFactory.getParser(null)
        );
    }
}
