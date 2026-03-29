package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.adapter.in.web.dto.AuthLoginResponse;
import com.example.stylohub.adapter.in.web.dto.LoginRequest;
import com.example.stylohub.adapter.in.web.dto.RegisterRequest;
import com.example.stylohub.application.command.RegisterUserCommand;
import com.example.stylohub.application.dto.AuthTokenDTO;
import com.example.stylohub.application.port.in.AuthUseCase;
import com.example.stylohub.infrastructure.security.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Autenticação e gestão de senha")
public class AuthController {

    private static final String ACCESS_COOKIE  = "stylohub_token";
    private static final String REFRESH_COOKIE = "stylohub_refresh";

    private final AuthUseCase authUseCase;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    public AuthController(AuthUseCase authUseCase, RefreshTokenService refreshTokenService) {
        this.authUseCase = authUseCase;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova conta — JWT entregue via cookie httpOnly")
    AuthLoginResponse register(@Valid @RequestBody RegisterRequest request,
                               HttpServletResponse response) {
        AuthTokenDTO dto = authUseCase.register(new RegisterUserCommand(
                request.email(), request.password(), request.username()
        ));
        issueTokens(response, dto);
        return new AuthLoginResponse(dto.username(), dto.email());
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica com e-mail e senha — JWT entregue via cookie httpOnly")
    AuthLoginResponse login(@Valid @RequestBody LoginRequest request,
                            HttpServletResponse response) {
        AuthTokenDTO dto = authUseCase.login(request.email(), request.password());
        issueTokens(response, dto);
        return new AuthLoginResponse(dto.username(), dto.email());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Renova o access token usando o refresh token (cookie httpOnly)")
    void refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractCookie(request, REFRESH_COOKIE);
        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token ausente.");
        }
        String userId = refreshTokenService.validate(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado."));

        AuthTokenDTO dto = authUseCase.refreshAccess(UUID.fromString(userId));
        String newRefresh = refreshTokenService.rotate(refreshToken, userId);

        setAccessCookie(response, dto.accessToken(), dto.expiresIn());
        setRefreshCookie(response, newRefresh);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Encerra a sessão removendo os cookies de autenticação")
    void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractCookie(request, REFRESH_COOKIE);
        if (refreshToken != null) {
            refreshTokenService.revoke(refreshToken);
        }
        clearCookie(response, ACCESS_COOKIE);
        clearCookie(response, REFRESH_COOKIE);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private void issueTokens(HttpServletResponse response, AuthTokenDTO dto) {
        String refreshToken = refreshTokenService.generate(dto.userId());
        setAccessCookie(response, dto.accessToken(), dto.expiresIn());
        setRefreshCookie(response, refreshToken);
    }

    private void setAccessCookie(HttpServletResponse response, String token, long expiresInMs) {
        response.addHeader(HttpHeaders.SET_COOKIE,
                buildCookie(ACCESS_COOKIE, token, Duration.ofMillis(expiresInMs)).toString());
    }

    private void setRefreshCookie(HttpServletResponse response, String token) {
        response.addHeader(HttpHeaders.SET_COOKIE,
                buildCookie(REFRESH_COOKIE, token,
                        Duration.ofMillis(refreshTokenService.getRefreshExpirationMs())).toString());
    }

    private void clearCookie(HttpServletResponse response, String name) {
        response.addHeader(HttpHeaders.SET_COOKIE,
                buildCookie(name, "", Duration.ZERO).toString());
    }

    private ResponseCookie buildCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();
    }

    private String extractCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
