package cz.habanec.composer3.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void returnExpectedRandomString() {

        for (int i = 0; i < 10; i++) {
            var result = AlphabetUtils.generateRandomTwoWordsName();
            System.out.println(result);
            Assertions.assertTrue(StringUtils.hasLength(result));
            assertTrue(result.matches("[A-Z][a-z]{1,12}\s[A-Z][a-z]{1,12}"));
        }
    }

    @Test
    void returnAmbitus() {

        var result = PatternStringUtils.getTunePatternsAmbitus(List.of(-3, 0, 1));
        Assertions.assertEquals(result, 5);

        result = PatternStringUtils.getTunePatternsAmbitus(List.of(-1, 0, 1));
        Assertions.assertEquals(result, 3);

        result = PatternStringUtils.getTunePatternsAmbitus(List.of(0, 0, 0, 0));
        Assertions.assertEquals(result, 1);
    }

    @Test
    void returnShuffledCustomOptionsEvenlyBounded() {
        var result = ProbabilityUtils.getRandomAndShuffledCustomOptionsListEvenlyBounded(3, 2, 9);
        System.out.println(result);
        result = ProbabilityUtils.getRandomAndShuffledCustomOptionsListEvenlyBounded(4, 0, 100);
        System.out.println(result);
        result = ProbabilityUtils.getRandomAndShuffledCustomOptionsListEvenlyBounded(5, 5, 10);
        System.out.println(result);
        result = ProbabilityUtils.getRandomAndShuffledCustomOptionsListEvenlyBounded(10, 0, 3);
        System.out.println(result);
    }

}
