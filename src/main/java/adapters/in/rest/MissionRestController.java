package adapters.in.rest;

import application.ports.in.MissionUseCase;
import domain.models.Mission;
import domain.reports.*;
import domain.specifications.FullTextSpecification;
import adapters.out.parsing.MissionParser;
import adapters.out.parsing.MissionParserFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Missions", description = "Управление магическими миссиями")
public class MissionRestController {
    private static final Logger logger = LoggerFactory.getLogger(MissionRestController.class);
    private final MissionUseCase missionUseCase;

    public MissionRestController(MissionUseCase missionUseCase) {
        this.missionUseCase = missionUseCase;
    }

    @GetMapping
    @Operation(summary = "Получить список миссий с фильтрацией")
    public List<Mission> getMissions(@RequestParam(required = false) String search) {
        logger.info("REST: Запрос списка миссий. Поиск: {}", (search != null ? search : "отсутствует"));
        if (search == null || search.isEmpty()) {
            return missionUseCase.getAllMissions();
        }
        return missionUseCase.getStore().find(new FullTextSpecification(search));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить миссию по ID")
    public Mission getMissionById(@PathVariable String id) {
        logger.info("REST: Запрос миссии по ID: {}", id);
        return missionUseCase.findMissionById(id)
                .orElseThrow(() -> {
                    logger.warn("REST: Миссия с ID {} не найдена", id);
                    return new RuntimeException("Миссия не найдена: " + id);
                });
    }

    @PostMapping
    @Operation(summary = "Добавить новую миссию")
    public void addMission(@RequestBody Mission mission) {
        logger.info("REST: Добавление новой миссии через JSON");
        missionUseCase.getStore().add(mission);
    }

    @PostMapping("/import")
    @Operation(summary = "Импортировать миссию из файла")
    public void importMission(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        logger.info("REST: Импорт миссии из файла: {}", fileName);
        try {
            String content = new String(file.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            MissionParser parser = MissionParserFactory.getParserByFileName(fileName);
            Mission mission = parser.parse(content);
            if (mission != null) {
                missionUseCase.getStore().add(mission);
                logger.info("REST: Файл {} успешно импортирован", fileName);
            }
        } catch (Exception e) {
            logger.error("REST: Ошибка при импорте файла {}: {}", fileName, e.getMessage());
            throw new RuntimeException("Ошибка при импорте: " + e.getMessage(), e);
        }
    }

    @DeleteMapping
    @Operation(summary = "Очистить все миссии")
    public void clearMissions() {
        logger.warn("REST: Запрос на удаление всех миссий");
        missionUseCase.clearMissions();
    }

    @GetMapping("/{id}/report/basic")
    @Operation(summary = "Получить базовый отчет по миссии")
    public BasicMissionReport getBasicReport(@PathVariable String id) {
        logger.info("REST: Генерация базового отчета для миссии {}", id);
        Mission mission = getMissionById(id);
        ReportNavigator navigator = new ReportNavigator(new BasicReportStrategy());
        return (BasicMissionReport) navigator.execute(mission);
    }

    @GetMapping("/{id}/report/tree")
    @Operation(summary = "Получить древовидный отчет по миссии")
    public TreeReport getTreeReport(@PathVariable String id) {
        logger.info("REST: Генерация древовидного отчета для миссии {}", id);
        Mission mission = getMissionById(id);
        ReportNavigator navigator = new ReportNavigator(new TreeReportStrategy());
        return (TreeReport) navigator.execute(mission);
    }
}
