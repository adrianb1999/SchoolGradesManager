package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.QCourse;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.CourseCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<Map<String, Object>> allCoursesByStudent(User student) {

        JPAQuery<Course> query = new JPAQuery<>(entityManager);
        QCourse qCourse = QCourse.course;

        List<Map<String, Object>> collect = query.select(qCourse.id, qCourse.name, qCourse.teacher.firstName, qCourse.teacher.lastName)
                .from(qCourse)
                .where(qCourse.classRoom.students.contains(student))
                .fetch()
                .stream().map(tuple ->
                        new HashMap<String, Object>() {{
                            put("courseId", tuple.get(0, Long.class));
                            put("courseName",tuple.get(1,String.class));
                            put("teacherFullName",tuple.get(2,String.class)+" " + tuple.get(3,String.class));
                        }}
                ).collect(Collectors.toList());

        return collect;
    }
}
