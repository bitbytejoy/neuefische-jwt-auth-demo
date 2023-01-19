package de.neuefische.neuefischejwttokendemo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthentication implements Authentication {
    private final String jwtToken;
    private final AppUserService appUserService;
    private final String jwtSecret;

    private Map<String, Claim> getJwtClaims() {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            decodedJWT = verifier.verify(jwtToken);
            return decodedJWT.getClaims();
        } catch (JWTVerificationException exception){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Optional<AppUser> appUser = appUserService
            .findUserById(getJwtClaims().get("id").asString());

        if (appUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return List.of(new SimpleGrantedAuthority(appUser.get().getRole()));
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getDetails() {
        return appUserService.findUserById(getJwtClaims().get("id").asString());
    }

    @Override
    public Object getPrincipal() {
        return appUserService.findUserById(getJwtClaims().get("id").asString());
    }

    @Override
    public boolean isAuthenticated() {
        try {
            getJwtClaims();
            return true;
        } catch (ResponseStatusException e) {
            return false;
        }
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return getJwtClaims().get("username").asString();
    }
}
