package com.adrian99.schoolGradesManager.repository.custom;

import com.adrian99.schoolGradesManager.token.VerificationToken;

public interface VerificationTokenCustomRepository {
    VerificationToken findByToken(String token);
}
