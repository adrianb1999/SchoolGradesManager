package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.User;

import java.util.List;
import java.util.Map;

public interface ClassroomCustomRepository {
    Classroom findClassroomByUserId(User currentUser);
    Classroom findClassroomByClassmasterId(User classmaster);
    List<Map<String, Object>> findAllClassrooms();
}
