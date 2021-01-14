package net.shyshkin.study.artsfgmsscconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ArtSfgMsscConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtSfgMsscConfigServerApplication.class, args);
    }

}
