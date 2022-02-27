package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.*;
import com.adrian99.schoolGradesManager.model.QMark;
import com.adrian99.schoolGradesManager.model.QSchool;
import com.adrian99.schoolGradesManager.repository.custom.MarkCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MarkCustomRepositoryImpl implements MarkCustomRepository {

    private final EntityManager entityManager;

    public MarkCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean checkIfExamMarkExist(LocalDate markDate, Course course, User student) {

        JPAQuery<Mark> query = new JPAQuery<>(entityManager);
        QMark qMark = QMark.mark;
        QSchool qSchool = QSchool.school;

        List<Tuple> firstSemesterList = query.select(qMark, qSchool.firstSemesterStartDate, qSchool.firstSemesterEndDate)
                .from(qMark, qSchool)
                .where(qMark.examMark.eq(true),
                        qMark.course.eq(course),
                        qMark.date.between(qSchool.firstSemesterStartDate, qSchool.firstSemesterEndDate),
                        qMark.student.eq(student),
                        qSchool.Id.eq(1L))
                .fetch();

        JPAQuery<Mark> query2 = new JPAQuery<>(entityManager);

        List<Tuple> secondSemesterList = query2.select(qMark,  qSchool.secondSemesterStartDate, qSchool.secondSemesterEndDate)
                .from(qMark, qSchool)
                .where(qMark.examMark.eq(true),
                        qMark.course.eq(course),
                        qMark.date.between(qSchool.secondSemesterStartDate, qSchool.secondSemesterEndDate),
                        qMark.student.eq(student),
                        qSchool.Id.eq(1L))
                .fetch();

        if(firstSemesterList.size() != 0) {
            LocalDate firstDate = firstSemesterList.get(0).get(1,LocalDate.class);
            LocalDate secondDate = firstSemesterList.get(0).get(2,LocalDate.class);

            if(markDate.isAfter(firstDate) && markDate.isBefore(secondDate))
                return true;
        }
        if(secondSemesterList.size() != 0) {
            LocalDate firstDate = secondSemesterList.get(0).get(1,LocalDate.class);
            LocalDate secondDate = secondSemesterList.get(0).get(2,LocalDate.class);

            if(markDate.isAfter(firstDate) && markDate.isBefore(secondDate))
                return true;
        }

        return false;
    }
}
