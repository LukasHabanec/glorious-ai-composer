package cz.habanec.composer3.midi;

import cz.habanec.composer3.entities.assets.Modus;
import cz.habanec.composer3.entities.assets.QuintCircleKey;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class KeyMidiValuesTest {

    @Test
    void creatingKeyMidiValuesSuccessful() {
        var cMajorValues = KeyMidiValues.from(
                QuintCircleKey.builder().midiIniTone(0).name("C").label("C").build(),
                Modus.builder().label("MAJOR").intervalsString("2,2,1,2,2,2,1").build());
        assertTrue(Objects.nonNull(cMajorValues));
        System.out.println(cMajorValues);
        System.out.println(cMajorValues.getScale());

    }
}