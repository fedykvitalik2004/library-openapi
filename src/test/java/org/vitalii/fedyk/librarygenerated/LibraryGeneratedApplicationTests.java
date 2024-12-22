package org.vitalii.fedyk.librarygenerated;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = LibraryGeneratedApplication.class)
class LibraryGeneratedApplicationTests {

    @Test
    void contextLoads() {
        assertAll(() -> Assertions.assertEquals(0, 0));
    }

}
