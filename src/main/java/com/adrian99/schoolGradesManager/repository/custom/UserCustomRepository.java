package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.model.User;

import java.util.List;
import java.util.Map;

public interface UserCustomRepository {
    User findByUsername(String username);
    List<Map<String, Object>> findAllTeachers();
    List<Map<String, Object>> findAllStudents();
}
