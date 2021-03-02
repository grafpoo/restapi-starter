package liveproject.webreport;

import liveproject.webreport.config.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestWebConfig.class})
class WebreportBootifiedApplicationTests {

    @Test
    void contextLoads() {
    }

}
