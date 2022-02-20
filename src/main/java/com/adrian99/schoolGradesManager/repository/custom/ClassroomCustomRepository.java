package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.model.User;

public interface ClassroomCustomRepository {
    Classroom findClassroomByUserId(User currentUser);
}
