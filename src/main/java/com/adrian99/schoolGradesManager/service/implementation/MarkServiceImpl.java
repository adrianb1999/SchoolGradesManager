package com.adrian99.schoolGradesManager.service.implementation;

import com.adrian99.schoolGradesManager.model.Mark;
import com.adrian99.schoolGradesManager.repository.MarkRepository;
import com.adrian99.schoolGradesManager.service.MarkService;
import org.springframework.stereotype.Service;

@Service
public class MarkServiceImpl implements MarkService {

    private final MarkRepository markRepository;

    public MarkServiceImpl(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    @Override
    public Mark findById(Long aLong) {
        return markRepository.findById(aLong).orElse(null);
    }

    @Override
    public Mark save(Mark object) {
        return markRepository.save(object);
    }

    @Override
    public <S extends Mark> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }
}
