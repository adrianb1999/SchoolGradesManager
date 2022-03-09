package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.UserRepository;
import com.adrian99.schoolGradesManager.repository.VerificationTokenRepository;
import com.adrian99.schoolGradesManager.service.UserService;
import com.adrian99.schoolGradesManager.token.VerificationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
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

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String passwordGenerator(int length) {
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; i++){
            char c = (char) (random.nextInt(26) + 65);
            boolean lower = random.nextBoolean();

            if(lower)
                c = (char) (c + 32);

            password.append(c);
        }
        return password.toString();
    }

    @Override
    public boolean isEmailValid(String email) {
        return email.matches("^(.+)@(\\S+)$");
    }

    @Override
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken =
                new VerificationToken(
                        token,
                        user,
                        LocalDateTime.now().plusHours(24));

        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
