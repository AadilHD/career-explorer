package com.rbc.explorer.careerexplorer;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;

    @Lob
    private String description; // Used for AI scoring and job display

    @ElementCollection
    private List<String> skills;

    // Default constructor (required by JPA)
    public Job() {}

    public Job(String title, String location, String[] skills, String description) {
        this.title = title;
        this.location = location;
        this.skills = Arrays.asList(skills);
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}