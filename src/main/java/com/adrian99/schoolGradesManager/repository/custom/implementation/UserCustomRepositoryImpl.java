package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.QClassroom;
import com.adrian99.schoolGradesManager.model.QCourse;
import com.adrian99.schoolGradesManager.model.QUser;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.UserCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager entityManager;

    public UserCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        return query.select(qUser).from(qUser)
                .where(qUser.username.eq(username)).fetchFirst();
    }

    @Override
    public List<Map<String, Object>> findAllTeachers() {

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QCourse qCourse = QCourse.course;
        QClassroom qClassroom = QClassroom.classroom;

        List<Tuple> teachers = query.select(qUser.firstName, qUser.lastName, qCourse.name, qClassroom.name)
                .from(qUser)
                .where(qUser.roles.contains("ROLE_TEACHER"))
                .leftJoin(qCourse).on(qCourse.teacher.id.eq(qUser.id))
                .leftJoin(qClassroom).on(qClassroom.classMaster.id.eq(qUser.id))
                .fetch();

        List<Map<String, Object>> teacherList = new ArrayList<>();

        return null;
    }

    @Override
    public List<Map<String, Object>> findAllStudents() {

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QClassroom qClassroom = QClassroom.classroom;

        List<Tuple> students = query.select(qUser.firstName, qUser.lastName, qClassroom.name)
                .from(qUser)
                .where(qUser.roles.contains("ROLE_STUDENT"))
                .leftJoin(qClassroom).on(qClassroom.students.contains(qUser))
                .fetch();

        List<Map<String, Object>> teacherList = new ArrayList<>();

        return null;
    }
}
