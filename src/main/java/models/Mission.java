package models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

import validation.ValChainFactory;


public class Mission {
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse;
    private String comment;

    @JacksonXmlElementWrapper(localName = "sorcerers")
    @JacksonXmlProperty(localName = "sorcerer")
    private List<Sorcerer> sorcerers;

    @JacksonXmlElementWrapper(localName = "techniques")
    @JacksonXmlProperty(localName = "technique")
    private List<Technique> techniques;

    public void validate() throws IllegalArgumentException {
        ValChainFactory.createDefaultChain().validate(this);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public long getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(long damageCost) {
        this.damageCost = damageCost;
    }

    public Curse getCurse() {
        return curse;
    }

    public void setCurse(Curse curse) {
        this.curse = curse;
    }

    public List<Sorcerer> getSorcerers() {
        return sorcerers;
    }

    public void setSorcerers(List<Sorcerer> sorcerers) {
        this.sorcerers = sorcerers;
    }

    public List<Technique> getTechniques() {
        return techniques;
    }

    public void setTechniques(List<Technique> techniques) {
        this.techniques = techniques;
    }
}