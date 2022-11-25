package com.mimi.w2m.backend.domain.converter;

import com.mimi.w2m.backend.domain.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * RoleConverterTest
 *
 * @author teddy
 * @version 1.0.0
 * @since 2022/11/24
 **/
class RoleConverterTest {
private final RoleConverter converter = new RoleConverter();

@DisplayName("ROLE TYPE -> ROLE STR 변환")
@Test
void convertToDatabaseColumn() {
    //given
    final var expectedUserString        = Role.USER.getKey();
    final var expectedParticipantString = Role.PARTICIPANT.getKey();
    final var expectedNoneString        = Role.NONE.getKey();

    //when
    final var givenUserString        = converter.convertToDatabaseColumn(Role.USER);
    final var givenParticipantString = converter.convertToDatabaseColumn(Role.PARTICIPANT);
    final var givenNoneString        = converter.convertToDatabaseColumn(Role.NONE);

    //then
    assertThat(expectedUserString).isEqualTo(givenUserString);
    assertThat(expectedParticipantString).isEqualTo(givenParticipantString);
    assertThat(expectedNoneString).isEqualTo(givenNoneString);
}

@DisplayName("ROLE STR -> ROLE TYPE 변환")
@Test
void convertToEntityAttribute() {
    //given
    final var expectedUserRole        = Role.USER;
    final var expectedParticipantRole = Role.PARTICIPANT;
    final var expectedNoneRole        = Role.NONE;

    //when
    final var givenUserRole        = converter.convertToEntityAttribute("ROLE_USER");
    final var givenParticipantRole = converter.convertToEntityAttribute("ROLE_PARTICIPANT");
    final var givenNoneRole        = converter.convertToEntityAttribute("ROLE_NONE");

    //then
    assertThat(expectedUserRole).isEqualTo(givenUserRole);
    assertThat(expectedParticipantRole).isEqualTo(givenParticipantRole);
    assertThat(expectedNoneRole).isEqualTo(givenNoneRole);
}
}