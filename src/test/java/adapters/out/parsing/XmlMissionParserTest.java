package adapters.out.parsing;

import domain.models.Mission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlMissionParserTest {

    private final XmlMissionParser parser = new XmlMissionParser();

    @Test
    void testParse_ExampleMission() {
        String xml = """
            <mission>
                <missionId>M-2024-025</missionId>
                <date>2024-11-18</date>
                <location>Токио, район Синдзюку</location>
                <outcome>PARTIAL_SUCCESS</outcome>
                <curse>
                    <name>Проклятие заброшенной станции</name>
                    <threatLevel>HIGH</threatLevel>
                </curse>
                <sorcerers>
                    <sorcerer>
                        <name>Инумаки Тоге</name>
                        <rank>SEMI_GRADE_1</rank>
                    </sorcerer>
                    <sorcerer>
                        <name>Панда</name>
                        <rank>GRADE_2</rank>
                    </sorcerer>
                </sorcerers>
                <techniques>
                    <technique>
                        <name>Проклятая речь</name>
                        <type>INNATE</type>
                        <owner>Инумаки Тоге</owner>
                        <damage>430000</damage>
                    </technique>
                    <technique>
                        <name>Горилла-режим</name>
                        <type>BODY</type>
                        <owner>Панда</owner>
                        <damage>390000</damage>
                    </technique>
                </techniques>
            </mission>
            """;

        Mission mission = parser.parse(xml);

        assertNotNull(mission);
        assertEquals("M-2024-025", mission.getMissionId());
        assertEquals("Токио, район Синдзюку", mission.getLocation());
        assertEquals("PARTIAL_SUCCESS", mission.getOutcome());
        assertNotNull(mission.getCurse());
        assertEquals("Проклятие заброшенной станции", mission.getCurse().getName());
        assertEquals(2, mission.getSorcerers().size());
        assertEquals(2, mission.getTechniques().size());
    }

    @Test
    void testParse_InvalidXml() {
        String invalidXml = "<mission> <invalid> ";
        assertThrows(RuntimeException.class, () -> parser.parse(invalidXml));
    }
}
