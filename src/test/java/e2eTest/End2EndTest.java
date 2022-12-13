package e2eTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import testFixtures.DatabaseCleaner;

/**
 * End2End Test 최상위 함수
 *
 * @since 2022-12-13
 * @author paul
 */

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
public abstract class End2EndTest {

    @Autowired
    protected MockMvc mockMvc;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @AfterEach
    protected void cleanup() {
        databaseCleaner.cleanUp();
    }
}
