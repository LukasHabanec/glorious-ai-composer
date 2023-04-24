package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.Composition;
import cz.habanec.composer3.entities.Melody;
import cz.habanec.composer3.entities.MelodyMeasure;
import cz.habanec.composer3.entities.TonalKey;
import cz.habanec.composer3.entities.MelodyRhythmPattern;
import cz.habanec.composer3.entities.MelodyTunePattern;
import cz.habanec.composer3.entities.assets.TimeSignature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static cz.habanec.composer3.utils.AlphabetUtils.ALPHABET;

@Service
@RequiredArgsConstructor
public class MelodyBuilder {

    private final TonalKeyService tonalKeyService;

    @Transactional
    public Melody buildUpNewMelody(
            List<MelodyRhythmPattern> shuffledRhythmPatterns,
            List<MelodyTunePattern> shuffledTunePatterns,
            List<String> repetitionPatterns,
            Composition composition
    ) {
        var melody = Melody.builder().composition(composition).build();

        if (shuffledRhythmPatterns.isEmpty() || shuffledTunePatterns.isEmpty()) {
            return melody;
        }

        var form = composition.getForm();
        var mainKey = composition.getTonicKey();
        var keyMap = tonalKeyService.extractHarmonySchemaMap(form.getKeyScheme(), mainKey);
        System.out.printf("MelodyBuilder::buildUpNewMelody: harmonySchemaMap: %s%n", keyMap);

        int rhythmSchemaCharIndex;
        int tuneSchemaCharIndex;
        int nextFirstToneIndex = mainKey.getKeyMidiValues().getMelodyGroundToneIndex();
        int nextMeasureShifter;
        MelodyRhythmPattern thisRhythmPattern;
        MelodyTunePattern thisTunePattern;
        String thisRepetitionPattern;
        TonalKey currentKey = mainKey;
        TimeSignature timeSignature = composition.getTimeSignature();

        List<MelodyMeasure> measureList = new ArrayList<>();
        for (int i = 0; i < form.getMeasureCount(); i++) {

            currentKey = keyMap.getOrDefault(i, currentKey);
            rhythmSchemaCharIndex = ALPHABET.indexOf(form.getMelodyRhythmScheme().charAt(i));
            tuneSchemaCharIndex = ALPHABET.indexOf(form.getMelodyTuneScheme().charAt(i));
            thisRhythmPattern = shuffledRhythmPatterns.get(rhythmSchemaCharIndex);
            thisTunePattern = shuffledTunePatterns.get(tuneSchemaCharIndex);
            thisRepetitionPattern = repetitionPatterns.get(tuneSchemaCharIndex);

            var newMeasure = MelodyMeasure.builder()
                    .measureIndex(i)
                    .melody(melody)
                    .timeSignature(timeSignature)
                    .rhythmPattern(thisRhythmPattern)
                    .tunePattern(thisTunePattern)
                    .repetitionSchema(thisRepetitionPattern)
                    .currentKey(currentKey)
                    .firstToneIndex(nextFirstToneIndex)
                    .userSpecialShifter(0)
                    .build();

            measureList.add(newMeasure);
            nextMeasureShifter = newMeasure.getRealTunePatternValues()
                    .get(thisRhythmPattern.getValues().size()); //last value
            nextFirstToneIndex += nextMeasureShifter;
        }
        melody.setMelodyMeasureList(measureList);

        System.out.printf("MelodyBuilder::buildUpNewMelody: Built melody with %d measures.%n", measureList.size());
        return melody;
    }
}
