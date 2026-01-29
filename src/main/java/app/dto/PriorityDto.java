package app.dto;

public class PriorityDto {
    private int id;
    private String name;
    private int level;
    private String description;

    public PriorityDto() {}

    public PriorityDto(int id, String name, int level, String description) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}