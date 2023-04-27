package cz.habanec.composer3.utils;

import lombok.experimental.UtilityClass;

import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;

@UtilityClass
public class AlphabetUtils {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String LITERALS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static final String SCRABBLE_FREQUENCY_WOVELS = "aaeeiioouy";
    public static final String SCRABBLE_FREQUENCY_CONSONANTS = "bcccdddfghhjjkkklllmmnnnpppqrrrssstttvvwxyzz";
    private static final int TWO_WORDS_NAME_MAX_LENGTH = 17;
    private static final int TWO_WORDS_NAME_MIN_LENGTH = 7;

    public static String cleanUpString(String string) {
        return string.replaceAll("[^A-Za-z]","");
    }

    public static String generateRandomTwoWordsName() {

        int length = RANDOM.nextInt(TWO_WORDS_NAME_MIN_LENGTH, TWO_WORDS_NAME_MAX_LENGTH);
        int spaceIndex = RANDOM.nextInt(2, length - 2);

        StringBuilder sb = new StringBuilder();
        sb.append(generateRandomName(spaceIndex)).append(' ').append(generateRandomName(length - spaceIndex));

        return sb.toString();
    }

    public static String generateRandomName(int length) {
        StringBuilder sb = new StringBuilder();
        int startsWithVocal = RANDOM.nextInt(2);
        for (int i = 0; i < length; i++) {
            char newCh;
            if (i % 2 == startsWithVocal) {
                newCh = SCRABBLE_FREQUENCY_CONSONANTS.charAt(RANDOM.nextInt(SCRABBLE_FREQUENCY_CONSONANTS.length()));
            } else {
                newCh = SCRABBLE_FREQUENCY_WOVELS.charAt(RANDOM.nextInt(SCRABBLE_FREQUENCY_WOVELS.length()));
            }
            if (i == 0) {
                newCh = Character.toUpperCase(newCh);
            }
            sb.append(newCh);
        }
        return sb.toString();
    }

}
