package com.adrian99.schoolGradesManager.service;

import com.adrian99.schoolGradesManager.model.Absence;
import com.adrian99.schoolGradesManager.repository.custom.AbsenceCustomRepository;

public interface AbsenceService extends CrudService<Absence, Long>, AbsenceCustomRepository {
}
