package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;

import java.util.List;
import java.util.Map;

public interface UserCustomRepository {
    User findByUsername(String username);
    List<Map<String, Object>> findAllTeachers();
    List<Map<String, Object>> findAllStudents();
    List<Map<String, Object>> findStudentsByCourse(Course course);
    List<Map<String, Object>> findStudentsByClassroom(Classroom classroom);
    List<Map<String, Object>> findMarksByStudentAndCourse(User student, Course course);
    List<Map<String, Object>> findAbsencesByStudentAndCourse(User student, Course course);
    List<Map<String, Object>> findMarksByStudent(User student);

    User findByEmail(String email);
}
