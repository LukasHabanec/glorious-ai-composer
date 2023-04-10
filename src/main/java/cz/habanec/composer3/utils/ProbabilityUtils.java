package cz.habanec.composer3.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class ProbabilityUtils {

    public static final Random RANDOM = new Random();

    public static int rollDice(int amount) {
        int result = 0;
        for (int i = 0; i < amount; i++) {
            result += RANDOM.nextInt(1, 7);
        }
        return result;
    }
}
