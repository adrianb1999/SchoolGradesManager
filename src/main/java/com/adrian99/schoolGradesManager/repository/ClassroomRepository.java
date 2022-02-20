package com.adrian99.schoolGradesManager.repository;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.repository.custom.ClassroomCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClassroomRepository extends CrudRepository<Classroom, Long>,
                                             ClassroomCustomRepository {
}
