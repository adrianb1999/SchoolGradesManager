package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.UserRepository;
import com.adrian99.schoolGradesManager.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long aLong) {
        return userRepository.findById(aLong).orElse(null);
    }

    @Override
    public User save(User object) {
        return userRepository.save(object);
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return userRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Custom queryes
    @Override
    public List<Map<String, Object>> findAllTeachers() {
        return userRepository.findAllTeachers();
    }

    @Override
    public List<Map<String, Object>> findAllStudents() {
        return userRepository.findAllStudents();
    }

    @Override
    public List<Map<String, Object>> findStudentsByCourse(Course course) {
        return userRepository.findStudentsByCourse(course);
    }

    @Override
    public List<Map<String, Object>> findStudentsByClassroom(Classroom classroom) {
        return userRepository.findStudentsByClassroom(classroom);
    }

    @Override
    public List<Map<String, Object>> findMarksByStudentAndCourse(User student, Course course) {
        return userRepository.findMarksByStudentAndCourse(student, course);
    }

    @Override
    public List<Map<String, Object>> findAbsencesByStudentAndCourse(User student, Course course) {
        return userRepository.findAbsencesByStudentAndCourse(student, course);
    }

    @Override
    public List<Map<String, Object>> findMarksByStudent(User student) {
        return userRepository.findMarksByStudent(student);
    }
}
