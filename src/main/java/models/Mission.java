package models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDate;
import java.util.List;


public class Mission {
    private String missionId;
    private LocalDate date;
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
        if (missionId == null || missionId.trim().isEmpty())
            throw new IllegalArgumentException("ID миссии не может быть пустым");
        if (date == null)
            throw new IllegalArgumentException("Дата не может быть пустой");
        if (location == null || location.trim().isEmpty())
            throw new IllegalArgumentException("Локация не может быть пустой");
        if (outcome == null || outcome.trim().isEmpty())
            throw new IllegalArgumentException("Результат миссии не может быть пустым");

        if (damageCost < 0)
            throw new IllegalArgumentException("Ущерб не может быть отрицательным");

        if (curse == null) {
            throw new IllegalArgumentException("Данные о проклятии отсутствуют");
        }
        curse.validate();

        if (sorcerers == null || sorcerers.isEmpty()) {
            throw new IllegalArgumentException("В миссии должен участвовать хотя бы один маг");
        }
        for (Sorcerer s : sorcerers) {
            s.validate();
        }

        if (techniques == null || techniques.isEmpty()) {
            throw new IllegalArgumentException("Список примененных техник не может быть пустым");
        }
        for (Technique t : techniques) {
            t.validate();
        }
        /*
        long sumOfTechniques = 0;
        if (techniques != null) {
            for (int i = 0; i < techniques.size(); i++) {
                Technique t = techniques.get(i);
                sumOfTechniques += t.getDamage();
            }
        }

        if (this.damageCost != sumOfTechniques) {
            throw new IllegalArgumentException("Ошибка: Общий урон (" + this.damageCost +
                ") не равен сумме урона техник (" + sumOfTechniques + ")");
        }
         */
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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