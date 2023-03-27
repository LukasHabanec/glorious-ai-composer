package cz.habanec.composer3.midi;

import cz.habanec.composer3.entities.assets.Modus;
import cz.habanec.composer3.entities.assets.QuintCircleKey;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cz.habanec.composer3.midi.KeyMidiValuesHelper.MIDI_TONES_LOWEST_GROUND_TONE_ELIGIBLE_FOR_MELODY;
import static cz.habanec.composer3.midi.KeyMidiValuesHelper.MIDI_TONES_MAX_VALUE;
import static cz.habanec.composer3.midi.KeyMidiValuesHelper.MIDI_TONES_MIN_VALUE;

@Getter
@Builder
@ToString(of = {"initialMidiValue", "melodyGroundToneIndex", "lowestToneIndex"})
public class KeyMidiValues {

    private List<Integer> scale;
    private Integer initialMidiValue;
    private Integer melodyGroundToneIndex;
    private Integer lowestToneIndex;

    public static KeyMidiValues from(QuintCircleKey quintCircleKey, Modus modus) {

        if (Objects.isNull(quintCircleKey) || Objects.isNull(modus) || modus.getIntervals().isEmpty() ) {
            return null;
        }

        int initialMidiValue = quintCircleKey.getMidiIniTone();
        List<Integer> scaleMidiValueList = new ArrayList<>();
        var modusIntervals = modus.getIntervals();
        int melodyGroundTone = 0;
        int interval;

        int currentScaleGradeIndex = 0;
        for (int newMidiValue = initialMidiValue; newMidiValue < MIDI_TONES_MAX_VALUE; newMidiValue += interval) {

            scaleMidiValueList.add(newMidiValue);

            var modusIncrementIndex = currentScaleGradeIndex % modusIntervals.size();
            interval = modusIntervals.get(modusIncrementIndex);

            // CESTOU URCI VHODNY MELODY_GROUND_TONE
            if (currentScaleGradeIndex % modusIntervals.size() == 0
                    && newMidiValue >= MIDI_TONES_LOWEST_GROUND_TONE_ELIGIBLE_FOR_MELODY
                    && melodyGroundTone == 0) {
                melodyGroundTone = newMidiValue;
            }

            currentScaleGradeIndex++;
        }

        int currentScaleGradeIndexDecrement = 1;
        for (int newMidiValue = initialMidiValue; newMidiValue >= MIDI_TONES_MIN_VALUE; newMidiValue -= interval) {
            interval = modusIntervals.get(modusIntervals.size() - currentScaleGradeIndexDecrement);
            // DOPLNI NEJHLUBSI TONY
            if (newMidiValue == initialMidiValue) { continue; }
            scaleMidiValueList.add(newMidiValue);
            currentScaleGradeIndexDecrement++;
        }
        scaleMidiValueList.sort(Integer::compareTo);

        System.out.printf("KeyMidiValues::from: Extracted KeyMidiValues for Key %s %s%n",
                quintCircleKey.getLabel(), modus.getLabel());

        return KeyMidiValues.builder()
                .scale(scaleMidiValueList)
                .initialMidiValue(initialMidiValue)
                .melodyGroundToneIndex(scaleMidiValueList.indexOf(melodyGroundTone))
                .lowestToneIndex(scaleMidiValueList.indexOf(initialMidiValue))
                .build();
    }

}
