package com.felipepalomino.foodorder.user.infrastructure.web.dto;

import com.felipepalomino.foodorder.user.domain.model.User;

import java.time.LocalDateTime;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        UserResponse r = new UserResponse();
        r.id = user.getId();
        r.name = user.getName();
        r.email = user.getEmail();
        r.phone = user.getPhone();
        r.address = user.getAddress();
        r.role = user.getRole() != null ? user.getRole().name() : null;
        r.createdAt = user.getCreatedAt();
        return r;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

