package cz.habanec.composer3.creators;

import cz.habanec.composer3.entities.enums.Eccentricity;
import cz.habanec.composer3.utils.ProbabilityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
@RequiredArgsConstructor
public class TunePatternCreator {

    private static final int TUNE_PATTERN_DEFAULT_SIZE = 9;

    public List<Integer> createOneTunePattern(int ambitus, int toneAmount, Eccentricity eccentricity) {
        //
        ambitus = max(ambitus, 2);
        toneAmount = max(toneAmount, 2);
        toneAmount = min(ambitus, toneAmount);

        List<Integer> eligibleValues = generateRandomEligibleValues(ambitus, toneAmount);
        System.out.println("TunePatternCreator::createOneTunePattern: randomly generated eligible values:" + eligibleValues);

        List<Integer> pattern;
        if (Eccentricity.HIGH.equals(eccentricity)) {
            pattern = generateRandomPatternUsingMbiraMethod(eligibleValues);
        } else {
            pattern = generateRandomPatternUsingStepMethod(eligibleValues, eccentricity);
        }
        System.out.println("TunePatternCreator::createOneTunePattern: " + pattern);

        return sanitizePatternToBeStartingWithZero(pattern);
    }

    private List<Integer> sanitizePatternToBeStartingWithZero(List<Integer> pattern) {
        if (pattern.isEmpty() || pattern.get(0) == 0) {
            return pattern;
        }
        int firstValue = pattern.get(0);
        return pattern.stream().map(value -> value - firstValue).collect(Collectors.toList());
    }

    private List<Integer> generateRandomEligibleValues(int ambitus, int toneAmount) {
        var eligibleValues = Stream.iterate(0, n -> ++n).limit(ambitus).collect(Collectors.toList());
        int rndIndex;
        while (eligibleValues.size() > toneAmount) {
            rndIndex = RANDOM.nextInt(1, eligibleValues.size() - 1); // remove any except first and last
            eligibleValues.remove(rndIndex);
        }
        return eligibleValues;
    }

    private List<Integer> generateRandomPatternUsingMbiraMethod(List<Integer> eligibleValues) {

        List<Integer> pattern = new ArrayList<>();
        List<Integer> unusedValues = new ArrayList<>(eligibleValues);
        int toneAmount = eligibleValues.size();
        int chosenValue;
        int lastValue = -1;
        int unusedValuesSize;
        for (int i = 0; i < TUNE_PATTERN_DEFAULT_SIZE; i++) {

            unusedValuesSize = unusedValues.size();
            if (TUNE_PATTERN_DEFAULT_SIZE - i == unusedValuesSize) {
                chosenValue = unusedValues.get(RANDOM.nextInt(unusedValuesSize)); // use all eligible values
            } else {
                do {
                    chosenValue = eligibleValues.get(RANDOM.nextInt(toneAmount));
                } while (chosenValue == lastValue); // allow no repetitions
            }

            pattern.add(chosenValue);
            if (unusedValues.contains(chosenValue)) {
                unusedValues.remove((Integer) chosenValue);
            }

            lastValue = chosenValue;
        }
        return pattern;
    }

    private List<Integer> generateRandomPatternUsingStepMethod(List<Integer> eligibleValues,
                                                               Eccentricity eccentricity) {
        List<Integer> pattern = new ArrayList<>();
        int eligibleValuesSize = eligibleValues.size();

        boolean upwards = RANDOM.nextBoolean();
        int newToneIndex = RANDOM.nextInt(eligibleValuesSize);
        int nextStep = 0;
        for (int attempt = 1; attempt < 4; attempt++) { // dej tomu 3 sance, aby byl plny ambitus, jinak salam

            pattern.clear();
            for (int i = 0; i < TUNE_PATTERN_DEFAULT_SIZE; i++) {

                newToneIndex += nextStep;
                if (newToneIndex >= eligibleValuesSize - 1) {
                    newToneIndex = eligibleValuesSize - 1;
                    upwards = false;
                } else if (newToneIndex <= 0) {
                    newToneIndex = 0;
                    upwards = true;
                } else if (nextStep > 1 && RANDOM.nextInt(3) != 0) {
                    upwards = !upwards;
                }

                pattern.add(eligibleValues.get(newToneIndex));

                nextStep = ProbabilityUtils.resolveStepByRollingThreeDice(eccentricity.getValue());
                nextStep = upwards ? nextStep : -nextStep;

            }
            if (new HashSet<>(pattern).containsAll(eligibleValues)) {
//                System.out.println("Success at " + attempt + ". attempt");
                break;
            }
        }
        return pattern;
    }

}
