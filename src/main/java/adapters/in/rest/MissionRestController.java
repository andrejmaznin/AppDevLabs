package adapters.in.rest;

import application.ports.in.MissionUseCase;
import domain.models.Mission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Missions", description = "Управление магическими миссиями")
public class MissionRestController {

    private final MissionUseCase missionUseCase;

    public MissionRestController(MissionUseCase missionUseCase) {
        this.missionUseCase = missionUseCase;
    }

    @GetMapping
    @Operation(summary = "Получить список всех миссий")
    public List<Mission> getAllMissions() {
        return missionUseCase.getAllMissions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить миссию по ID")
    public Mission getMissionById(@PathVariable String id) {
        return missionUseCase.findMissionById(id)
                .orElseThrow(() -> new RuntimeException("Миссия не найдена: " + id));
    }

    @PostMapping
    @Operation(summary = "Добавить новую миссию")
    public void addMission(@RequestBody Mission mission) {
        missionUseCase.getStore().add(mission);
    }

    @DeleteMapping
    @Operation(summary = "Очистить все миссии")
    public void clearMissions() {
        missionUseCase.clearMissions();
    }
}
