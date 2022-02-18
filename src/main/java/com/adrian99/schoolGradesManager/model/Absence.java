package com.adrian99.schoolGradesManager.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Absences")
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User student;

    @OneToOne
    private Course course;

    private LocalDate date;

    private Boolean justified;

    public Absence() {
    }

    public Absence(User student, Course course, LocalDate date, Boolean justified) {
        this.student = student;
        this.course = course;
        this.date = date;
        this.justified = justified;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate localDate) {
        this.date = localDate;
    }

    public Boolean getJustified() {
        return justified;
    }

    public void setJustified(Boolean justified) {
        this.justified = justified;
    }
}

