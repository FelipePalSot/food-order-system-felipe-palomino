package com.felipepalomino.foodorder.user.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.user.domain.model.User;
import com.felipepalomino.foodorder.user.domain.model.UserRole;
import com.felipepalomino.foodorder.user.domain.repository.UserRepository;
import com.felipepalomino.foodorder.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * INFRAESTRUCTURA: Implementación del repositorio del dominio usando JPA.
 * Aquí vive el mapeo entidad ↔ dominio.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = jpaUserRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    // ============================================================
    // Mapeos manuales (sin MapStruct para mantener simplicidad)
    // ============================================================
    private User toDomain(UserEntity e) {
        User u = new User();
        u.setId(e.getId());
        u.setName(e.getName());
        u.setEmail(e.getEmail());
        u.setPassword(e.getPassword());
        u.setPhone(e.getPhone());
        u.setAddress(e.getAddress());
        u.setRole(UserRole.valueOf(e.getRole()));
        u.setCreatedAt(e.getCreatedAt());
        return u;
    }

    private UserEntity toEntity(User u) {
        UserEntity e = new UserEntity();
        e.setId(u.getId());
        e.setName(u.getName());
        e.setEmail(u.getEmail());
        e.setPassword(u.getPassword());
        e.setPhone(u.getPhone());
        e.setAddress(u.getAddress());
        e.setRole(u.getRole() != null ? u.getRole().name() : UserRole.CUSTOMER.name());
        e.setCreatedAt(u.getCreatedAt());
        return e;
    }
}

