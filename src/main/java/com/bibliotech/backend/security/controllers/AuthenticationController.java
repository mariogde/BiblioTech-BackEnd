package com.bibliotech.backend.security.controllers;

import com.bibliotech.backend.security.models.dtos.AuthenticationDTO;
import com.bibliotech.backend.security.models.dtos.LoginResponseDTO;
import com.bibliotech.backend.security.services.TokenService;
import com.bibliotech.backend.users.models.dtos.PasswordResetDTO;
import com.bibliotech.backend.users.models.entities.User;
import com.bibliotech.backend.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody AuthenticationDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        User user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        // Retorna o token e o booleano de admin do usuário
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getAdmin()));
    }

    @PostMapping("/forgot-password/validate")
    public ResponseEntity<Void> validateRecovery(@RequestBody Map<String, String> payload) {
        userService.validateRecoveryData(payload.get("email"), payload.get("cpf"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/forgot-password/reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        userService.resetPassword(dto);
        return ResponseEntity.noContent().build();
    }
}