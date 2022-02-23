package com.adrian99.schoolGradesManager.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String schoolName;

    private LocalDate firstSemesterStartDate;
    private LocalDate firstSemesterEndDate;
    private LocalDate secondSemesterStartDate;
    private LocalDate secondSemesterEndDate;

    public School() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public LocalDate getFirstSemesterStartDate() {
        return firstSemesterStartDate;
    }

    public void setFirstSemesterStartDate(LocalDate firstSemesterStartDate) {
        this.firstSemesterStartDate = firstSemesterStartDate;
    }

    public LocalDate getFirstSemesterEndDate() {
        return firstSemesterEndDate;
    }

    public void setFirstSemesterEndDate(LocalDate firstSemesterEndDate) {
        this.firstSemesterEndDate = firstSemesterEndDate;
    }

    public LocalDate getSecondSemesterStartDate() {
        return secondSemesterStartDate;
    }

    public void setSecondSemesterStartDate(LocalDate secondSemesterStartDate) {
        this.secondSemesterStartDate = secondSemesterStartDate;
    }

    public LocalDate getSecondSemesterEndDate() {
        return secondSemesterEndDate;
    }

    public void setSecondSemesterEndDate(LocalDate secondSemesterEndDate) {
        this.secondSemesterEndDate = secondSemesterEndDate;
    }
}
