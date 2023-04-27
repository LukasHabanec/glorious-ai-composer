package cz.habanec.composer3.utils;

import cz.habanec.composer3.entities.TonalKey;
import cz.habanec.composer3.entities.assets.TimeSignature;
import cz.habanec.composer3.entities.enums.NoteLength;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class MelodyMeasureUtils {

    public static final int VIEW_QUARTER_DIVIDER = Properties.DEFAULT_MIDI_RESOLUTION / 4;

    public static String extractMelodyPatternForView(
            Integer firstToneIndex,
            Integer userSpecialShifter,
            List<Integer> rhythmPattern,
            List<Integer> tunePattern,
            TimeSignature timeSignature
    ) {
        final int MIN_VALUE_VISIBLE = NoteLength.SIXTEENTH_NOTE.getMidiValue();
        int currentInvisibleValue = 0;
        int currentInvisibleLength = 0;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rhythmPattern.size(); i++) {
            int currentToneLength = rhythmPattern.get(i);
            if (currentToneLength < MIN_VALUE_VISIBLE) {
                currentInvisibleLength += currentToneLength;
                if (currentInvisibleValue == 0) {
                    currentInvisibleValue = tunePattern.get(i) + firstToneIndex + userSpecialShifter;
                }
            } else {
                if (currentInvisibleLength > 0) {
                    sb.append(currentInvisibleValue).append("*").append("***".repeat(Math.max(0, (currentInvisibleLength / MIN_VALUE_VISIBLE - 1))));
                    currentInvisibleValue = 0;
                    currentInvisibleLength = 0;
                }
                sb.append(tunePattern.get(i) + firstToneIndex + userSpecialShifter);
                sb.append("_").append("___".repeat(Math.max(0, (currentToneLength - MIN_VALUE_VISIBLE) / VIEW_QUARTER_DIVIDER)));
            }
        }
        if (currentInvisibleLength > 0) {
            sb.append(currentInvisibleValue).append("*").append("***".repeat(Math.max(0, (currentInvisibleLength / MIN_VALUE_VISIBLE - 1))));
        }
        int beatBarOffset = 0;
        for (int i = 0; i < timeSignature.getNumOfBeats() - 1; i++) {
            beatBarOffset += timeSignature.getBeat().getMidiValue() / 4 + 1;
            sb.insert(beatBarOffset, "|");
        }
        sb.append("]");
        return sb.toString();
    }

    public static int[] extractMelodyMatrix(
            int midiLenght,
            int firstToneIndex,
            int userSpecialShifter,
            List<Integer> rhythmPattern,
            List<Integer> tunePattern,
            TonalKey currentKey
    ) {
        var melodyMatrix = new int[midiLenght];
        int index = 0;
        for (int i = 0; i < rhythmPattern.size(); i++) {
            for (int j = 0; j < rhythmPattern.get(i); j++) {
                int requiredMidiValueIndex = tunePattern.get(i) + firstToneIndex + userSpecialShifter;
                melodyMatrix[index] = currentKey.getKeyMidiValues().getScale().get(requiredMidiValueIndex);
                index++;
            }
        }
        return melodyMatrix;
    }

    public static List<Integer> extractRealTunePattern(
            List<Integer> rawTunePattern,
            Map<Integer, Integer> repetitionMap,
            int rhythmPatternSize
    ) {
        List<Integer> realPattern = new ArrayList<>();
        int repetitions;
        int newValue;
        int rawPatternIndex = 0;
        int shifter = 0;

        while (realPattern.size() < rhythmPatternSize + 1) { // +1 potrebuju vzdy 1 navic pro dalsi measure

            repetitions = repetitionMap.getOrDefault(rawPatternIndex, 1);
            newValue = rawTunePattern.get(rawPatternIndex) + shifter;
            for (int i = 0; i < repetitions; i++) {
                realPattern.add(newValue);
            }
            rawPatternIndex++;

            // opatreni pri potrebe vice hodnot nez 9 (bezna delka tunePatternu)
            if (rawPatternIndex == rawTunePattern.size()) {
                shifter = newValue;
                rawPatternIndex = 1; // nultou value nechci opakovat, byla by tam dvakrat
            }
        }
        return realPattern;
    }

}
