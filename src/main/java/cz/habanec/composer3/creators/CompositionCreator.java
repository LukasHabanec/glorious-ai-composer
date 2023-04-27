package cz.habanec.composer3.creators;

import cz.habanec.composer3.entities.enums.ModusLabel;
import cz.habanec.composer3.entities.enums.NoteLength;
import cz.habanec.composer3.entities.enums.Eccentricity;
import cz.habanec.composer3.service.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import static cz.habanec.composer3.utils.PatternStringUtils.getUniqueSymbolsCount;
import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;
import static cz.habanec.composer3.utils.ProbabilityUtils.getRandomAndShuffledCustomOptionsListEvenlyBounded;

@Component
@RequiredArgsConstructor
public class CompositionCreator {

    private final CompositionService compositionService;
    private final TonalKeyService tonalKeyService;

    private final CompositionBuilder compositionBuilder;
    private final PatternService patternService;
    private final RhythmPatternCreator rhythmPatternCreator;
    private final TunePatternCreator tunePatternCreator;
    private final CompositionFormService formService;
    private final TimeSignatureService timeSignatureService;

    public void createNewRandomComposition(RandomCompositionIngredients ingredients) {

        var timeSignature = timeSignatureService.fetchOrCreateTimeSignature(ingredients.getTimeSignatureLabel());
        var form = formService.getFormByTitle(ingredients.getFormTitle());
        var mainKey = tonalKeyService.getTonalKeyByLabels(
                ingredients.getQuintCircleKeyLabel(), ingredients.getModusLabel());
        int rhythmValueCountTopBound = timeSignature.getMidiLength() / NoteLength.SIXTEENTH_NOTE.getMidiValue(); // how many 16th in one measure
        int rhythmSchemeSymbolsCount = getUniqueSymbolsCount(form.getMelodyRhythmScheme());

        var rhythmValueCountOptions = getRandomAndShuffledCustomOptionsListEvenlyBounded(
                rhythmSchemeSymbolsCount, 1, rhythmValueCountTopBound);

        var rhythmGranularityOptions = getRandomAndShuffledCustomOptionsListEvenlyBounded(
                rhythmSchemeSymbolsCount, 0, NoteLengthHelper.DEFAULT_GRANULARITY_DEPTH);

        var rhythmRepetitionDensityOptions = getRandomAndShuffledCustomOptionsListEvenlyBounded(
                rhythmSchemeSymbolsCount, 0, 100);

        var rhythmEccentricOptions = getRandomAndShuffledCustomOptionsListEvenlyBounded(
                rhythmSchemeSymbolsCount, 0, 1);

        var rhythmPatterns = rhythmPatternCreator.createRhythmPatternsSet(
                RhythmPatternCreator.RhythmPatternSetIngredients.builder()
                        .formId(form.getId())
                        .timeSignature(timeSignature)
                        .eccentricOptions(rhythmEccentricOptions)
                        .granularityOptions(rhythmGranularityOptions)
                        .valueCountOptions(rhythmValueCountOptions)
                        .build());

        int tuneSchemaSymbolsCount = getUniqueSymbolsCount(form.getMelodyTuneScheme());

        var tunePatternAmbitusOptions = getRandomAndShuffledCustomOptionsListEvenlyBounded(
                tuneSchemaSymbolsCount, 2, 9);

        var tunePatternToneAmountOptions = getRandomAndShuffledCustomOptionsListEvenlyBounded(
                tuneSchemaSymbolsCount, 2, 9);

        var tuneEccentricityOptions = getRandomAndShuffledCustomOptionsListEvenlyBounded(
                tuneSchemaSymbolsCount, 0, Eccentricity.values().length - 1);

        var tunePatterns = tunePatternCreator.createTunePatternsSet(
                TunePatternCreator.TunePatternSetIngredients.builder()
                        .formId(form.getId())
                        .eccentricityOptions(tuneEccentricityOptions)
                        .ambitusOptions(tunePatternAmbitusOptions)
                        .toneAmountOptions(tunePatternToneAmountOptions)
                        .build()
        );

        var repetitionPatterns = tunePatternCreator.createRepetitionPatternSet(
                rhythmPatterns, rhythmRepetitionDensityOptions);

        var tempo = RANDOM.nextInt(60, 260);

        compositionService.setCurrentComposition(compositionBuilder.buildNewComposition(
                CompositionBuilder.NewCompositionIngredients.builder()
                        .rhythmPatterns(rhythmPatterns)
                        .tunePatterns(tunePatterns)
                        .repetitionPatterns(repetitionPatterns)
                        .mainKey(mainKey)
                        .timeSignature(timeSignature)
                        .form(form)
                        .title(ingredients.getTitle())
                        .tempo(tempo)
                        .build()
        ));
    }

    @Value
    @Builder
    public static class RandomCompositionIngredients {
        String formTitle, timeSignatureLabel, title, quintCircleKeyLabel;
        ModusLabel modusLabel;
    }
}
