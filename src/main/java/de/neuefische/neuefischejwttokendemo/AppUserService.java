package de.neuefische.neuefischejwttokendemo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {
    @Value("jwt.secret")
    private String secret;

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser create(AppUser appUser) {
        if (
            appUserRepository.findByUsername(appUser.getUsername()).isPresent()
        ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("BASIC");

        appUserRepository.save(appUser);

        appUser.setPassword("");

        return appUser;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<AppUser> optionalAppUser = appUserRepository.findByUsername(
            loginRequest.getUsername()
        );

        if (optionalAppUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        AppUser appUser = optionalAppUser.get();

        if (
            !passwordEncoder.matches(
                loginRequest.getPassword(),
                appUser.getPassword()
            )
        ) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            String token = JWT.create()
                .withClaim("id", appUser.getId())
                .withClaim("username", appUser.getUsername())
                .sign(algorithm);

            return new LoginResponse(token);
        } catch (JWTCreationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public Optional<AppUser> findUserById (String userId) {
        return appUserRepository.findById(userId);
    }
}
