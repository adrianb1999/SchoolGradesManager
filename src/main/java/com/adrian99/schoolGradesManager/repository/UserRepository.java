package com.adrian99.schoolGradesManager.repository;

import com.adrian99.schoolGradesManager.model.User;
import com.adrian99.schoolGradesManager.repository.custom.UserCustomRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>,
                                        UserCustomRepository {
}
