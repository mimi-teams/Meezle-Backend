package com.mimi.w2m.backend.repository;

import com.mimi.w2m.backend.type.common.Role;
import com.mimi.w2m.backend.type.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * UserRepositoryTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
static private final Logger         logger = LogManager.getLogger(UserRepositoryTest.class);
@Autowired private   UserRepository userRepository;

@BeforeEach
void setUp() {
    final var name  = "tester";
    final var email = "tester@meezle.org";
    final var user = User.builder()
                         .name(name)
                         .email(email)
                         .build();
    userRepository.save(user);
}

@Test
void findByName() {
    //given
    final var expectedName = "tester";

    //when
    final var user = userRepository.findByName(expectedName);

    //then
    assertThat(user.isEmpty()).isFalse();
    assertThat(user.get(0).getName()).isEqualTo(expectedName);
}

@Test
void findByEmail() {
    //given
    final var expectedEmail = "tester@meezle.org";

    //when
    final var user = userRepository.findByEmail(expectedEmail);

    //then
    assertThat(user.isPresent()).isTrue();
    assertThat(user.get().getEmail()).isEqualTo(expectedEmail);
}

@Test
void update() {
    //given
    final var updatedName  = "teddy";
    final var updatedEmail = "teddy@meezle.org";
    final var user         = userRepository.findByName("tester").get(0);

    //when
    user.update(updatedName, updatedEmail);
    final var expectedUser1 = userRepository.findByName(updatedName);
    final var expectedUser2 = userRepository.findByEmail(updatedEmail);

    //then
    assertThat(expectedUser1.isEmpty()).isFalse();
    assertThat(expectedUser2.isPresent()).isTrue();
}

@Test
void checkRole() {
    //given
    final var user         = userRepository.findByName("tester").get(0);
    final var expectedRole = Role.USER;

    //when
    final var savedRole = user.getRole();

    //then
    assertThat(expectedRole).isEqualTo(savedRole);
}
}