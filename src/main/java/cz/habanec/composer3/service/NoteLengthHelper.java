package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.enums.NoteLength;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

import static cz.habanec.composer3.utils.PatternStringUtils.UNDERSCORE_DELIMITER;

@UtilityClass
public class NoteLengthHelper {

    public static final int MIN_RHYTHM_VALUE = 3;
    public static final int DEFAULT_GRANULARITY_DEPTH = 2;

    public static String glueLabelsForValueAndAddToMap(final int initialMidiValue) {
        int midiValue = initialMidiValue;
        var midiValuesList = NoteLength.VALUES_MAP_BY_MIDI_VALUE.keySet().stream().sorted().toList();
        List<String> labelList = new ArrayList<>();

        while (midiValue > 0) {

            int newValue = findClosestLowerValue(midiValue, midiValuesList);
            labelList.add(NoteLength.getLabelByMidiValue(newValue));

            midiValue -= newValue;
        }
        String newLabel = String.join(UNDERSCORE_DELIMITER, labelList);
        NoteLength.LABELS_MAP_BY_MIDI_VALUE.putIfAbsent(initialMidiValue, newLabel);
        NoteLength.MIDI_VALUES_MAP_BY_LABEL.putIfAbsent(newLabel, initialMidiValue);

        return newLabel;
    }

    private static int findClosestLowerValue(int midiValue, List<Integer> valuesAscendant) {
        for (int i = valuesAscendant.size() - 1; i >= 0; i--) {
            int eligibleValue = valuesAscendant.get(i);
            if (eligibleValue <= midiValue) {
                return eligibleValue;
            }
        }
        return MIN_RHYTHM_VALUE;
    }

    public static int getMidiValueBySplittingLabelAndAddToMap(String label) {
        int valuesSum = Arrays.stream(label.split(UNDERSCORE_DELIMITER))
                .mapToInt(NoteLength.MIDI_VALUES_MAP_BY_LABEL::get)
                .sum();
        NoteLength.LABELS_MAP_BY_MIDI_VALUE.putIfAbsent(valuesSum, label);
        NoteLength.MIDI_VALUES_MAP_BY_LABEL.putIfAbsent(label, valuesSum);

        return valuesSum;
    }

    public static NoteLength getHalfTheLengthOf(NoteLength noteLength) {
        return NoteLength.VALUES_MAP_BY_MIDI_VALUE.get(noteLength.getMidiValue() / 2);
    }

    public static NoteLength getDoubledLengthOf(NoteLength noteLength) {
        return NoteLength.VALUES_MAP_BY_MIDI_VALUE.get(noteLength.getMidiValue() * 2);
    }

    public static List<NoteLength> extractNoteLengthListFrom(List<Integer> granularityOptions, NoteLength beat) {
        Map<Integer, NoteLength> granularityMap = new HashMap<>();
        feedGranularityMapOfHalvedNoteLengths(granularityMap, getDoubledLengthOf(beat), DEFAULT_GRANULARITY_DEPTH);

        return granularityOptions.stream().map(granularityMap::get).collect(Collectors.toList());
    }

    private static void feedGranularityMapOfHalvedNoteLengths(
            Map<Integer, NoteLength> map, NoteLength noteLength, int depth
    ) {
        map.put(depth, noteLength);
        if (depth > 0) {
            feedGranularityMapOfHalvedNoteLengths(map, getHalfTheLengthOf(noteLength), depth - 1);
        }
    }

}
