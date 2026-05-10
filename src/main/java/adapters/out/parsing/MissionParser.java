package adapters.out.parsing;

import domain.models.Mission;

public interface MissionParser {
    Mission parse(String data);
}
