package cz.habanec.composer3.entities.enums;

import cz.habanec.composer3.service.NoteLengthHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cz.habanec.composer3.service.NoteLengthHelper.glueLabelsForValueAndAddToMap;
import static cz.habanec.composer3.utils.Properties.DEFAULT_MIDI_RESOLUTION;

@Getter
@AllArgsConstructor
public enum NoteLength {
    DOUBLE_WHOLE_NOTE(DEFAULT_MIDI_RESOLUTION * 8, "21"),
    WHOLE_NOTE(DEFAULT_MIDI_RESOLUTION * 4, "1"),
    HALF_NOTE(DEFAULT_MIDI_RESOLUTION * 2, "2"),
    DOTTED_HALF_NOTE(DEFAULT_MIDI_RESOLUTION * 3, "2."),
    TRIPLET_HALF_NOTE(DEFAULT_MIDI_RESOLUTION * 4 / 3, "2E"),
    QUARTER_NOTE(DEFAULT_MIDI_RESOLUTION,"4"),
    DOTTED_QUARTER_NOTE(DEFAULT_MIDI_RESOLUTION * 3 / 2, "4."),
    TRIPLET_QUARTER_NOTE(DEFAULT_MIDI_RESOLUTION * 2 / 3, "4E"),
    EIGHT_NOTE(DEFAULT_MIDI_RESOLUTION / 2, "8"),
    DOTTED_EIGHT_NOTE(DEFAULT_MIDI_RESOLUTION * 3 / 4, "8."),
    TRIPLET_EIGHT_NOTE(DEFAULT_MIDI_RESOLUTION / 3, "8E"),
    SIXTEENTH_NOTE(DEFAULT_MIDI_RESOLUTION / 4, "16"),
    TRIPLET_SIXTEENTH_NOTE(DEFAULT_MIDI_RESOLUTION / 6, "16E"),
    THIRTY_TWO_NOTE(DEFAULT_MIDI_RESOLUTION / 8, "32"),
    SIXTY_FOUR_NOTE(DEFAULT_MIDI_RESOLUTION / 16, "64");


    public static final Map<String, Integer> MIDI_VALUES_MAP_BY_LABEL =
            EnumSet.allOf(NoteLength.class).stream()
                    .collect(Collectors.toMap(NoteLength::getLabel, NoteLength::getMidiValue));

    public static final Map<String, NoteLength> VALUES_MAP_BY_LABEL =
            EnumSet.allOf(NoteLength.class).stream()
                    .collect(Collectors.toMap(NoteLength::getLabel, Function.identity()));

    public static final Map<Integer, String> LABELS_MAP_BY_MIDI_VALUE =
            EnumSet.allOf(NoteLength.class).stream()
                    .collect(Collectors.toMap(NoteLength::getMidiValue, NoteLength::getLabel));

    public static final Map<Integer, NoteLength> VALUES_MAP_BY_MIDI_VALUE =
            EnumSet.allOf(NoteLength.class).stream()
                    .collect(Collectors.toMap(NoteLength::getMidiValue, Function.identity()));

    private final int midiValue;
    private final String label;

    public static String getLabelByMidiValue(int midiValue) {
        var noteLabel = LABELS_MAP_BY_MIDI_VALUE.get(midiValue);
        return noteLabel != null
                ? noteLabel
                : glueLabelsForValueAndAddToMap(midiValue);
    }

    public static int getMidiValueByLabel(String label) {
        var midiValue = MIDI_VALUES_MAP_BY_LABEL.get(label);
        return midiValue != null
                ? midiValue
                : NoteLengthHelper.getMidiValueBySplittingLabelAndAddToMap(label);
    }


}
