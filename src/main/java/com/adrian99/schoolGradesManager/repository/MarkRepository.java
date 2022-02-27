package com.adrian99.schoolGradesManager.repository;

import com.adrian99.schoolGradesManager.model.Mark;
import com.adrian99.schoolGradesManager.repository.custom.MarkCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface MarkRepository extends CrudRepository<Mark, Long>, MarkCustomRepository {
}
