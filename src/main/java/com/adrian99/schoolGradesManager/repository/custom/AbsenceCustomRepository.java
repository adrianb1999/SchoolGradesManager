package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.model.Absence;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;

import java.time.LocalDate;

public interface AbsenceCustomRepository {
    boolean checkIfAbsenceExists(LocalDate date, Course course, User student);
}
