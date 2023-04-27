package cz.habanec.composer3.creators;

import cz.habanec.composer3.entities.enums.Eccentricity;
import cz.habanec.composer3.utils.AlphabetUtils;
import cz.habanec.composer3.utils.ProbabilityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;

@Component
@RequiredArgsConstructor
public class CompositionFormCreator {

    public String createRandomLetterScheme(int schemeLength, int letterCount, Eccentricity eccentricity) {

        if (Eccentricity.HIGH.equals(eccentricity)) {
            return generateRandomLetterSchemeUsingMbiraMethod(schemeLength, letterCount);
        }
        return generateRandomLetterSchemeUsingStepMethod(schemeLength, letterCount, eccentricity.getValue());
    }

    private String generateRandomLetterSchemeUsingMbiraMethod(
            int schemeLength, int letterCount
    ) {
        StringBuilder newScheme = new StringBuilder("A");
        String schemeLetterMatrix = createAlphabetMatrix(letterCount);
        int newLetterIndex;
        List<Character> unusedLetters = schemeLetterMatrix.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

        while (newScheme.length() < schemeLength) {

            // ensures all the letters are used
            if (schemeLength - newScheme.length() == unusedLetters.size()) {
                newLetterIndex = RANDOM.nextInt(1, newScheme.length());
                char thisChar = unusedLetters.get(0);
                newScheme.insert(newLetterIndex, thisChar);
                unusedLetters.remove(Character.valueOf(thisChar));
                continue;
            }

            newLetterIndex = RANDOM.nextInt(schemeLetterMatrix.length());
            char newLetter = schemeLetterMatrix.charAt(newLetterIndex);
            newScheme.append(newLetter);
            if (unusedLetters.contains(newLetter)) {
                unusedLetters.remove(Character.valueOf(newLetter));
            }

        }
        System.out.println("Generated new letter scheme by mbira method: " + newScheme);
        return newScheme.toString();
    }

    public String generateRandomLetterSchemeUsingStepMethod(
            int schemeLength, int letterCount, int eccentricity
    ) {
        letterCount = Math.min(schemeLength, letterCount);
        String schemeLetterMatrix = createAlphabetMatrix(letterCount);
        final int startingIndex = schemeLetterMatrix.indexOf('A');
        StringBuilder newScheme = new StringBuilder();
        List<Character> unusedLetters = schemeLetterMatrix.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

        int newLetterIndex = startingIndex;
        int step = 0;
        boolean upwards = RANDOM.nextBoolean();

        while (newScheme.length() < schemeLength) {

            // ensures all the letters are used
            if (schemeLength - newScheme.length() == unusedLetters.size()) {
                newLetterIndex = RANDOM.nextInt(1, newScheme.length());
                char thisChar = unusedLetters.get(0);
                newScheme.insert(newLetterIndex, thisChar);
                unusedLetters.remove(Character.valueOf(thisChar));
                continue;
            }

            newLetterIndex += step;
            if (newLetterIndex >= schemeLetterMatrix.length() - 1) {
                newLetterIndex = schemeLetterMatrix.length() - 1;
                upwards = false;
            } else if (newLetterIndex <= 0) {
                newLetterIndex = 0;
                upwards = true;
            }
            if (eccentricity == 0 && RANDOM.nextInt(2) != 0) {
                newLetterIndex = startingIndex;
            }

            char newLetter = schemeLetterMatrix.charAt(newLetterIndex);
            newScheme.append(newLetter);
            if (unusedLetters.contains(newLetter)) {
                unusedLetters.remove(Character.valueOf(newLetter));
            }

            step = ProbabilityUtils.resolveStepByRollingThreeDice(eccentricity) - 1; // nejmene 0
            step = upwards ? step : -step;
        }
        System.out.println("Generated new letter scheme by step method: " + newScheme);
        return newScheme.toString();
    }

    private static String createAlphabetMatrix(int letterCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letterCount; i++) {
            if (i % 2 == 0) {
                sb.append(AlphabetUtils.ALPHABET.charAt(i));
            } else {
                sb.insert(0, AlphabetUtils.ALPHABET.charAt(i));
            }
        }
        return sb.toString();
    }
}
