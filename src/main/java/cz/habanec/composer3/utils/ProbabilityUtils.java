package cz.habanec.composer3.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class ProbabilityUtils {

    public static int rollDice(int amount) {
        Random rnd = new Random();
        int result = 0;
        for (int i = 0; i < amount; i++) {
            result += rnd.nextInt(1, 7);
        }
        return result;
    }
}
