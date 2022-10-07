package com.mimi.w2m.backend.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 사용자_생성하기(){
        //given
        var expectedName = "teddy";
        var expectedEmail = "teddy@super.com";

        //when
        userRepository.save(User.builder()
                .name(expectedName)
                .email(expectedEmail)
                .build());
        var user = userRepository.findAll().get(0);

        //then
        assertThat(user.getName()).isEqualTo(expectedName);
        assertThat(user.getEmail()).isEqualTo(expectedEmail);
    }

    @Test
    void 사용자_수정하기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        var expectedName = "bear";
        var expectedEmail = "bear@super.com";
        userRepository.save(user);

        //when
        user.update(expectedName, expectedEmail);
        var expectedUser = userRepository.findAll().get(0);

        //then
        assertThat(expectedUser.getName()).isEqualTo(expectedName);
        assertThat(expectedUser.getEmail()).isEqualTo(expectedEmail);
    }

    @Test
    void 사용자_제거하기() {
        //given
        var user = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        userRepository.save(user);

        //when
        userRepository.delete(user);
        var userList = userRepository.findAll();

        //then
        assertThat(userList.isEmpty());
    }

    @Test
    void 사용자_가져오기() {
        //given
        var user1 = User.builder()
                .name("teddy")
                .email("teddy@super.com")
                .build();
        var user2 = User.builder()
                .name("bear")
                .email("bear@super.com")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        //when
        var expectedUser1 = userRepository.findByName("teddy");
        var expectedUser2 = userRepository.findByEmail("bear@super.com");

        //then
        assertThat(expectedUser1.get()).isEqualTo(user1);
        assertThat(expectedUser2.get()).isEqualTo(user2);
    }
}