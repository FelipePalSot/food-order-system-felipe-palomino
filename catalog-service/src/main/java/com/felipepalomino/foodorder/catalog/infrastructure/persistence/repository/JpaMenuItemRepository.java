package com.felipepalomino.foodorder.catalog.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.catalog.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    List<MenuItemEntity> findByRestaurantId(Long restaurantId);
}

