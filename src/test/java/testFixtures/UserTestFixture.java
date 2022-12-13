package testFixtures;

import com.mimi.w2m.backend.domain.User;

public class UserTestFixture {

    public static User createUser() {
        return User.builder()

                .build();
    }

}
