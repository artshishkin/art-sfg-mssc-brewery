package net.shyshkin.study.artsfgmsscconfigserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {"spring.cloud.config.server.git.clone-on-start=false"}
)
class ArtSfgMsscConfigServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
