package cz.habanec.composer3.service;

import cz.habanec.composer3.creators.RepetitionPatternCreator;
import cz.habanec.composer3.creators.RhythmPatternCreator;
import cz.habanec.composer3.creators.TunePatternCreator;
import cz.habanec.composer3.entities.MelodyRhythmPattern;
import cz.habanec.composer3.entities.MelodyTunePattern;
import cz.habanec.composer3.entities.assets.Pattern;
import cz.habanec.composer3.entities.assets.TimeSignature;
import cz.habanec.composer3.entities.enums.Eccentricity;
import cz.habanec.composer3.repositories.MelodyRhythmPatternRepo;
import cz.habanec.composer3.repositories.MelodyTunePatternRepo;
import cz.habanec.composer3.utils.PatternStringUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.habanec.composer3.utils.PatternStringUtils.convertRhythmValuesToLabelsAndStringify;
import static cz.habanec.composer3.utils.PatternStringUtils.isPatternNotUnique;

@Service
@RequiredArgsConstructor
public class PatternService {

    private final MelodyRhythmPatternRepo rhythmPatternRepo;
    private final MelodyTunePatternRepo tunePatternRepo;
    private final RepetitionPatternCreator repetitionPatternCreator;
    private final TunePatternCreator tunePatternCreator;
    private final RhythmPatternCreator rhythmPatternCreator;

    @Transactional
    public MelodyRhythmPattern fetchOrReturnMelodyRhythmPattern(MelodyRhythmPattern pattern) {
        return rhythmPatternRepo.findByBody(pattern.getBody())
                .orElse(pattern);

    }

    @Transactional
    public MelodyTunePattern fetchOrReturnMelodyTunePattern(MelodyTunePattern pattern) {
        return tunePatternRepo.findByBody(pattern.getBody())
                .orElse(pattern);

    }

    public List<String> collectCreatedRepetitionPatternSet(
            List<MelodyRhythmPattern> rhythmPatterns,
            List<Integer> densityOptions
    ) {
        return Stream.iterate(0, n -> ++n).limit(rhythmPatterns.size())
                .map(index -> repetitionPatternCreator.createOneRepetitionPattern(
                        rhythmPatterns.get(index).getValues().size(),
                        densityOptions.get(index)))
                .collect(Collectors.toList());
    }


    public <T extends Pattern> List<T> getRequiredPatternListUsingShuffledIndexes(
            List<Integer> shuffledIndexes, List<T> patterns) {

        List<T> reconstructedPatternList = new ArrayList<>();
        for (int i = 0; i < shuffledIndexes.size(); i++) {
            var selectedIndex = shuffledIndexes.get(i);
            reconstructedPatternList.add(patterns.get(selectedIndex));
        }
        return reconstructedPatternList;
    }

    public List<MelodyRhythmPattern> createRhythmPatternsSet(RhythmPatternSetIngredients ingredients) {

        var valuesCountOptions = ingredients.valueCountOptions;
        var timeSignature = ingredients.timeSignature;
        var granularityOptions = NoteLengthHelper.extractNoteLengthListFrom(
                ingredients.granularityOptions, timeSignature.getBeat());
        var eccentricityOptions = ingredients.eccentricityOptions;

        List<List<Integer>> rhythmPatternsRaw = new ArrayList<>();
        List<Integer> newPattern;
        for (int i = 0; i < granularityOptions.size(); i++) {

            do {
                newPattern = rhythmPatternCreator.createOneRhythmPattern(
                        timeSignature.getMidiLength(),
                        valuesCountOptions.get(i),
                        granularityOptions.get(i).getMidiValue(),
                        eccentricityOptions.get(i)
                );
            }
            while (isPatternNotUnique(newPattern, rhythmPatternsRaw));

            rhythmPatternsRaw.add(newPattern);
        }

        return Stream.iterate(0, n -> ++n).limit(rhythmPatternsRaw.size())
                .map(index -> {
                    var pattern = rhythmPatternsRaw.get(index);
                    return fetchOrReturnMelodyRhythmPattern(MelodyRhythmPattern.builder()
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

    public List<MelodyTunePattern> collectCreatedTunePatternsSet(TunePatternSetIngredients ingredients) {
        var eccentricityOptions = extractEccentricityListFrom(ingredients.eccentricityOptions);
        List<List<Integer>> tunePatternsRaw = new ArrayList<>();
        List<Integer> newPattern;
        for (int i = 0; i < ingredients.ambitusOptions.size(); i++) {
            do {
                newPattern = tunePatternCreator.createOneTunePattern(
                        ingredients.ambitusOptions.get(i),
                        ingredients.toneAmountOptions.get(i),
                        eccentricityOptions.get(i));
            }
            while (isPatternNotUnique(newPattern, tunePatternsRaw));

            tunePatternsRaw.add(newPattern);
        }
        return Stream.iterate(0, n -> ++n).limit(tunePatternsRaw.size())
                .map(index -> {
                    var pattern = tunePatternsRaw.get(index);
                    return fetchOrReturnMelodyTunePattern(
                            MelodyTunePattern.builder()
                                    .rndCreated(true)
                                    .formAssociationId(ingredients.formId)
                                    .ambitus(PatternStringUtils.getTunePatternsAmbitus(pattern))
                                    .eccentricity(eccentricityOptions.get(index))
                                    .toneAmount(PatternStringUtils.getUniqueValuesCount(pattern))
                                    .body(PatternStringUtils.joinIntListWithCommas(pattern))
                                    .build());
                }).toList();
    }

    private List<Eccentricity> extractEccentricityListFrom(List<Integer> eccentricityOptions) {
        return eccentricityOptions.stream()
                .map(number -> Eccentricity.values()[number])
                .collect(Collectors.toList());
    }

    @Builder
    public static class TunePatternSetIngredients {
        private List<Integer> ambitusOptions;
        private List<Integer> toneAmountOptions;
        private List<Integer> eccentricityOptions;
        private long formId;
    }

    @Builder
    public static class RhythmPatternSetIngredients {
        List<Integer> granularityOptions;
        List<Integer> valueCountOptions;
        long formId;
        TimeSignature timeSignature;
        List<Eccentricity> eccentricityOptions;
    }
}
