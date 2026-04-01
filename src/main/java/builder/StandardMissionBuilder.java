package builder;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import models.Curse;
import models.Mission;
import models.Sorcerer;
import models.Technique;

import java.util.ArrayList;
import java.util.List;

@JsonPOJOBuilder(withPrefix = "set")
public class StandardMissionBuilder implements MissionBuilder {
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse;
    private String comment;
    private final List<Sorcerer> sorcerers = new ArrayList<>();
    private final List<Technique> techniques = new ArrayList<>();

    @Override
    public MissionBuilder setMissionId(String id) { this.missionId = id; return this; }
    
    @Override
    public MissionBuilder setDate(String date) { this.date = date; return this; }
    
    @Override
    public MissionBuilder setLocation(String location) { this.location = location; return this; }
    
    @Override
    public MissionBuilder setOutcome(String outcome) { this.outcome = outcome; return this; }
    
    @Override
    public MissionBuilder setDamageCost(long cost) { this.damageCost = cost; return this; }
    
    @Override
    public MissionBuilder setComment(String comment) { this.comment = comment; return this; }

    @Override
    public MissionBuilder setCurse(Curse curse) { this.curse = curse; return this; }

    @Override
    @JacksonXmlElementWrapper(localName = "sorcerers")
    @JacksonXmlProperty(localName = "sorcerer")
    public MissionBuilder setSorcerers(List<Sorcerer> sorcerers) {
        if (sorcerers != null) {
            this.sorcerers.addAll(sorcerers);
        }
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "techniques")
    @JacksonXmlProperty(localName = "technique")
    public MissionBuilder setTechniques(List<Technique> techniques) {
        if (techniques != null) {
            this.techniques.addAll(techniques);
        }
        return this;
    }

    @Override
    public MissionBuilder setCurseDetails(String name, String threatLevel) {
        if (this.curse == null) this.curse = new Curse();
        if (name != null) this.curse.setName(name);
        if (threatLevel != null) this.curse.setThreatLevel(threatLevel);
        return this;
    }

    @Override
    public MissionBuilder addSorcerer(Sorcerer sorcerer) {
        this.sorcerers.add(sorcerer);
        return this;
    }

    @Override
    public MissionBuilder addTechnique(Technique technique) {
        this.techniques.add(technique);
        return this;
    }

    @Override
    public Mission build() {
        Mission mission = new Mission();
        mission.setMissionId(this.missionId);
        mission.setDate(this.date);
        mission.setLocation(this.location);
        mission.setOutcome(this.outcome);
        mission.setDamageCost(this.damageCost);
        mission.setComment(this.comment);
        mission.setCurse(this.curse);
        mission.setSorcerers(this.sorcerers);
        mission.setTechniques(this.techniques);

        mission.validate();
        
        return mission;
    }
}