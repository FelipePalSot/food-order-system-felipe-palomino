package com.felipepalomino.foodorder.catalog.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.catalog.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    List<RestaurantEntity> findByActiveTrue();
}

