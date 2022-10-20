package com.app.service;

import com.app.dto.AuthenticationResponse;
import com.app.dto.LoginRequest;
import com.app.dto.RefreshTokenRequest;
import com.app.dto.SignupRequest;
import com.app.exceptions.AuthException;
import com.app.model.User;
import com.app.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(SignupRequest signupRequest){
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Optional<User> userByEmail = userRepo.getUserByEmail(user.getEmail());

        Optional<User> userByUsername = userRepo.findByUsername(user.getUsername());

        if(!pattern.matcher(user.getEmail()).matches())
           throw new AuthException("Invalid email format!");

        if(userByEmail.isPresent())
            throw new AuthException("Email already in use!");

        if(userByUsername.isPresent())
            throw new AuthException("Username already in use!");

        userRepo.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return AuthenticationResponse.builder()
                    .token(jwtProvider.generateToken(authentication))
                    .refreshToken(refreshTokenService.generateRefreshToken().getRefreshToken())
                    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                    .build();

        } catch (BadCredentialsException e){
            throw new AuthException("Incorrect username or password");
        }

    }

    public User findLoggedInUser(Principal principal) {
        return userRepo.findByUsername(principal.getName()).orElseThrow();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        return AuthenticationResponse.builder()
                .token(jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername()))
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .build();
    }

}
