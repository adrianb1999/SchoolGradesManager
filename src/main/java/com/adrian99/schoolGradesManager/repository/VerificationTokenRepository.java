package com.adrian99.schoolGradesManager.repository;


import com.adrian99.schoolGradesManager.repository.custom.VerificationTokenCustomRepository;
import com.adrian99.schoolGradesManager.token.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends
        CrudRepository<VerificationToken, Long>, VerificationTokenCustomRepository {
}
