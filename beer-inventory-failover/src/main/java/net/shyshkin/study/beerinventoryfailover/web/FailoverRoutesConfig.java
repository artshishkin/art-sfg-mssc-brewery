package net.shyshkin.study.beerinventoryfailover.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FailoverRoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> failoverRoutes(FailoverHandler failoverHandler) {
        return RouterFunctions.route()
                .GET("/inventory-failover", failoverHandler::listInventory)
                .build();
    }
}
