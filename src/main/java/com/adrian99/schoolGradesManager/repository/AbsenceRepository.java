package com.adrian99.schoolGradesManager.repository;

import com.adrian99.schoolGradesManager.model.Absence;
import com.adrian99.schoolGradesManager.repository.custom.AbsenceCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface AbsenceRepository extends CrudRepository<Absence, Long>, AbsenceCustomRepository {
}
