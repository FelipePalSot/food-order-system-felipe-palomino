package com.felipepalomino.foodorder.order.infrastructure.client;

import com.felipepalomino.foodorder.order.domain.exception.ExternalServiceException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * INFRAESTRUCTURA - Cliente REST con Circuit Breaker.
 *
 * ¿Qué hace el Circuit Breaker?
 *  - CLOSED (normal): las llamadas pasan directo al catalog-service.
 *  - Si hay 5 fallos seguidos → se ABRE el circuito.
 *  - OPEN: deja de llamar al catalog-service y llama al fallback.
 *  - Después de 10s → HALF_OPEN: prueba si el servicio volvió.
 */
@Component
public class CatalogServiceClient {

    private final RestTemplate restTemplate;
    private final String catalogServiceUrl;

    public CatalogServiceClient(RestTemplate restTemplate,
                                @Value("${catalog.service.url}") String catalogServiceUrl) {
        this.restTemplate = restTemplate;
        this.catalogServiceUrl = catalogServiceUrl;
    }

    /**
     * Obtiene un item de menú. Si el catalog-service falla → fallback.
     * name = "catalog-cb" debe coincidir con la config en application.yaml
     */
    @CircuitBreaker(name = "catalog-cb", fallbackMethod = "getMenuItemFallback")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMenuItemById(Long menuItemId) {
        try {
            return restTemplate.getForObject(
                    catalogServiceUrl + "/api/menu-items/" + menuItemId,
                    Map.class
            );
        } catch (RestClientException e) {
            throw new ExternalServiceException("catalog-service", e.getMessage());
        }
    }

    /**
     * FALLBACK: Se ejecuta cuando el Circuit Breaker está ABIERTO.
     * Devuelve un item genérico para que el pedido no falle completamente.
     */
    public Map<String, Object> getMenuItemFallback(Long menuItemId, Throwable ex) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("id", menuItemId);
        fallback.put("name", "Producto no disponible (catalog-service caído)");
        fallback.put("price", "0.00");
        fallback.put("available", false);
        return fallback;
    }
}
