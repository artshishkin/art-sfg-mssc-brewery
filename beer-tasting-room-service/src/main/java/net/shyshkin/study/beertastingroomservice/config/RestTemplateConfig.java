package net.shyshkin.study.beertastingroomservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    @Profile({"local-discovery","digitalocean","play-with-docker"})
    public RestTemplate loadBalanced(RestTemplateBuilder builder){
        return builder.build();
    }

    @Bean
    @Profile("!local-discovery & !digitalocean & !play-with-docker")
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
