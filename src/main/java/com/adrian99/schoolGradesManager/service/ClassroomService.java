package com.adrian99.schoolGradesManager.service;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.repository.custom.ClassroomCustomRepository;

public interface ClassroomService extends CrudService<Classroom, Long>,
        ClassroomCustomRepository {
}

