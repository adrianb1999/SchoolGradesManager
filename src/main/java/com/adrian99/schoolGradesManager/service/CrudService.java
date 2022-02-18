package com.adrian99.schoolGradesManager.service;

public interface CrudService<T, ID> {
    T findById(ID id);

    T save(T object);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    void deleteById(ID id);

    void deleteAllById(Iterable<? extends ID> ids);
}
