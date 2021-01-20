package net.shyshkin.study.beerservice.config;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "net.shyshkin.study.beerservice.services")
public class FeignConfig {

    @Bean
    public Feign.Builder circuitBreakerFeignBuilder() {
        return FeignCircuitBreaker.builder();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${net.shyshkin.client.inventory-user}") String inventoryUser,
                                                                   @Value("${net.shyshkin.client.inventory-password}") String inventoryPassword){
        return new BasicAuthRequestInterceptor(inventoryUser, inventoryPassword);
    }
}
