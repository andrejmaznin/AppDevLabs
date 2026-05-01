package io;

import models.Mission;

import java.util.List;

public interface MissionSource {
    List<Mission> loadMissions() throws Exception;
}
