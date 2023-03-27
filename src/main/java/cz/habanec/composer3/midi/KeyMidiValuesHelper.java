package cz.habanec.composer3.midi;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyMidiValuesHelper {

    public static int MIDI_TONES_MIN_VALUE = 0;
    public static int MIDI_TONES_MAX_VALUE = 127;
    public static int MIDI_TONES_LOWEST_GROUND_TONE_ELIGIBLE_FOR_MELODY = 64; // e1
    public static int MIDI_TONES_LOWEST_HEARABLE_VALUE = 21;
    public static int MIDI_TONES_HIGHEST_HEARABLE_VALUE = 108;

}
