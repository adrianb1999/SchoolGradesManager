package com.adrian99.schoolGradesManager.service.implementation;

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
    public User findById(Long aLong) {
        return userRepository.findById(aLong).orElse(null);
    }

    @Override
    public User save(User object) {
        return userRepository.save(object);
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
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
}
