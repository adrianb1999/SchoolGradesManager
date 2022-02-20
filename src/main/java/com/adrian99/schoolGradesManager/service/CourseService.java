package com.adrian99.schoolGradesManager.service;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.repository.custom.CourseCustomRepository;

public interface CourseService extends CrudService<Course, Long>, CourseCustomRepository {
}
