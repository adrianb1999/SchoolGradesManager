package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.QCourse;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.CourseCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.List;

public class CourseCustomRepositoryImpl implements CourseCustomRepository {

    private final EntityManager entityManager;

    public CourseCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Course> allCoursesByTeacher(User teacher) {

        JPAQuery<Course> query = new JPAQuery<>(entityManager);
        QCourse qCourse = QCourse.course;

        return query.select(qCourse)
                .from(qCourse)
                .where(qCourse.teacher.eq(teacher))
                .fetch();
    }
}
