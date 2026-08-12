package com.felipepalomino.foodorder.catalog.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.catalog.domain.model.MenuItem;
import com.felipepalomino.foodorder.catalog.domain.repository.MenuItemRepository;
import com.felipepalomino.foodorder.catalog.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final JpaMenuItemRepository jpa;

    public MenuItemRepositoryImpl(JpaMenuItemRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public MenuItem save(MenuItem item) {
        return toDomain(jpa.save(toEntity(item)));
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return jpa.findByRestaurantId(restaurantId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> findAll() {
        return jpa.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    private MenuItem toDomain(MenuItemEntity e) {
        MenuItem m = new MenuItem();
        m.setId(e.getId());
        m.setRestaurantId(e.getRestaurantId());
        m.setName(e.getName());
        m.setDescription(e.getDescription());
        m.setPrice(e.getPrice());
        m.setCategory(e.getCategory());
        m.setAvailable(e.isAvailable());
        return m;
    }

    private MenuItemEntity toEntity(MenuItem m) {
        MenuItemEntity e = new MenuItemEntity();
        e.setId(m.getId());
        e.setRestaurantId(m.getRestaurantId());
        e.setName(m.getName());
        e.setDescription(m.getDescription());
        e.setPrice(m.getPrice());
        e.setCategory(m.getCategory());
        e.setAvailable(m.isAvailable());
        return e;
    }
}

