package cz.habanec.composer3.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class AlphabetUtilsTest {

    @Test
    void returnExpectedRandomString() {

        for (int i = 0; i < 10; i++) {
            var result = AlphabetUtils.generateRandomName();
            System.out.println(result);
            Assertions.assertTrue(StringUtils.hasLength(result));
            assertTrue(result.matches("[A-Z][a-z]{1,12}\s[A-Z][a-z]{1,12}"));
        }
    }

}