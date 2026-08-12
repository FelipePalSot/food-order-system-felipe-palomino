package com.felipepalomino.foodorder.catalog.infrastructure.web.controller;

import com.felipepalomino.foodorder.catalog.application.service.CatalogApplicationService;
import com.felipepalomino.foodorder.catalog.domain.model.MenuItem;
import com.felipepalomino.foodorder.catalog.domain.model.Restaurant;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * INFRAESTRUCTURA - Web: Expone la API REST del Catalog Service.
 * GET  /api/restaurants           → listar restaurantes activos
 * GET  /api/restaurants/{id}      → obtener restaurante
 * GET  /api/restaurants/{id}/menu → menú del restaurante
 * GET  /api/menu-items/{id}       → obtener item de menú
 */
@RestController
public class CatalogController {

    private final CatalogApplicationService catalogService;

    public CatalogController(CatalogApplicationService catalogService) {
        this.catalogService = catalogService;
    }

    // ============================================================
    // RESTAURANTES
    // ============================================================
    @GetMapping("/api/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(catalogService.getAllRestaurants());
    }

    @GetMapping("/api/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getRestaurantById(id));
    }

    @PostMapping("/api/restaurants")
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Map<String, String> body) {
        Restaurant r = catalogService.createRestaurant(
                body.get("name"), body.get("type"),
                body.get("description"), body.get("address")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(r);
    }

    @PutMapping("/api/restaurants/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(catalogService.updateRestaurant(
                id, body.get("name"), body.get("description"), body.get("address")));
    }

    @DeleteMapping("/api/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        catalogService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // MENU ITEMS
    // ============================================================
    @GetMapping("/api/restaurants/{restaurantId}/menu")
    public ResponseEntity<List<MenuItem>> getMenu(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(catalogService.getMenuByRestaurant(restaurantId));
    }

    @GetMapping("/api/menu-items/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getMenuItemById(id));
    }

    @PostMapping("/api/restaurants/{restaurantId}/menu")
    public ResponseEntity<MenuItem> createMenuItem(@PathVariable Long restaurantId,
                                                    @RequestBody Map<String, Object> body) {
        MenuItem item = catalogService.createMenuItem(
                restaurantId,
                (String) body.get("name"),
                (String) body.get("description"),
                new BigDecimal(body.get("price").toString()),
                (String) body.get("category")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/api/menu-items/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @RequestBody Map<String, Object> body) {
        BigDecimal price = body.get("price") != null ? new BigDecimal(body.get("price").toString()) : null;
        Boolean available = body.get("available") != null ? (Boolean) body.get("available") : null;
        return ResponseEntity.ok(catalogService.updateMenuItem(id, (String) body.get("name"), price, available));
    }

    @DeleteMapping("/api/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        catalogService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}

