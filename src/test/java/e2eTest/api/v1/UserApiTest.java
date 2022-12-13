package e2eTest.api.v1;

import com.mimi.w2m.backend.domain.User;
import com.mimi.w2m.backend.repository.UserRepository;
import e2eTest.End2EndTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testFixtures.UserTestFixture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings({"unused", "NonAsciiCharacters"})
public class UserApiTest extends End2EndTest {

    @Autowired
    protected UserRepository userRepository;


    @Test
    void 이용자_정보_반환() throws Exception {
        // given
        User user = UserTestFixture.createUser("가나다");
        userRepository.save(user);

        //when & then
        mockMvc.perform(get("/v1/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.name").value(user.getEmail()))
        ;


    }

}
