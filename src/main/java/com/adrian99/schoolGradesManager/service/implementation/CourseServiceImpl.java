package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.CourseRepository;
import com.adrian99.schoolGradesManager.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Iterable<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(Long aLong) {
        return courseRepository.findById(aLong).orElse(null);
    }

    @Override
    public Course save(Course object) {
        return courseRepository.save(object);
    }

    @Override
    public <S extends Course> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
    }

    //Custom queries

    @Override
    public List<Course> allCoursesByTeacher(User teacher) {
        return courseRepository.allCoursesByTeacher(teacher);
    }

    @Override
    public List<Map<String, Object>> allCoursesByStudent(User student) {
        return courseRepository.allCoursesByStudent(student);
    }
}
