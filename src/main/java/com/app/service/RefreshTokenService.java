package com.app.service;

import com.app.exceptions.NotFoundException;
import com.app.model.RefreshToken;
import com.app.repository.RefreshTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepo.save(refreshToken);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepo.findByRefreshToken(token)
                .orElseThrow(() -> new NotFoundException("Invalid refresh token"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepo.findByRefreshToken(token).orElseThrow(() ->
                new NotFoundException("There is no such refresh token!"));

        refreshTokenRepo.deleteByRefreshToken(token);
    }

}
