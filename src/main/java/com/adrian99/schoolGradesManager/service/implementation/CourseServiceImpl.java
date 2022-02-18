package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.repository.CourseRepository;
import com.adrian99.schoolGradesManager.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
}
