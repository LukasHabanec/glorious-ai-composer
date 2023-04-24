package cz.habanec.composer3.creators;

import cz.habanec.composer3.entities.MelodyRhythmPattern;
import cz.habanec.composer3.entities.assets.TimeSignature;
import cz.habanec.composer3.entities.enums.NoteLength;
import cz.habanec.composer3.service.NoteLengthHelper;
import cz.habanec.composer3.service.PatternService;
import cz.habanec.composer3.utils.PatternStringUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.habanec.composer3.utils.PatternStringUtils.*;
import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;

@Service
@RequiredArgsConstructor
public class RhythmPatternCreator {

    private final PatternService patternService;

    public List<MelodyRhythmPattern> createRhythmPatternsSet(RhythmPatternSetIngredients ingredients) {

        var valuesCountOptions = ingredients.valueCountOptions;
        var timeSignature = ingredients.timeSignature;
        var granularityOptions = ingredients.granularityOptions;

        List<List<Integer>> rhythmPatternsRaw = new ArrayList<>();
        List<Integer> newPattern;
        for (int i = 0; i < granularityOptions.length; i++) {

            do {
                if (ingredients.eccentricOptions[i]) {
                    newPattern = createRandomRhythmPatternBodyBySquashing(
                            timeSignature.getMidiLength(), valuesCountOptions[i], granularityOptions[i].getMidiValue());
                } else {
                    newPattern = createRandomRhythmPatternBodyByCutting(
                            timeSignature.getMidiLength(), valuesCountOptions[i], granularityOptions[i].getMidiValue());
                }
            }
            while (isPatternNotUnique(newPattern, rhythmPatternsRaw));

            rhythmPatternsRaw.add(newPattern);
        }

        return Stream.iterate(0, n -> ++n).limit(rhythmPatternsRaw.size())
                .map(index -> {
                    var pattern = rhythmPatternsRaw.get(index);
                    return patternService.fetchOrReturnMelodyRhythmPattern(MelodyRhythmPattern.builder()
                            .body(convertRhythmValuesToLabelsAndStringify(pattern))
                            .rndGenerated(true)
                            .timeSignature(timeSignature)
                            .granularity(PatternStringUtils.extractShortestNoteLength(pattern))
                            .valuesCount(pattern.size())
                            .formAssociationId(ingredients.formId)
                            .build());
                })
                .toList();
    }

    public List<Integer> createRandomRhythmPatternBodyByCutting(final int length, int valueCount, int granularity) {

        if (valueCount == 0) {
            return List.of(-length);
        }
        valueCount = Math.min(length, valueCount);

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
            if (valueToCut == NoteLengthHelper.MIN_RHYTHM_VALUE || valueToCut < granularity) {
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
        return list;
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

    public List<Integer> createRandomRhythmPatternBodyBySquashing(final int length, int valueCount, int granularity) {

        if (valueCount == 0) {
            return List.of(-length);
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
        return list;
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
        NoteLength[] granularityOptions;
        int[] valueCountOptions;
        long formId;
        TimeSignature timeSignature;
        boolean[] eccentricOptions;
    }
}
