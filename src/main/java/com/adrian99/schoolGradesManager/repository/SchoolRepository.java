package com.adrian99.schoolGradesManager.repository;

import com.adrian99.schoolGradesManager.model.School;
import org.springframework.data.repository.CrudRepository;

public interface SchoolRepository extends CrudRepository<School, Long> {
}
