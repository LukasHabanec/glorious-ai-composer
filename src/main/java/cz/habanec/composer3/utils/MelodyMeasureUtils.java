package cz.habanec.composer3.utils;

import cz.habanec.composer3.entities.TonalKey;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class MelodyMeasureUtils {

    public static String extractMelodyPatternForView(
            Integer firstToneIndex,
            Integer userSpecialShifter,
            List<Integer> rhythmPattern,
            List<Integer> tunePattern
    ) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rhythmPattern.size(); i++) {
            sb.append(tunePattern.get(i) + firstToneIndex + userSpecialShifter);
            sb.append("__".repeat(Math.max(0, rhythmPattern.get(i) - 1)));
        }
        sb.append("]");
        return sb.toString();
    }

    public static int[] extractMelodyMatrix(
            Integer size, // todo composition.getMidiSettings().getResolution() * numOfBeats
            Integer firstToneIndex,
            Integer userSpecialShifter,
            List<Integer> rhythmPattern,
            List<Integer> tunePattern,
            TonalKey currentKey) {
        var melodyMatrix = new int[size];
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
