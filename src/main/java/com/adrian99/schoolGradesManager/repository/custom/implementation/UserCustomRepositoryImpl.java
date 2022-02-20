package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.*;
import com.adrian99.schoolGradesManager.model.QAbsence;
import com.adrian99.schoolGradesManager.model.QClassroom;
import com.adrian99.schoolGradesManager.model.QCourse;
import com.adrian99.schoolGradesManager.model.QMark;
import com.adrian99.schoolGradesManager.model.QUser;
import com.adrian99.schoolGradesManager.repository.custom.UserCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

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

        List<Tuple> teachers  =
                query.select(qUser.id,qUser.username, qUser.email, qUser.firstName, qUser.lastName, qCourse.name, qClassroom.name)
                    .from(qUser)
                    .where(qUser.roles.contains("ROLE_TEACHER"))
                    .leftJoin(qCourse).on(qCourse.teacher.id.eq(qUser.id))
                    .leftJoin(qClassroom).on(qClassroom.eq(qCourse.classRoom))
                //    .leftJoin(qClassroom).on(qClassroom.classMaster.id.eq(qUser.id))
                    .fetch();

        List<Map<String, Object>> teacherList = new ArrayList<>();

        for(int i = 0 ; i < teachers.size(); i++)
        {
            Long id = teachers.get(i).get(0, Long.class);
            String username = teachers.get(i).get(1,String.class);
            String email = teachers.get(i).get(2,String.class);
            String firstName = teachers.get(i).get(3, String.class);
            String lastName = teachers.get(i).get(4, String.class);
            String courseName = teachers.get(i).get(5, String.class);
            String courseClassName = teachers.get(i).get(6, String.class);

            List<String> courseList = new ArrayList<>();
            courseList.add(courseName+" "+courseClassName);

            while(i + 1 < teachers.size()){
                i++;
                if(Objects.equals(teachers.get(i).get(0, String.class), id)){
                    courseList.add(teachers.get(i).get(5,String.class) + " " + teachers.get(i).get(6,String.class));
                }
                else {
                    i--;
                    break;
                }
            }
            teacherList.add(Map.of(
                    "id",id,
                    "username","" + username,
                    "firstName", "" + firstName,
                    "lastName", "" + lastName,
                    "courses", courseList,
                    "email","" + email
                    //"classMaster", ""+classRoom));
            ));
        }

        return teacherList;
    }

    @Override
    public List<Map<String, Object>> findAllStudents() {

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QClassroom qClassroom = QClassroom.classroom;

        List<Map<String, Object>> studentList = query.select(qUser.id,qUser.username, qUser.email, qUser.firstName, qUser.lastName, qClassroom.name, qClassroom.id)
                .from(qUser)
                .where(qUser.roles.contains("ROLE_STUDENT"))
                .leftJoin(qClassroom).on(qClassroom.students.contains(qUser))
                .fetch().stream().map(tuple ->
                        new HashMap<String, Object>() {
                            {
                                put("id", tuple.get(0, String.class));
                                put("username",tuple.get(1,String.class));
                                put("email",tuple.get(2,String.class));
                                put("firstName", tuple.get(3, String.class));
                                put("lastName", tuple.get(4, String.class));
                                put("classroom", tuple.get(5, String.class));
                                put("classroomId", tuple.get(6, String.class));
                            }
                        })
                .collect(Collectors.toList());

        return studentList;
    }

    @Override
    public List<Map<String, Object>> findStudentsByClassroom(Classroom classroom) {

        JPAQuery<User> query = new JPAQuery<>(entityManager);

        QUser qUser = QUser.user;
        QAbsence qAbsence = QAbsence.absence;
        QMark qMark = QMark.mark;
        QClassroom qClassroom = QClassroom.classroom;
//
//        List<Tuple> tuples = query.select(qUser.id, qUser.firstName, qUser.lastName)//, qMark.value, qMark.date)//, qAbsence.date, qAbsence.justified)
//                .from(qUser, qClassroom)
//
        return null;
    }
}
