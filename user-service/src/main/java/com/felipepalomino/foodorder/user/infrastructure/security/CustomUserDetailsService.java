package com.felipepalomino.foodorder.user.infrastructure.security;

import com.felipepalomino.foodorder.user.infrastructure.persistence.entity.UserEntity;
import com.felipepalomino.foodorder.user.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * INFRAESTRUCTURA - Seguridad: Carga el usuario desde la BD para Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JpaUserRepository jpaUserRepository;

    public CustomUserDetailsService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity entity = jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        return new User(
                entity.getEmail(),
                entity.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + entity.getRole()))
        );
    }
}

