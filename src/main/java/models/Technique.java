package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Technique {
    private String name;
    private String type;
    
    @JsonIgnore
    private Sorcerer owner;
    
    @JsonProperty("owner")
    private String ownerName;
    
    private int damage;

    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название техники не может быть пустым");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Тип техники не может быть пустым");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Владелец техники не может быть пустым или не найден");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным числом");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Sorcerer getOwner() {
        return owner;
    }

    public void setOwner(Sorcerer owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}