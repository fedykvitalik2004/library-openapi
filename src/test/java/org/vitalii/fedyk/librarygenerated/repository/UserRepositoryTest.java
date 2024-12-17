package org.vitalii.fedyk.librarygenerated.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.vitalii.fedyk.librarygenerated.api.dto.FullName;
import org.vitalii.fedyk.librarygenerated.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    void existsByEmailAndIdNot_shouldReturnFalseWhenEmailMatchesAndIdIsSame() {
        final String email = "email@mail.com";
        final User user = userRepository.save(
                new User()
                        .setFullName(new FullName("John", "Doe"))
                        .setBirthday(LocalDate.of(2004, 12, 12))
                        .setEmail(email)
                        .setPassword("password"));
        assertFalse(userRepository.existsByEmailAndIdNot(email, user.getId()));
    }

    @ParameterizedTest
    @CsvSource({
            "email1@mail.com,1,false",
            "email@mail.com,-1,true"
    })
    void existsByEmailAndIdNot_shouldReturnExpectedResultForEmailAndIdCombination(String email, Long id, boolean expected) {
        final User user = userRepository.save(
                new User()
                        .setEmail("email@mail.com")
                        .setFullName(new FullName("John", "Doe"))
                        .setBirthday(LocalDate.of(2004, 12, 12))
                        .setEmail(email)
                        .setPassword("password"));
        assertEquals(expected, userRepository.existsByEmailAndIdNot(email, id));
    }
}