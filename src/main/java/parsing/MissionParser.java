package parsing;

import models.Mission;

public interface MissionParser {
    Mission parse(String data);
}
