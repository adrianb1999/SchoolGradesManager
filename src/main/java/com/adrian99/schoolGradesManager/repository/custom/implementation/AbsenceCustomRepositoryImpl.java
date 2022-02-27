package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.*;
import com.adrian99.schoolGradesManager.model.QAbsence;
import com.adrian99.schoolGradesManager.model.QMark;
import com.adrian99.schoolGradesManager.model.QSchool;
import com.adrian99.schoolGradesManager.repository.custom.AbsenceCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
public class AbsenceCustomRepositoryImpl implements AbsenceCustomRepository {

    private final EntityManager entityManager;

    public AbsenceCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean checkIfAbsenceExists(LocalDate date, Course course, User student) {

        JPAQuery<Absence> query = new JPAQuery<>(entityManager);
        QAbsence qAbsence = QAbsence.absence;

        List<Absence> absenceList = query.select(qAbsence)
                .from(qAbsence)
                .where(qAbsence.date.eq(date),
                        qAbsence.student.eq(student),
                        qAbsence.course.eq(course))
                .fetch();

        return absenceList.size() != 0;
    }
}
