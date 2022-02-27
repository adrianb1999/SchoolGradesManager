package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.QClassroom;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.ClassroomCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ClassroomCustomRepositoryImpl implements ClassroomCustomRepository {

    private final EntityManager entityManager;

    public ClassroomCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Map<String, Object>> findAllClassrooms() {

        JPAQuery<Classroom> query = new JPAQuery<>(entityManager);
        QClassroom qClassroom = QClassroom.classroom;

        return query.select(qClassroom.id,qClassroom.classMaster.firstName,qClassroom.classMaster.lastName,qClassroom.name, qClassroom.students.size(), qClassroom.classMaster.id)
                .from(qClassroom)
                .fetch()
                .stream().map(tuple -> new HashMap<String, Object>(){{
                    put("id",tuple.get(0,Long.class));
                    put("classMasterName",tuple.get(1,String.class) + " " + tuple.get(2, String.class));
                    put("name",tuple.get(3,String.class));
                    put("numOfStudents",tuple.get(4,Integer.class));
                    put("classMasterId",tuple.get(5,Integer.class));
                }}).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Classroom findClassroomByUserId(User currentUser) {

        JPAQuery<Classroom> query = new JPAQuery<>(entityManager);
        QClassroom qClassroom = QClassroom.classroom;

        return query.select(qClassroom)
                .from(qClassroom)
                .where(qClassroom.students.contains(currentUser))
                .fetchFirst();
    }

    @Override
    public Classroom findClassroomByClassmasterId(User classmaster) {

        JPAQuery<Classroom> query = new JPAQuery<>(entityManager);
        QClassroom qClassroom = QClassroom.classroom;

        return query.select(qClassroom)
                .from(qClassroom)
                .where(qClassroom.classMaster.eq(classmaster))
                .fetchFirst();
    }
}
