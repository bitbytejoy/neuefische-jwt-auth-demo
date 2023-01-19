package de.neuefische.neuefischejwttokendemo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final AppUserService appUserService;

    @PostMapping
    public LoginResponse login (@RequestBody LoginRequest loginRequest) {
        return appUserService.login(loginRequest);
    }
}
