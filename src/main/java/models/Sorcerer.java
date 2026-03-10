package models;

public class Sorcerer {
    private String name;
    private String rank;

    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя мага не может быть пустым");
        }
        if (rank == null || rank.trim().isEmpty()) {
            throw new IllegalArgumentException("Ранг мага не может быть пустым");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}