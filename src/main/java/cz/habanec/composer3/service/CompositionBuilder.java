package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.Composition;
import cz.habanec.composer3.entities.CompositionForm;
import cz.habanec.composer3.entities.MidiSettings;
import cz.habanec.composer3.entities.TonalKey;
import cz.habanec.composer3.entities.MelodyRhythmPattern;
import cz.habanec.composer3.entities.MelodyTunePattern;
import cz.habanec.composer3.entities.assets.TimeSignature;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cz.habanec.composer3.utils.Properties.DEFAULT_MIDI_RESOLUTION;


@Service
@RequiredArgsConstructor
public class CompositionBuilder {

    private final MelodyBuilder melodyBuilder;
    private final CompositionService compositionService;

    @Transactional
    public Composition buildNewComposition(NewCompositionIngredients ingredients) {

        System.out.println("CompositionBuilder::buildNewComposition: " + ingredients.getTitle());

        var composition = Composition.builder()
                .id(33L) // todo odstranit, jakmile FE bude umet loadovat
                .title(ingredients.getTitle())
                .tonicKey(ingredients.getMainKey())
                .form(ingredients.getForm())
                .timeSignature(ingredients.getTimeSignature())
                .tempo(ingredients.getTempo())
                .build();
        composition.setMidiSettings(MidiSettings.builder()
                .resolution(DEFAULT_MIDI_RESOLUTION)
                .melodyOn(true)
                .accompanimentOn(false)
                .composition(composition)
                .build());
        composition.setMelody(melodyBuilder.buildUpNewMelody(
                ingredients.getRhythmPatterns(),
                ingredients.getTunePatterns(),
                ingredients.getRepetitionPatterns(),
                composition));

        return composition;
    }

    @Builder
    @Data
    public static class NewCompositionIngredients {

        List<MelodyRhythmPattern> rhythmPatterns;
        List<MelodyTunePattern> tunePatterns;
        List<String> repetitionPatterns;
        TonalKey mainKey;
        CompositionForm form;
        TimeSignature timeSignature;
        String title;
        Integer tempo;
    }

}
