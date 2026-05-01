package builder;

import models.CivilianImpact;
import models.Curse;
import models.EconomicAssessment;
import models.EnemyActivity;
import models.EnvironmentConditions;
import models.OperationEvent;
import models.Sorcerer;
import models.Technique;

import java.util.ArrayList;
import java.util.List;

public class MissionData {
    public String missionId;
    public String date;
    public String location;
    public String outcome;
    public long damageCost;
    public String comment;

    public String curseName;
    public String curseThreatLevel;
    public Curse curse;

    public List<Sorcerer> sorcerers = new ArrayList<>();
    public List<Technique> techniques = new ArrayList<>();

    public EconomicAssessment economicAssessment;
    public CivilianImpact civilianImpact;
    public EnemyActivity enemyActivity;
    public EnvironmentConditions environmentConditions;
    public List<OperationEvent> operationTimeline = new ArrayList<>();
}
