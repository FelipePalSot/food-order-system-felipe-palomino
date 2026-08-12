package com.felipepalomino.foodorder.catalog.application.service;

import com.felipepalomino.foodorder.catalog.domain.exception.MenuItemNotFoundException;
import com.felipepalomino.foodorder.catalog.domain.exception.RestaurantNotFoundException;
import com.felipepalomino.foodorder.catalog.domain.model.MenuItem;
import com.felipepalomino.foodorder.catalog.domain.model.Restaurant;
import com.felipepalomino.foodorder.catalog.domain.repository.MenuItemRepository;
import com.felipepalomino.foodorder.catalog.domain.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CatalogApplicationService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public CatalogApplicationService(RestaurantRepository restaurantRepository,
                                     MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // ============================================================
    // RESTAURANTES
    // ============================================================

    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAllActive();
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    public Restaurant createRestaurant(String name, String type, String description, String address) {
        Restaurant r = new Restaurant();
        r.setName(name);
        r.setType(type.toUpperCase());
        r.setDescription(description);
        r.setAddress(address);
        r.setActive(true);
        return restaurantRepository.save(r);
    }

    public Restaurant updateRestaurant(Long id, String name, String description, String address) {
        Restaurant r = getRestaurantById(id);
        if (name != null) r.setName(name);
        if (description != null) r.setDescription(description);
        if (address != null) r.setAddress(address);
        return restaurantRepository.save(r);
    }

    public void deleteRestaurant(Long id) {
        getRestaurantById(id);  // Valida existencia
        restaurantRepository.deleteById(id);
    }

    // ============================================================
    // MENU ITEMS
    // ============================================================

    @Transactional(readOnly = true)
    public List<MenuItem> getMenuByRestaurant(Long restaurantId) {
        getRestaurantById(restaurantId); // Valida que el restaurante existe
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
    }

    public MenuItem createMenuItem(Long restaurantId, String name, String description,
                                   BigDecimal price, String category) {
        getRestaurantById(restaurantId); // Valida existencia
        MenuItem item = new MenuItem();
        item.setRestaurantId(restaurantId);
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setCategory(category);
        item.setAvailable(true);
        return menuItemRepository.save(item);
    }

    public MenuItem updateMenuItem(Long id, String name, BigDecimal price, Boolean available) {
        MenuItem item = getMenuItemById(id);
        if (name != null) item.setName(name);
        if (price != null) item.setPrice(price);
        if (available != null) item.setAvailable(available);
        return menuItemRepository.save(item);
    }

    public void deleteMenuItem(Long id) {
        getMenuItemById(id);
        menuItemRepository.deleteById(id);
    }
}

