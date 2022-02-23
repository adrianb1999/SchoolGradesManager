package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.School;
import com.adrian99.schoolGradesManager.repository.SchoolRepository;
import com.adrian99.schoolGradesManager.service.SchoolService;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public Iterable<School> findAll() {
        return null;
    }

    @Override
    public School findById(Long aLong) {
        return schoolRepository.findById(aLong).orElse(null);
    }

    @Override
    public School save(School object) {
        return schoolRepository.save(object);
    }

    @Override
    public <S extends School> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }
}
