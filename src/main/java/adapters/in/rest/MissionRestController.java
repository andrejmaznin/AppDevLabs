package adapters.in.rest;

import application.ports.in.MissionUseCase;
import domain.models.Mission;
import domain.reports.*;
import domain.specifications.FullTextSpecification;
import adapters.out.parsing.MissionParser;
import adapters.out.parsing.MissionParserFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Operation(summary = "Получить список миссий с фильтрацией")
    public List<Mission> getMissions(@RequestParam(required = false) String search) {
        if (search == null || search.isEmpty()) {
            return missionUseCase.getAllMissions();
        }
        return missionUseCase.getStore().find(new FullTextSpecification(search));
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

    @PostMapping("/import")
    @Operation(summary = "Импортировать миссию из файла")
    public void importMission(@RequestParam("file") MultipartFile file) {
        try {
            String content = new String(file.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            String fileName = file.getOriginalFilename();
            MissionParser parser = MissionParserFactory.getParserByFileName(fileName);
            Mission mission = parser.parse(content);
            if (mission != null) {
                missionUseCase.getStore().add(mission);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте: " + e.getMessage(), e);
        }
    }

    @DeleteMapping
    @Operation(summary = "Очистить все миссии")
    public void clearMissions() {
        missionUseCase.clearMissions();
    }

    @GetMapping("/{id}/report/basic")
    @Operation(summary = "Получить базовый отчет по миссии")
    public BasicMissionReport getBasicReport(@PathVariable String id) {
        Mission mission = getMissionById(id);
        ReportNavigator navigator = new ReportNavigator(new BasicReportStrategy());
        return (BasicMissionReport) navigator.execute(mission);
    }

    @GetMapping("/{id}/report/tree")
    @Operation(summary = "Получить древовидный отчет по миссии")
    public TreeReport getTreeReport(@PathVariable String id) {
        Mission mission = getMissionById(id);
        ReportNavigator navigator = new ReportNavigator(new TreeReportStrategy());
        return (TreeReport) navigator.execute(mission);
    }
}
