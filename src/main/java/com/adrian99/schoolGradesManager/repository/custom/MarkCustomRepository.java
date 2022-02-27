package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;

import java.time.LocalDate;

public interface MarkCustomRepository {
    boolean checkIfExamMarkExist(LocalDate markDate, Course course, User student);
}
