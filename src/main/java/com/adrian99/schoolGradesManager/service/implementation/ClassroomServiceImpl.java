package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.Classroom;
import com.adrian99.schoolGradesManager.repository.ClassroomRepository;
import com.adrian99.schoolGradesManager.service.ClassroomService;
import org.springframework.stereotype.Service;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public Classroom findById(Long aLong) {
        return classroomRepository.findById(aLong).orElse(null);
    }

    @Override
    public Classroom save(Classroom object) {
        return classroomRepository.save(object);
    }

    @Override
    public <S extends Classroom> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }
}