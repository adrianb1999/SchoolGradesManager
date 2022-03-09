package com.adrian99.schoolGradesManager.service;


import com.adrian99.schoolGradesManager.repository.custom.VerificationTokenCustomRepository;
import com.adrian99.schoolGradesManager.token.TokenState;
import com.adrian99.schoolGradesManager.token.TokenType;
import com.adrian99.schoolGradesManager.token.VerificationToken;

public interface VerificationTokenService extends CrudService<VerificationToken, Long>,
                                                  VerificationTokenCustomRepository {
    VerificationToken isTokenValid(String token);
    TokenState isTokenValidHtml(String token, TokenType tokenType);
}
