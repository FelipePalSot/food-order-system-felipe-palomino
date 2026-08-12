package com.felipepalomino.foodorder.catalog.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.catalog.domain.model.Restaurant;
import com.felipepalomino.foodorder.catalog.domain.repository.RestaurantRepository;
import com.felipepalomino.foodorder.catalog.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final JpaRestaurantRepository jpa;

    public RestaurantRepositoryImpl(JpaRestaurantRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Restaurant save(Restaurant r) {
        return toDomain(jpa.save(toEntity(r)));
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return jpa.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> findAllActive() {
        return jpa.findByActiveTrue().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    private Restaurant toDomain(RestaurantEntity e) {
        Restaurant r = new Restaurant();
        r.setId(e.getId());
        r.setName(e.getName());
        r.setType(e.getType());
        r.setDescription(e.getDescription());
        r.setAddress(e.getAddress());
        r.setActive(e.isActive());
        return r;
    }

    private RestaurantEntity toEntity(Restaurant r) {
        RestaurantEntity e = new RestaurantEntity();
        e.setId(r.getId());
        e.setName(r.getName());
        e.setType(r.getType());
        e.setDescription(r.getDescription());
        e.setAddress(r.getAddress());
        e.setActive(r.isActive());
        return e;
    }
}

