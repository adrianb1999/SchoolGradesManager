package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.QCourse;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.CourseCustomRepository;
import com.querydsl.core.Tuple;
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
    public List<Map<String, Object>> allCoursesByTeacher(User teacher) {

        JPAQuery<Course> query = new JPAQuery<>(entityManager);
        QCourse qCourse = QCourse.course;

        return query.select(qCourse.id, qCourse.name, qCourse.classRoom.name)
                .from(qCourse)
                .where(qCourse.teacher.eq(teacher))
                .fetch().stream().map(tuple ->
                    new HashMap<String, Object>(){{
                        put("id",tuple.get(0, Long.class));
                        put("name", tuple.get(1, String.class));
                        put("classroomName",tuple.get(2,String.class));
                    }}
                ).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> allCoursesByStudent(User student) {

        JPAQuery<Course> query = new JPAQuery<>(entityManager);
        QCourse qCourse = QCourse.course;

        List<Map<String, Object>> collect = query.select(qCourse.id, qCourse.name, qCourse.teacher.firstName, qCourse.teacher.lastName, qCourse.teacher.id)
                .from(qCourse)
                .where(qCourse.classRoom.students.contains(student))
                .fetch()
                .stream().map(tuple ->
                        new HashMap<String, Object>() {{
                            put("courseId", tuple.get(0, Long.class));
                            put("courseName", tuple.get(1, String.class));
                            put("teacherFullName", tuple.get(2, String.class) + " " + tuple.get(3, String.class));
                        }}
                ).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<Map<String, Object>> findAllCourses() {
        JPAQuery<Course> query = new JPAQuery<>(entityManager);
        QCourse qCourse = QCourse.course;

        return query.select(qCourse.id, qCourse.name, qCourse.teacher.firstName, qCourse.teacher.lastName, qCourse.classRoom.name, qCourse.teacher.id, qCourse.classRoom.id, qCourse.exam)
                .from(qCourse)
                .fetch()
                .stream().map(tuple ->
                        new HashMap<String, Object>() {{
                            put("id", tuple.get(0, Long.class));
                            put("name", tuple.get(1, String.class));
                            put("teacherName", tuple.get(2, String.class) + " " + tuple.get(3, String.class));
                            put("classroomName", tuple.get(4, String.class));
                            put("teacherId", tuple.get(5, String.class));
                            put("classroomId", tuple.get(6, String.class));
                            put("examCourse", tuple.get(7, String.class));
                        }}).collect(Collectors.toList());
    }

}
