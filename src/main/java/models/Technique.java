package models;

public class Technique {
    private String name;
    private String type;
    private String owner;
    private int damage;

    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название техники не может быть пустым");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Тип техники не может быть пустым");
        }
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Владелец техники не может быть пустым");
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}