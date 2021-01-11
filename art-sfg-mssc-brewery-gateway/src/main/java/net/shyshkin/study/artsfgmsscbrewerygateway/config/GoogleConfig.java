package net.shyshkin.study.artsfgmsscbrewerygateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("google")
public class GoogleConfig {

    @Bean
    public RouteLocator googleRoute(RouteLocatorBuilder builder) {
        return builder.routes()
//                .route("google", r -> r.path("/googlesearch/**")
                .route("google", r -> r.path("/googlesearch")
                        .filters(f -> f

//                                .rewritePath("/googlesearch/(?<segment>.*)", "/${segment}") //works with localhost:9090/googlesearch/search?q=hello
                                        .rewritePath("/googlesearch", "/search") //works with localhost:9090/googlesearch?q=wtf
                        )
                        .uri("https://google.com"))
                .build();
    }
}
