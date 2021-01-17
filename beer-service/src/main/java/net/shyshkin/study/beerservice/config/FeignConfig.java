package net.shyshkin.study.beerservice.config;

import feign.Feign;
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
}
