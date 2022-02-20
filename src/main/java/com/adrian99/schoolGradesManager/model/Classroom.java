package com.adrian99.schoolGradesManager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User classMaster;

    @OneToMany
    private Set<User> students = new HashSet<>();

    private String name;

    public Classroom(User classMaster, Set<User> students, String name) {
        this.classMaster = classMaster;
        this.students = students;
        this.name = name;
    }

    public Classroom() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getClassMaster() {
        return classMaster;
    }

    public void setClassMaster(User teacher) {
        this.classMaster = teacher;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
