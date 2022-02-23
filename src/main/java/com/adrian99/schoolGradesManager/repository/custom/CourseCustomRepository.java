package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;

import java.util.List;
import java.util.Map;

public interface CourseCustomRepository {
    List<Course> allCoursesByTeacher(User teacher);
    List<Map<String, Object>> allCoursesByStudent(User student);
}
