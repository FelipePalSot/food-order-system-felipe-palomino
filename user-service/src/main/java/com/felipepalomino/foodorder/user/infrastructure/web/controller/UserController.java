package com.felipepalomino.foodorder.user.infrastructure.web.controller;

import com.felipepalomino.foodorder.user.application.service.UserApplicationService;
import com.felipepalomino.foodorder.user.domain.model.User;
import com.felipepalomino.foodorder.user.domain.model.UserRole;
import com.felipepalomino.foodorder.user.infrastructure.security.CustomUserDetailsService;
import com.felipepalomino.foodorder.user.infrastructure.security.JwtTokenProvider;
import com.felipepalomino.foodorder.user.infrastructure.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * INFRAESTRUCTURA - Web: Expone la API REST del User Service.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserApplicationService userService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public UserController(UserApplicationService userService,
                          AuthenticationManager authManager,
                          JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    // ============================================================
    // POST /api/users/auth/login  → obtener JWT
    // ============================================================
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails);
        User user = userService.getUserByEmail(request.getEmail());
        return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getRole().name()));
    }

    // ============================================================
    // POST /api/users/auth/register  → registrar nuevo usuario
    // ============================================================
    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        UserRole role = UserRole.CUSTOMER;
        if (request.getRole() != null) {
            try { role = UserRole.valueOf(request.getRole().toUpperCase()); } catch (Exception ignored) {}
        }
        User created = userService.registerUser(
                request.getName(), request.getEmail(), request.getPassword(),
                request.getPhone(), request.getAddress(), role
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created));
    }

    // ============================================================
    // GET /api/users  → listar todos (solo ADMIN)
    // ============================================================
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> list = userService.getAllUsers().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // ============================================================
    // GET /api/users/{id}  → obtener por ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.from(userService.getUserById(id)));
    }

    // ============================================================
    // PUT /api/users/{id}  → actualizar
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @RequestBody UpdateUserRequest request) {
        User updated = userService.updateUser(id, request.getName(), request.getPhone(), request.getAddress());
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    // ============================================================
    // DELETE /api/users/{id}  → eliminar (solo ADMIN)
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

