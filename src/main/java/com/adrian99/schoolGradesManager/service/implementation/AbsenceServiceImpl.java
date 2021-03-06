package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.Absence;
import com.adrian99.schoolGradesManager.model.Course;
import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.AbsenceRepository;
import com.adrian99.schoolGradesManager.service.AbsenceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    @Override
    public Iterable<Absence> findAll() {
        return absenceRepository.findAll();
    }

    @Override
    public Absence findById(Long aLong) {
        return absenceRepository.findById(aLong).orElse(null);
    }

    @Override
    public Absence save(Absence object) {
        return absenceRepository.save(object);
    }

    @Override
    public <S extends Absence> Iterable<S> saveAll(Iterable<S> entities) {
        return absenceRepository.saveAll(entities);
    }

    @Override
    public void deleteById(Long aLong) {
        absenceRepository.deleteById(aLong);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        absenceRepository.deleteAllById(longs);
    }

    //Custom repository
    @Override
    public boolean checkIfAbsenceExists(LocalDate date, Course course, User student) {
        return absenceRepository.checkIfAbsenceExists(date, course, student);
    }
}
