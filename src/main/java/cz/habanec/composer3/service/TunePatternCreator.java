package cz.habanec.composer3.service;

import cz.habanec.composer3.utils.PatternStringUtils;
import cz.habanec.composer3.utils.ProbabilityUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
@RequiredArgsConstructor
public class TunePatternCreator {

    private static final int TUNE_PATTERN_DEFAULT_SIZE = 9;
    private static final int STEP_RESOLVING_DEFAULT_UPPER_LIMIT = 12;
    private static final int STEP_RESOLVING_DEFAULT_LOWER_LIMIT = 7;

    private static final Random RANDOM = new Random();

    /**
     * excentricity: skoky/kroky - dava smysl pro vetsi ambitus a toneAmount, jinak ma nejnizsi prioritu
     * <p>
     * toneAmount: pocet ruznych vysek, 2-9 ambitus: maximalni/povinny vyskovy rozsah, mel by mit prioritu oproti
     * toneAmountu = ambitus 2 tony znamena snizit toneAmount na 2 (anebo naopak?) anebo ambitus zrusim ? toneAmount
     * obstara pocet a excentricity rozsah stejne porad nevim, jak dosahnu pozadovane excentricity - ona jen umoznuje
     * vetsi skoky, ale nezajistuje je
     * excentricity bude mit asi 3 stupne a podle nej budu volit metodu tvorby - vic nebo min skakavou
     */
    public String createTunePattern(int ambitus, int toneAmount, Eccentricity eccentricity) {
        //
        ambitus = max(ambitus, 2);
        toneAmount = max(toneAmount, 2);
        toneAmount = min(ambitus, toneAmount);

        List<Integer> eligibleValues = generateRandomEligibleValues(ambitus, toneAmount);
        System.out.println(eligibleValues);

        List<Integer> pattern;
        if (Eccentricity.HIGH_ECCENTRICITY.equals(eccentricity)) {
            pattern = generateRandomPatternUsingMbiraMethod(eligibleValues);
        } else {
            pattern = generateRandomPatternUsingStepMethod(eligibleValues, eccentricity);
        }

        System.out.println(pattern);
        pattern = correctPatternToBeStartingWithZero(pattern);

        return PatternStringUtils.stringyfyPattern(pattern);
    }

    private List<Integer> correctPatternToBeStartingWithZero(List<Integer> pattern) {
        if (pattern.isEmpty()) {
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

                nextStep = resolveStepFrom(ProbabilityUtils.rollDice(3), eccentricity.getValue());
                nextStep = upwards ? nextStep : -nextStep;

            }
            if (new HashSet<>(pattern).containsAll(eligibleValues)) {
                System.out.println("Success at " + attempt + ". attempt");
                break;
            }
        }
        return pattern;
    }


    private int resolveStepFrom(int threeDiceRoll, int eccentricity) {
        int upperLimit = STEP_RESOLVING_DEFAULT_UPPER_LIMIT - eccentricity;
        int lowerLimit = STEP_RESOLVING_DEFAULT_LOWER_LIMIT + eccentricity;
        int step = 1;
        if (threeDiceRoll > upperLimit) {
            step = threeDiceRoll - upperLimit;
        } else if (threeDiceRoll < lowerLimit) {
            step = lowerLimit - threeDiceRoll;
        }
        System.out.println("For roll " + threeDiceRoll + " step: " + step);
        return step;
    }


    /**
     * done vkladam pocet rytmickych hodnot a hustotu jednicky budu squashovat, ale ne whileloopou počet iterací =
     * valuesCount - 1 squashuju pouze if rnd(100) < density, jinak continue; při padesátce je tedy šance, že bude
     * repetováno vše i nic, ale pravděpodobně to bude v odpovídajícím poměru ještě promysli if rnd(100) * 2 < density,
     * tak squashne rovnou tři hodnoty a tak dál... tzn. nízký random při vysoké density vytvoří morseovku --- co pak
     * ale s přebytečnými iteracemi...(navýšit) raději rnd(100) < density / 2 ? anebo bych radeji mel resit pocet
     * zbyvajicich iteraci - oboji
     */
    public String createRepetitionPattern(int valuesCount, int density) {
        if (density == 0) {
            return "";
        } else if (density == 100) {
            return "0_" + valuesCount;
        }
        List<Integer> repetitions = Stream.generate(() -> 1).limit(valuesCount).collect(Collectors.toList());
        var rnd = new Random();
        int amount;
        int rndIndexToSquash;
        for (int remains = valuesCount; remains > 1; remains--) {
            amount = min(remains, evaluateRepetitions(rnd.nextInt(101), density, valuesCount));
            if (amount == 1) {
                continue;
            }
            rndIndexToSquash = rnd.nextInt(repetitions.size() - amount + 1);
            squashMultipleValues(repetitions, rndIndexToSquash, amount);
            remains -= --amount;
        }
        System.out.printf("TunePatternCreator::createRepetitionPattern: for %d values in density of %d created %s%n",
                valuesCount, density, repetitions);
        return PatternStringUtils.stringifyRepetitionsPattern(repetitions);
    }

    private int evaluateRepetitions(int random, int density, int valuesCount) {
        int amount = 1;
        if (random < density) {
            amount++;
        }
        if (random < density * 0.33 && random < density - 33) {
            amount++;
        }
        if (random < density * 0.16 && random < density - 50) {
            amount++;
        }
        if (random < density * 0.04 && random < density - 66) {
            amount += 2;
        }
        if (valuesCount < 4) { // correction for small amount of values
            amount = min(amount, 2);
        }
        return amount;
    }

    private void squashMultipleValues(List<Integer> list, int rndIndex, int amount) {
        int squashedValue;
        for (int i = 1; i < amount; i++) {
            squashedValue = list.get(rndIndex) + list.get(rndIndex + 1);
            list.set(rndIndex, squashedValue);
            list.remove(rndIndex + 1);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Eccentricity {
        NO_ECCENTRICITY(0), LOW_ECCENTRICITY(1), MID_ECCENTRICITY(2), HIGH_ECCENTRICITY(3);
        private final int value;
    }
}

