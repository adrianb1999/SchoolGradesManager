package com.adrian99.schoolGradesManager.service;

import com.adrian99.schoolGradesManager.model.Mark;
import com.adrian99.schoolGradesManager.repository.custom.MarkCustomRepository;

public interface MarkService extends CrudService<Mark, Long>, MarkCustomRepository {
}
