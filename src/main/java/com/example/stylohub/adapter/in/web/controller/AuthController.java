package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.dto.AuthLoginResponse;
import com.example.stylohub.adapter.in.web.dto.LoginRequest;
import com.example.stylohub.adapter.in.web.dto.RegisterRequest;
import com.example.stylohub.application.command.RegisterUserCommand;
import com.example.stylohub.application.dto.AuthTokenDTO;
import com.example.stylohub.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticação e gestão de senha")
public class AuthController {

    private static final String COOKIE_NAME = "stylohub_token";

    private final AuthUseCase authUseCase;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova conta — JWT entregue via cookie httpOnly")
    AuthLoginResponse register(@Valid @RequestBody RegisterRequest request,
                               HttpServletResponse response) {
        AuthTokenDTO dto = authUseCase.register(new RegisterUserCommand(
                request.email(),
                request.password(),
                request.username()
        ));
        setAuthCookie(response, dto.accessToken(), dto.expiresIn());
        return new AuthLoginResponse(dto.username(), dto.email());
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica com e-mail e senha — JWT entregue via cookie httpOnly")
    AuthLoginResponse login(@Valid @RequestBody LoginRequest request,
                            HttpServletResponse response) {
        AuthTokenDTO dto = authUseCase.login(request.email(), request.password());
        setAuthCookie(response, dto.accessToken(), dto.expiresIn());
        return new AuthLoginResponse(dto.username(), dto.email());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Encerra a sessão removendo o cookie de autenticação")
    void logout(HttpServletResponse response) {
        ResponseCookie clear = ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clear.toString());
    }

    private void setAuthCookie(HttpServletResponse response, String token, long expiresInMs) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(Duration.ofMillis(expiresInMs))
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
