package com.felipepalomino.foodorder.catalog.domain.repository;

import com.felipepalomino.foodorder.catalog.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {
    MenuItem save(MenuItem item);
    Optional<MenuItem> findById(Long id);
    List<MenuItem> findByRestaurantId(Long restaurantId);
    List<MenuItem> findAll();
    void deleteById(Long id);
}

