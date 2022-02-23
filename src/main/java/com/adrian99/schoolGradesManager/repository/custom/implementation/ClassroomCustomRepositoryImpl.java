package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.QClassroom;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.ClassroomCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class ClassroomCustomRepositoryImpl implements ClassroomCustomRepository {

    private final EntityManager entityManager;

    public ClassroomCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
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
