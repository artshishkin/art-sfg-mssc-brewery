package net.shyshkin.study.beerorderservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

//    @Bean
//    @LoadBalanced
//    @Profile("local-discovery")
    public RestTemplate loadBalanced(RestTemplateBuilder builder){
        return builder.build();
    }

    @Bean
//    @Profile("!local-discovery")
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
