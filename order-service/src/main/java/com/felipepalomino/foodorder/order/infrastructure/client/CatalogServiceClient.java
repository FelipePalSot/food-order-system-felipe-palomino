package com.felipepalomino.foodorder.order.infrastructure.client;

import com.felipepalomino.foodorder.order.domain.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * INFRAESTRUCTURA - Cliente: Comunicación sincrónica con Catalog Service.
 * Patrón de referencia: product-service → user-service en el proyecto base.
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
     * Obtiene un item de menú por ID.
     * Retorna un Map con los campos: id, name, price, restaurantId, available
     */
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
}

