package cz.habanec.composer3.service;

import cz.habanec.composer3.utils.PatternStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
@RequiredArgsConstructor
public class TunePatternCreator {

    private static final Integer TUNE_PATTERN_DEFAULT_SIZE = 9;


    /** excentricity: skoky/kroky - dava smysl pro vetsi ambitus a toneAmount, jinak ma nejnizsi prioritu

    toneAmount: pocet ruznych vysek, 2-9
    * ambitus: maximalni/povinny vyskovy rozsah, mel by mit prioritu oproti toneAmountu
    * = ambitus 2 tony znamena snizit toneAmount na 2 (anebo naopak?)
    * anebo ambitus zrusim ? toneAmount obstara pocet a excentricity rozsah
     * stejne porad nevim, jak dosahnu pozadovane excentricity - ona jen umoznuje vetsi skoky, ale nezajistuje
     */
    public String createTunePattern(int excentricity, int toneAmount, int ambitus) {
        toneAmount = 3;
        excentricity = 6;
        List<Integer> eligibleValues = new ArrayList<>();
        int rndValue;
        Random rnd = new Random();
        for (int i = 0; i < toneAmount - 2; i++) { // minus 2 bo prvni a posledni tam budou urcite
            rndValue = rnd.nextInt(1, excentricity);
            eligibleValues.add(rndValue);
        } // musim poresit, jak tam dostat jen unikaty
        eligibleValues.addAll(List.of(0, excentricity));
        System.out.println(eligibleValues);
        return "";
    }



    /** done vkladam pocet rytmickych hodnot a hustotu
    * jednicky budu squashovat, ale ne whileloopou
    * počet iterací = valuesCount - 1
    * squashuju pouze if rnd(100) < density, jinak continue;
    * při padesátce je tedy šance, že bude repetováno vše i nic, ale pravděpodobně to bude v odpovídajícím poměru
    * ještě promysli if rnd(100) * 2 < density, tak squashne rovnou tři hodnoty a tak dál...
    * tzn. nízký random při vysoké density vytvoří morseovku --- co pak ale s přebytečnými iteracemi...(navýšit)
    * raději rnd(100) < density / 2 ?
    * anebo bych radeji mel resit pocet zbyvajicich iteraci - oboji
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
}

