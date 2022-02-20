package com.adrian99.schoolGradesManager.repository;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.repository.custom.CourseCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long>, CourseCustomRepository {
}
