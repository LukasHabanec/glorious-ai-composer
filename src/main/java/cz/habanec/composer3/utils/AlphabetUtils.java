package cz.habanec.composer3.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class AlphabetUtils {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String LITERALS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static final String SCRABBLE_FREQUENCY_WOVELS = "aaeeiioouy";
    public static final String SCRABBLE_FREQUENCY_CONSONANTS = "bcccdddfghhjjkkklllmmnnnpppqrrrssstttvvwxyzz";

    public static String cleanUpString(String string) {
        return string.replaceAll("[^A-Za-z]","");
    }

    public static String setHash(int length) {

        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomString.append(LITERALS.charAt((int) (Math.random() * LITERALS.length())));
        }
        return randomString.toString();
    }

    public static String generateRandomName() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(7,17);
        int spaceIndex = random.nextInt(2, length - 2);
        int startsWithVocal = random.nextInt(2);
        for (int i = 0; i < length; i++) {
            char newCh;
            if (i == spaceIndex) {
                newCh = ' ';
                startsWithVocal = random.nextInt(2);
            } else if (i % 2 == startsWithVocal) {
                newCh = SCRABBLE_FREQUENCY_CONSONANTS.charAt(random.nextInt(SCRABBLE_FREQUENCY_CONSONANTS.length()));
            } else {
                newCh = SCRABBLE_FREQUENCY_WOVELS.charAt(random.nextInt(SCRABBLE_FREQUENCY_WOVELS.length()));
            }
            if (i == 0 || i == spaceIndex + 1) {
                newCh = Character.toUpperCase(newCh);
            }
            sb.append(newCh);
        }
        return sb.toString();
    }

}
