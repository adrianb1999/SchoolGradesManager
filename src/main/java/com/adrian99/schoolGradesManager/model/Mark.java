package com.adrian99.schoolGradesManager.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User student;

    @OneToOne
    private Course course;

    private Integer value;
    private LocalDate date;
    private String type;

    public Mark() {
    }

    public Mark(User student, Course course, Integer value, LocalDate date, String type) {
        this.student = student;
        this.course = course;
        this.value = value;
        this.date = date;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
