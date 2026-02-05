package app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "priorities")
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int level;


    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, length = 32)
    private String color;


    public Priority() {}

    public Priority(String name, int level, String description, String color) {
        this.name = name;
        this.level = level;
        this.description = description;
        this.color = color;
    }

    public Priority(String name, int level, String description) {
        this(name, level, description, "#000000"); // default color
    }

    public int getId() {
        return id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
