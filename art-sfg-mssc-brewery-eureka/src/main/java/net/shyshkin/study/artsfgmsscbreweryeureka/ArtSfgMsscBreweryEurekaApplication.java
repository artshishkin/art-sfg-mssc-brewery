package net.shyshkin.study.artsfgmsscbreweryeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ArtSfgMsscBreweryEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtSfgMsscBreweryEurekaApplication.class, args);
    }

}
