package com.felipepalomino.foodorder.catalog.domain.repository;

import com.felipepalomino.foodorder.catalog.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(Long id);
    List<Restaurant> findAll();
    List<Restaurant> findAllActive();
    void deleteById(Long id);
}

