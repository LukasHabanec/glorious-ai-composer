package cz.habanec.composer3.creators;

import cz.habanec.composer3.utils.PatternStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;
import static java.lang.Math.min;

@Component
public class RepetitionPatternCreator {

    /**
     * done vkladam pocet rytmickych hodnot a hustotu jednicky budu squashovat, ale ne whileloopou počet iterací =
     * valuesCount - 1 squashuju pouze if rnd(100) < density, jinak continue; při padesátce je tedy šance, že bude
     * repetováno vše i nic, ale pravděpodobně to bude v odpovídajícím poměru ještě promysli if rnd(100) * 2 < density,
     * tak squashne rovnou tři hodnoty a tak dál... tzn. nízký random při vysoké density vytvoří morseovku --- co pak
     * ale s přebytečnými iteracemi...(navýšit) raději rnd(100) < density / 2 ? anebo bych radeji mel resit pocet
     * zbyvajicich iteraci - oboji
     */
    public String createOneRepetitionPattern(int valuesCount, int density) {
        if (density == 0) {
            return "";
        } else if (density == 100) {
            return "0_" + valuesCount;
        }
        List<Integer> repetitions = Stream.generate(() -> 1).limit(valuesCount).collect(Collectors.toList());

        int amount;
        for (int remains = valuesCount; remains > 1; remains--) {
            amount = min(remains, evaluateDiceRollAndDensityForRepetitions(
                    RANDOM.nextInt(101), density, valuesCount));
            if (amount == 1) {
                continue;
            }
            squashMultipleValues(repetitions, amount);
            remains -= --amount;
        }
        System.out.printf("TunePatternCreator::createRepetitionPattern: for %d values in density of %d created %s%n",
                valuesCount, density, repetitions);
        return PatternStringUtils.stringifyRepetitionsPattern(repetitions);
    }

    private int evaluateDiceRollAndDensityForRepetitions(int random, int density, int valuesCount) {
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

    private void squashMultipleValues(List<Integer> list, int amount) {
        int rndIndex = RANDOM.nextInt(list.size() - amount + 1);
        int squashedValue;
        for (int i = 1; i < amount; i++) {
            squashedValue = list.get(rndIndex) + list.get(rndIndex + 1);
            list.set(rndIndex, squashedValue);
            list.remove(rndIndex + 1);
        }
    }
}
