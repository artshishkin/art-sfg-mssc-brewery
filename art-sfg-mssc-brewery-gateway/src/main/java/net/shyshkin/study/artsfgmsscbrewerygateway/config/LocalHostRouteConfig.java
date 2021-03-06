package net.shyshkin.study.artsfgmsscbrewerygateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local-discovery")
public class LocalHostRouteConfig {

    @Bean
    public RouteLocator localHostRoutes(RouteLocatorBuilder builder,
                                        @Value("${net.shyshkin.routes.beer-service}") final String beerServiceUri,
                                        @Value("${net.shyshkin.routes.beer-inventory-service}") final String beerInventoryServiceUri,
                                        @Value("${net.shyshkin.routes.beer-order-service}") final String beerOrderServiceUri) {
        return builder.routes()
                .route("beer-service",
                        r -> r.path("/api/v1/beer", "/api/v1/beer/*", "/api/v1/beerUpc/*")
                                .uri(beerServiceUri))
                .route("order-service",
                        r -> r.path("/api/v1/customers", "/api/v1/customers/**")
                                .uri(beerOrderServiceUri))
                .route("inventory-service",
                        r -> r.path("/api/v1/beer/*/inventory")
                                .uri(beerInventoryServiceUri))
                .build();
    }
}
