package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.assets.MelodyRhythmPattern;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.habanec.composer3.utils.PatternStringUtils.isPatternNotUnique;
import static cz.habanec.composer3.utils.PatternStringUtils.stringyfyPattern;
import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;

@Service
@RequiredArgsConstructor
public class RhythmPatternCreator {

    private final PatternService patternService;

    @Value("${midi.resolution}")
    private final Integer DEFAULT_MIDI_RESOLUTION = 4;
    private final int MIN_RHYTHM_VALUE = 1;

    public List<MelodyRhythmPattern> createRhythmPatterns(RhythmPatternSetIngredients ingredients) {

        int beatCount = ingredients.beatCount;
        int[] granularityOptions = ingredients.granularityOptions;
        final int MAX_RHYTHM_VALUE = DEFAULT_MIDI_RESOLUTION * beatCount;

        List<String> rhythmPatternsRaw = new ArrayList<>();
        String newPattern;
        for (int i = 0; i < granularityOptions.length; i++) {

            int valueCount = ingredients.valueCountOptions[i];
            do {
                if (ingredients.eccentricOptions[i]) {
                    newPattern = createRandomRhythmPatternBySquashing(beatCount, valueCount, granularityOptions[i]);
                } else {
                    newPattern = createRandomRhythmPatternByCutting(beatCount, valueCount, granularityOptions[i]);
                }
            }
            while (isPatternNotUnique(newPattern, rhythmPatternsRaw));

            rhythmPatternsRaw.add(newPattern);
        }

        return rhythmPatternsRaw.stream()
                .map(pattern -> patternService.getOrCreateMelodyRhythmPattern(pattern, ingredients.formId))
                .toList();
    }

    public String createRandomRhythmPatternByCutting(final int beatCount, int valueCount, int granularity) {
        int length = DEFAULT_MIDI_RESOLUTION * beatCount;

        if (valueCount == 0) {
            return List.of(-length).toString();
        }
        if (valueCount > length) {
            System.out.printf("PatternCreator::createRandomRhythmPatternByCutting: "
                    + "Value count corrected from %d to %d%n", valueCount, length);
            valueCount = length;
        }

        List<Integer> list = new ArrayList<>(List.of(length));
        int valueToCutRandomIndex;
        int valueToCut;
        int optionsRandomIndex;
        int[] fragments = new int[2];

        while (list.size() < valueCount) {

            if (isNoValueFragmentable(list, granularity)) {
                granularity /= 2;
//                System.out.println("granularity at half");
            }

            valueToCutRandomIndex = RANDOM.nextInt(list.size());
            valueToCut = list.get(valueToCutRandomIndex);
            if (valueToCut == MIN_RHYTHM_VALUE || valueToCut < granularity) {
                continue;
            }

            var options = getOptionsForCutting(valueToCut, granularity);
            optionsRandomIndex = RANDOM.nextInt(options.size());
            fragments[0] = options.get(optionsRandomIndex);
            fragments[1] = valueToCut - fragments[0];

            list.remove(valueToCutRandomIndex);
            list.addAll(valueToCutRandomIndex, List.of(fragments[0], fragments[1]));
        }

        System.out.printf("PatternCreator::createRandomRhythmPatternByCutting: %s%n", list);
        return stringyfyPattern(list);
    }

    private List<Integer> getOptionsForCutting(int valueToCut, int granularity) {

        if (valueToCut == granularity) { // it is desirable to work with one level lower granularity too
            granularity /= 2;
        }
        int[] valuesEligibleForCutLine = {valueToCut / 2, granularity, valueToCut - granularity,
                granularity * 2, valueToCut - granularity * 2, granularity * 4, valueToCut - granularity * 4
        };
        int finalGranularity = granularity;
        return Arrays.stream(valuesEligibleForCutLine)
                .filter(value -> value % finalGranularity == 0 && value > 0 && value < valueToCut)
                .distinct()
                .boxed().toList();
    }

    private boolean isNoValueFragmentable(List<Integer> list, int granularity) {
        return list.stream().noneMatch(value -> value > granularity);
    }

    public String createRandomRhythmPatternBySquashing(final int beatCount, int valueCount, int granularity) {
        int length = DEFAULT_MIDI_RESOLUTION * beatCount;

        if (valueCount == 0) {
            return List.of(- length).toString();
        }
        if (valueCount > length) {
            System.out.printf("PatternCreator::createRandomRhythmPatternBySquashing: "
                    + "Value count corrected from %d to %d%n", valueCount, length);
            valueCount = length;
        }
        while (valueCount * granularity > length) {
            System.out.printf("PatternCreator::createRandomRhythmPatternBySquashing: "
                    + "Granularity corrected from %d to %d%n", granularity, granularity / 2);
            granularity /= 2;
        }

        var list = createStartingListForSquashing(length, granularity);
        while (list.size() > valueCount) {
            squashRandomValues(list);
        }
        System.out.printf("PatternCreator::createRandomRhythmPatternBySquashing: %s%n", list);
        return stringyfyPattern(list);
    }

    private void squashRandomValues(List<Integer> list) {
        int rndIndex = RANDOM.nextInt(list.size());
        int nextIndex = (rndIndex + 1 == list.size()) ? rndIndex - 1 : rndIndex + 1;
        int squashedValue = list.get(rndIndex) + list.get(nextIndex);
        list.set(rndIndex, squashedValue);
        list.remove(nextIndex);
    }

    private List<Integer> createStartingListForSquashing(int length, int granularity) {
        var list = Stream.generate(() -> granularity)
                .limit(length / granularity)
                .collect(Collectors.toList());
        int missingValue = length % granularity;
        if (missingValue != 0) {
            list.add(missingValue);
        }
        return list;
    }

    @Builder
    public static class RhythmPatternSetIngredients {
        int[] granularityOptions;
        int[] valueCountOptions;
        long formId;
        int beatCount;
        boolean[] eccentricOptions;
    }
}
