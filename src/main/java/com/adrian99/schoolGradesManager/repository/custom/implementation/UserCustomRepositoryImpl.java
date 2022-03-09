package com.adrian99.schoolGradesManager.repository.custom.implementation;

import com.adrian99.schoolGradesManager.model.*;
import com.adrian99.schoolGradesManager.model.QAbsence;
import com.adrian99.schoolGradesManager.model.QClassroom;
import com.adrian99.schoolGradesManager.model.QCourse;
import com.adrian99.schoolGradesManager.model.QMark;
import com.adrian99.schoolGradesManager.model.QSchool;
import com.adrian99.schoolGradesManager.model.QUser;
import com.adrian99.schoolGradesManager.repository.custom.UserCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

        List<Tuple> teachers =
                query.select(qUser.id, qUser.username, qUser.email, qUser.firstName, qUser.lastName, qCourse.name, qClassroom.name)
                        .from(qUser)
                        .where(qUser.roles.contains("ROLE_TEACHER"))
                        .leftJoin(qCourse).on(qCourse.teacher.id.eq(qUser.id))
                        .leftJoin(qClassroom).on(qClassroom.eq(qCourse.classRoom))
                        .fetch();

        List<Map<String, Object>> teacherList = new ArrayList<>();

        Set<Long> teacherSet = teachers.stream().map(tuple -> tuple.get(0, Long.class)).collect(Collectors.toSet());

        for (Long id : teacherSet) {
            String username = "";
            String email = "";
            String firstName = "";
            String lastName = "";
            String courseName;
            String courseClassName;

            List<String> courseList = new ArrayList<>();

            for (var teacher : teachers) {
                if (teacher.get(0, Long.class) == id) {
                    username = teacher.get(1, String.class);
                    email = teacher.get(2, String.class);
                    firstName = teacher.get(3, String.class);
                    lastName = teacher.get(4, String.class);
                    courseName = teacher.get(5, String.class);
                    courseClassName = teacher.get(6, String.class);

                    if (courseName != null)
                        courseList.add(courseName + ", clasa " + courseClassName);
                    else
                        courseList.add("Nu preda nicio materie!");
                }
            }

            teacherList.add(Map.of(
                    "id", id,
                    "username", username,
                    "firstName", firstName,
                    "lastName", lastName,
                    "courses", courseList,
                    "email", email
            ));
        }

        return teacherList;
    }

    @Override
    public List<Map<String, Object>> findAllStudents() {

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QClassroom qClassroom = QClassroom.classroom;

        List<Map<String, Object>> studentList = query.select(qUser.id, qUser.username, qUser.email, qUser.firstName, qUser.lastName, qClassroom.name, qClassroom.id)
                .from(qUser)
                .where(qUser.roles.contains("ROLE_STUDENT"))
                .leftJoin(qClassroom).on(qClassroom.students.contains(qUser))
                .fetch().stream().map(tuple ->
                        new HashMap<String, Object>() {
                            {
                                put("id", tuple.get(0, String.class));
                                put("username", tuple.get(1, String.class));
                                put("email", tuple.get(2, String.class));
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
    public List<Map<String, Object>> findStudentsByCourse(Course course) {

        JPAQuery<User> query = new JPAQuery<>(entityManager);

        QUser qUser = QUser.user;
        QAbsence qAbsence = QAbsence.absence;
        QMark qMark = QMark.mark;
        QCourse qCourse = QCourse.course;
        QSchool qSchool = QSchool.school;

        List<Tuple> studentTuples = query.select(qUser.id, qUser.firstName, qUser.lastName)
                .from(qUser, qCourse)
                .where(qUser.roles.contains("ROLE_STUDENT").and(qCourse.classRoom.students.contains(qUser).and(qCourse.eq(course))))
                .fetch();

        List<Tuple> markSem1Tuples = query.select(qMark.value, qMark.date, qUser.id, qMark.examMark)
                .from(qMark, qSchool)
                .leftJoin(qUser).on(qMark.student.eq(qUser))
                .where(qMark.student.eq(qUser),
                        qMark.course.eq(course),
                        qMark.date.between(qSchool.firstSemesterStartDate, qSchool.firstSemesterEndDate),
                        qSchool.Id.eq(1L))
                .fetch();

        JPAQuery<User> query2 = new JPAQuery<>(entityManager);

        List<Tuple> markSem2Tuples = query2.select(qMark.value, qMark.date, qUser.id, qMark.examMark)
                .from(qMark, qSchool)
                .leftJoin(qUser).on(qMark.student.eq(qUser))
                .where(qMark.student.eq(qUser),
                        qMark.course.eq(course),
                        qMark.date.between(qSchool.secondSemesterStartDate, qSchool.secondSemesterEndDate),
                        qSchool.Id.eq(1L))
                .fetch();

        JPAQuery<User> query3 = new JPAQuery<>(entityManager);

        List<Tuple> absencesSem1Tuples = query3.select(qAbsence.date, qAbsence.justified, qUser.id, qAbsence.id)
                .from(qAbsence, qSchool)
                .leftJoin(qUser).on(qAbsence.student.eq(qUser))
                .where(qUser.roles.contains("ROLE_STUDENT"),
                        qUser.eq(qAbsence.student),
                        qAbsence.course.eq(course),
                        qAbsence.date.between(qSchool.firstSemesterStartDate, qSchool.firstSemesterEndDate),
                        qSchool.Id.eq(1L)
                )
                .fetch();

        JPAQuery<User> query4 = new JPAQuery<>(entityManager);

        List<Tuple> absencesSem2Tuples = query4.select(qAbsence.date, qAbsence.justified, qUser.id, qAbsence.id)
                .from(qAbsence, qSchool)
                .leftJoin(qUser).on(qAbsence.student.eq(qUser))
                .where(qUser.roles.contains("ROLE_STUDENT"),
                        qUser.eq(qAbsence.student),
                        qAbsence.course.eq(course),
                        qAbsence.date.between(qSchool.secondSemesterStartDate, qSchool.secondSemesterEndDate),
                        qSchool.Id.eq(1L)
                )
                .fetch();

        List<Map<String, Object>> studentsMapList = new ArrayList<>();

        for (Tuple student : studentTuples) {
            Map<String, Object> currentMap = new HashMap<>();
            Long currentId = student.get(0, Long.class);

            double averageSem1 = 0D;
            double averageSem2 = 0D;

            Integer markSem1 = 0;
            Integer markSem2 = 0;

            int numOfMarkSem1 = 0;
            int numOfMarkSem2 = 0;

            currentMap.put("student_id", currentId);
            currentMap.put("studentFirstName", student.get(1, String.class));
            currentMap.put("studentLastName", student.get(2, String.class));

            List<Map<String, Object>> markListSem1 = new ArrayList<>();
            List<Map<String, Object>> markListSem2 = new ArrayList<>();
            List<Map<String, Object>> absenceListSem1 = new ArrayList<>();
            List<Map<String, Object>> absenceListSem2 = new ArrayList<>();


            Map<String, Object> examGradeSem1 = new HashMap<>();
            examGradeSem1.put("date", null);
            examGradeSem1.put("value", null);

            Map<String, Object> examGradeSem2 = new HashMap<>();

            examGradeSem2.put("date", null);
            examGradeSem2.put("value", null);


            for (Tuple markTupleSem1 : markSem1Tuples) {
                if (Objects.equals(markTupleSem1.get(2, Long.class), currentId)) {

                    LocalDate currentDate = markTupleSem1.get(1, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    if (Boolean.FALSE.equals(markTupleSem1.get(3, Boolean.class))) {
                        markListSem1.add(Map.of("date", date,
                                "value", markTupleSem1.get(0, Integer.class)));
                        averageSem1 = averageSem1 + markTupleSem1.get(0, Integer.class);
                        numOfMarkSem1++;
                    } else {
                        markSem1 = markTupleSem1.get(0, Integer.class);
                        examGradeSem1.put("date", date);
                        examGradeSem1.put("value", markSem1);
                    }
                }
            }

            for (Tuple markTupleSem2 : markSem2Tuples) {
                if (Objects.equals(markTupleSem2.get(2, Long.class), currentId)) {

                    LocalDate currentDate = markTupleSem2.get(1, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    if (Boolean.FALSE.equals(markTupleSem2.get(3, Boolean.class))) {
                        markListSem2.add(Map.of("date", date,
                                "value", markTupleSem2.get(0, Integer.class)));
                        averageSem2 = averageSem2 + markTupleSem2.get(0, Integer.class);
                        numOfMarkSem2++;

                    } else {
                        markSem2 = markTupleSem2.get(0, Integer.class);
                        examGradeSem2.put("date", date);
                        examGradeSem2.put("value", markSem2);
                    }
                }
            }

            for (Tuple absenceTuple : absencesSem1Tuples) {
                if (Objects.equals(absenceTuple.get(2, Long.class), currentId)) {

                    LocalDate currentDate = absenceTuple.get(0, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    absenceListSem1.add(Map.of("date", date,
                            "justified", absenceTuple.get(1, Boolean.class),
                            "id", absenceTuple.get(3, Long.class)));
                }
            }

            for (Tuple absenceTuple : absencesSem2Tuples) {
                if (Objects.equals(absenceTuple.get(2, Long.class), currentId)) {

                    LocalDate currentDate = absenceTuple.get(0, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    absenceListSem2.add(Map.of("date", date,
                            "justified", absenceTuple.get(1, Boolean.class),
                            "id", absenceTuple.get(3, Long.class)));
                }
            }

            if (numOfMarkSem1 != 0) {
                averageSem1 = BigDecimal.valueOf(averageSem1 / numOfMarkSem1)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }

            if (numOfMarkSem2 != 0) {
                averageSem2 = BigDecimal.valueOf(averageSem2 / numOfMarkSem2)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }

            if (course.getExam()) {
                if (markSem1 != null && averageSem1 != 0)
                    averageSem1 = BigDecimal.valueOf((averageSem1 * 0.75) + ((double) markSem1 * 0.25))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                if (markSem1 != null && averageSem2 != 0)
                    averageSem2 = BigDecimal.valueOf((averageSem2 * 0.75) + ((double) markSem2 * 0.25))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();
            }

            currentMap.put("examCourse", course.getExam());

            currentMap.put("studentMarksSem1", markListSem1);
            currentMap.put("averageSem1", averageSem1);
            currentMap.put("examMarkSem1", examGradeSem1);

            currentMap.put("studentMarksSem2", markListSem2);
            currentMap.put("averageSem2", averageSem2);
            currentMap.put("examMarkSem2", examGradeSem2);

            currentMap.put("studentAbsencesSem1", absenceListSem1);
            currentMap.put("studentAbsencesSem2", absenceListSem2);

            studentsMapList.add(currentMap);
        }

        return studentsMapList;
    }

    @Override
    public List<Map<String, Object>> findStudentsByClassroom(Classroom classroom) {

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        QClassroom qClassroom = QClassroom.classroom;

        List<Map<String, Object>> collect = query.select(qUser.id, qUser.firstName, qUser.lastName)
                .from(qUser, qClassroom)
                .where(qClassroom.students.contains(qUser),
                        qClassroom.eq(classroom))
                .fetch().stream().map(tuple -> new HashMap<String, Object>() {{
                    put("studentId", tuple.get(0, Long.class));
                    put("studentName", tuple.get(1, String.class) + " " + tuple.get(2, String.class));
                }}).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<Map<String, Object>> findMarksByStudentAndCourse(User student, Course course) {

        findMarksByStudent(student);

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QMark qMark = QMark.mark;
        QCourse qCourse = QCourse.course;

        List<Map<String, Object>> collect = query.select(qMark.value, qMark.date, qCourse.name, qCourse.id)
                .from(qMark)
                .leftJoin(qCourse).on(qMark.course.id.eq(qCourse.id))
                .where(qMark.student.eq(student),
                        qCourse.eq(course))
                .fetch().stream()
                .map(tuple ->
                        new HashMap<String, Object>() {
                            {
                                put("value", tuple.get(0, Integer.class));
                                put("date", tuple.get(1, LocalDate.class));
                                put("course", tuple.get(2, String.class));
                            }
                        }
                ).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Map<String, Object>> findAbsencesByStudentAndCourse(User student, Course course) {

        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QAbsence qAbsence = QAbsence.absence;
        QCourse qCourse = QCourse.course;

        List<Map<String, Object>> collect = query.select(qAbsence.date, qAbsence.justified, qCourse.name)
                .from(qAbsence)
                .leftJoin(qCourse).on(qAbsence.course.eq(qCourse))
                .where(qAbsence.student.eq(student),
                        qCourse.eq(course))
                .fetch().stream()
                .map(tuple ->
                        new HashMap<String, Object>() {
                            {
                                put("date", tuple.get(0, Integer.class));
                                put("justified", tuple.get(1, LocalDate.class));
                                put("course", tuple.get(2, String.class));
                            }
                        }
                ).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Map<String, Object>> findMarksByStudent(User student) {

        JPAQuery<User> query = new JPAQuery<>(entityManager);

        QMark qMark = QMark.mark;
        QCourse qCourse = QCourse.course;
        QSchool qSchool = QSchool.school;
        QAbsence qAbsence = QAbsence.absence;

        List<Tuple> studentCourses = query.select(qCourse.id, qCourse.name, qCourse.exam)
                .from(qCourse)
                .where(qCourse.classRoom.students.contains(student))
                .fetch();

        List<Tuple> markSem1 = query
                .select(qMark.id, qMark.value, qMark.date, qMark.examMark, qCourse.name, qCourse.id)
                .from(qMark, qSchool)
                .leftJoin(qCourse).on(qCourse.eq(qMark.course))
                .where(qMark.student.eq(student),
                        qCourse.eq(qMark.course),
                        qMark.date.between(qSchool.firstSemesterStartDate, qSchool.firstSemesterEndDate),
                        qSchool.Id.eq(1L))
                .fetch();

        JPAQuery<User> query2 = new JPAQuery<>(entityManager);

        List<Tuple> markSem2 = query2
                .select(qMark.id, qMark.value, qMark.date, qMark.examMark, qCourse.name, qCourse.id)
                .from(qMark, qSchool)
                .leftJoin(qCourse).on(qCourse.eq(qMark.course))
                .where(qMark.student.eq(student),
                        qCourse.eq(qMark.course),
                        qMark.date.between(qSchool.secondSemesterStartDate, qSchool.secondSemesterEndDate),
                        qSchool.Id.eq(1L))
                .fetch();

        JPAQuery<User> query3 = new JPAQuery<>(entityManager);

        List<Tuple> absencesListSem1 = query3.select(qAbsence.date, qAbsence.justified, qAbsence.id, qAbsence.course.id)
                .from(qAbsence, qSchool)
                .where(qAbsence.student.eq(student),
                        qAbsence.date.between(qSchool.firstSemesterStartDate, qSchool.firstSemesterEndDate),
                        qSchool.Id.eq(1L))
                .fetch();

        JPAQuery<User> query4 = new JPAQuery<>(entityManager);

        List<Tuple> absencesListSem2 = query4.select(qAbsence.date, qAbsence.justified, qAbsence.id, qAbsence.course.id)
                .from(qAbsence, qSchool)
                .where(qAbsence.student.eq(student),
                        qAbsence.date.between(qSchool.secondSemesterStartDate, qSchool.secondSemesterEndDate),
                        qSchool.Id.eq(1L))
                .fetch();

        List<Map<String, Object>> mapList = new ArrayList<>();

        for (int i = 0; i < studentCourses.size(); i++) {
            long courseId = studentCourses.get(i).get(0, Long.class);

            List<Map<String, Object>> marksListSem1 = new ArrayList<>();
            List<Map<String, Object>> marksListSem2 = new ArrayList<>();

            Map<String, Object> currentMap = new HashMap<>();

            Map<String, Object> examSem1 = new HashMap<>();
            Map<String, Object> examSem2 = new HashMap<>();

            examSem1.put("date", null);
            examSem1.put("value", null);

            examSem2.put("date", null);
            examSem2.put("value", null);

            List<Map<String, Object>> absenceListSem1 = new ArrayList<>();
            List<Map<String, Object>> absenceListSem2 = new ArrayList<>();

            int examMarkSem1 = 0;
            int examMarkSem2 = 0;

            double averageSem1 = 0;
            double averageSem2 = 0;

            int sem1 = 0;
            int sem2 = 0;

            for (Tuple marks : markSem1) {
                if (courseId == marks.get(5, Long.class)) {

                    LocalDate currentDate = marks.get(2, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    if (Boolean.FALSE.equals(marks.get(3, Boolean.class))) {

                        marksListSem1.add(Map.of(
                                "value", marks.get(1, Integer.class),
                                "date", date
                        ));
                        sem1++;
                        averageSem1 += marks.get(1, Integer.class);
                    } else {
                        examMarkSem1 = marks.get(1, Integer.class);
                        examSem1.put("value", examMarkSem1);
                        examSem1.put("date", date);
                    }
                }
            }

            for (Tuple marks : markSem2) {
                if (courseId == marks.get(5, Long.class)) {

                    LocalDate currentDate = marks.get(2, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    if (Boolean.FALSE.equals(marks.get(3, Boolean.class))) {

                        marksListSem2.add(Map.of(
                                "value", marks.get(1, Integer.class),
                                "date", date
                        ));
                        sem2++;
                        averageSem2 += marks.get(1, Integer.class);
                    } else {
                        examMarkSem2 = marks.get(1, Integer.class);
                        examSem2.put("value", examMarkSem2);
                        examSem2.put("date", date);
                    }
                }
            }

            for (Tuple absenceTuple : absencesListSem1) {
                if (courseId == absenceTuple.get(3, Long.class)) {
                    LocalDate currentDate = absenceTuple.get(0, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    absenceListSem1.add(Map.of("date", date,
                            "justified", absenceTuple.get(1, Boolean.class),
                            "id", absenceTuple.get(2, Long.class)));
                }
            }

            for (Tuple absenceTuple : absencesListSem2) {
                if (courseId == absenceTuple.get(3, Long.class)) {
                    LocalDate currentDate = absenceTuple.get(0, LocalDate.class);
                    String date = "" + currentDate.getDayOfMonth() + "/" + currentDate.getMonthValue();

                    absenceListSem2.add(Map.of("date", date,
                            "justified", absenceTuple.get(1, Boolean.class),
                            "id", absenceTuple.get(2, Long.class)));
                }
            }

            if (sem1 != 0)
                averageSem1 = BigDecimal.valueOf(averageSem1 / sem1).setScale(2, RoundingMode.HALF_UP).doubleValue();

            if (sem2 != 0)
                averageSem2 = BigDecimal.valueOf(averageSem2 / sem2).setScale(2, RoundingMode.HALF_UP).doubleValue();

            if (Boolean.TRUE.equals(studentCourses.get(0).get(2, Boolean.class))) {

                if(examMarkSem1 != 0 && averageSem1 != 0)
                    averageSem1 = BigDecimal.valueOf(averageSem1 * 0.75 + examMarkSem1 * 0.25)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                if(examMarkSem2 != 0 && averageSem2 != 0)
                    averageSem2 = BigDecimal.valueOf(averageSem2 * 0.75 + examMarkSem2 * 0.25)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();
            }

            currentMap.put("name", studentCourses.get(i).get(1, String.class));

            currentMap.put("marksSem1", marksListSem1);
            currentMap.put("marksSem2", marksListSem2);

            currentMap.put("averageSem1", averageSem1);
            currentMap.put("averageSem2", averageSem2);

            currentMap.put("examCourse", studentCourses.get(i).get(2, Boolean.class));

            currentMap.put("examMarkSem1", examSem1);
            currentMap.put("examMarkSem2", examSem2);

            currentMap.put("absencesSem1", absenceListSem1);
            currentMap.put("absencesSem2", absenceListSem2);
            mapList.add(currentMap);
        }
        return mapList;
    }

    @Override
    public User findByEmail(String email) {
        JPAQuery<User> query = new JPAQuery<>(entityManager);
        QUser qUser = QUser.user;
        return query.select(qUser).from(qUser)
                .where(qUser.email.eq(email)).fetchFirst();
    }
}
