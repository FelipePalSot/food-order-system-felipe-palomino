package com.felipepalomino.foodorder.user.application.service;

import com.felipepalomino.foodorder.user.domain.exception.DuplicateEmailException;
import com.felipepalomino.foodorder.user.domain.exception.UserNotFoundException;
import com.felipepalomino.foodorder.user.domain.model.User;
import com.felipepalomino.foodorder.user.domain.model.UserRole;
import com.felipepalomino.foodorder.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * APLICACIÓN: Casos de uso del dominio de usuarios.
 * Orquesta la lógica de negocio usando el repositorio del dominio.
 */
@Service
@Transactional
public class UserApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserApplicationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // CASO DE USO: Registrar nuevo usuario
    // ============================================================
    public User registerUser(String name, String email, String password,
                             String phone, String address, UserRole role) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role != null ? role : UserRole.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // ============================================================
    // CASO DE USO: Obtener todos los usuarios
    // ============================================================
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ============================================================
    // CASO DE USO: Obtener usuario por ID
    // ============================================================
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // ============================================================
    // CASO DE USO: Obtener usuario por email
    // ============================================================
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    // ============================================================
    // CASO DE USO: Actualizar usuario
    // ============================================================
    public User updateUser(Long id, String name, String phone, String address) {
        User user = getUserById(id);
        if (name != null && !name.isBlank()) user.setName(name);
        if (phone != null) user.setPhone(phone);
        if (address != null) user.setAddress(address);
        return userRepository.save(user);
    }

    // ============================================================
    // CASO DE USO: Eliminar usuario
    // ============================================================
    public void deleteUser(Long id) {
        if (!userRepository.existsByEmail(getUserById(id).getEmail())) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}

