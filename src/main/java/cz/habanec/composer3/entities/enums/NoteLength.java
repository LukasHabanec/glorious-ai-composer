package cz.habanec.composer3.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cz.habanec.composer3.utils.Properties.DEFAULT_MIDI_RESOLUTION;

@Getter
@AllArgsConstructor
public enum NoteLength {

    WHOLE_NOTE(DEFAULT_MIDI_RESOLUTION * 4, "1"),
    HALF_NOTE(DEFAULT_MIDI_RESOLUTION * 2, "2"),
    DOTTED_HALF_NOTE(DEFAULT_MIDI_RESOLUTION * 3, "2."),
    TRIPLET_HALF_NOTE(DEFAULT_MIDI_RESOLUTION * 4 / 2, "2E"),
    QUARTER_NOTE(DEFAULT_MIDI_RESOLUTION,"4"),
    DOTTED_QUARTER_NOTE(DEFAULT_MIDI_RESOLUTION * 3 / 2, "4."),
    TRIPLET_QUARTER_NOTE(DEFAULT_MIDI_RESOLUTION * 2 / 3, "4E"),
    EIGHT_NOTE(DEFAULT_MIDI_RESOLUTION / 2, "8"),
    DOTTED_EIGHT_NOTE(DEFAULT_MIDI_RESOLUTION * 3 / 4, "8."),
    TRIPLET_EIGHT_NOTE(DEFAULT_MIDI_RESOLUTION / 3, "8E"),
    SIXTEENTH_NOTE(DEFAULT_MIDI_RESOLUTION / 4, "16");

    private static final Map<String, NoteLength> VALUES_MAP_BY_LABEL =
            EnumSet.allOf(NoteLength.class).stream()
                    .collect(Collectors.toMap(NoteLength::getLabel, Function.identity()));

    private final int midiValue;
    private final String label;

}
