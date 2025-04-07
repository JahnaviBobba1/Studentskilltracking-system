package com.example.ssts.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column
    private String offeredBy;

    @Column(nullable = false)
    private String courseUrl;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseSubscription> subscriptions = new HashSet<>();

    // Constructors, Getters, Setters
    public Course() {}

    public Course(String title, String description, String offeredBy, String courseUrl) {
        this.title = title;
        this.description = description;
        this.offeredBy = offeredBy;
        this.courseUrl = courseUrl;
    }

    // Getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getOfferedBy() { return offeredBy; }
    public void setOfferedBy(String offeredBy) { this.offeredBy = offeredBy; }
    public String getCourseUrl() { return courseUrl; }
    public void setCourseUrl(String courseUrl) { this.courseUrl = courseUrl; }
    public Set<CourseSubscription> getSubscriptions() { return subscriptions; }
    public void setSubscriptions(Set<CourseSubscription> subscriptions) { this.subscriptions = subscriptions; }
}