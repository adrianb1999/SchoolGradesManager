package com.adrian99.schoolGradesManager.service;

import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.UserCustomRepository;

public interface UserService extends CrudService<User, Long>, UserCustomRepository{
    String passwordGenerator(int length);
    boolean isEmailValid(String email);
}
