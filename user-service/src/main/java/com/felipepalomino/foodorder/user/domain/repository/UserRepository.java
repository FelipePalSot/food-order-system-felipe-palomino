package com.felipepalomino.foodorder.user.domain.repository;

import com.felipepalomino.foodorder.user.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * DOMINIO: Puerto (interfaz) que define cómo persistir usuarios.
 * La implementación concreta está en infrastructure/persistence.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsByEmail(String email);
}

