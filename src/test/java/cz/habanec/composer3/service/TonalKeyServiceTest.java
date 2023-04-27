package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.TonalKey;
import cz.habanec.composer3.entities.assets.Modus;
import cz.habanec.composer3.entities.assets.QuintCircleKey;
import cz.habanec.composer3.entities.enums.ModusLabel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TonalKeyServiceTest {

    @Autowired
    private TonalKeyService sut;

    @Test
    void creating_harmony_plan_map() {
        var key = new TonalKey(
                QuintCircleKey.builder().midiIniTone(0).name("C").label("C").build(),
                Modus.builder().label(ModusLabel.MAJOR).intervalsString("2,2,1,2,2,2,1").build()
        );
        var result = sut.extractHarmonySchemaMap(
                "0_0 7_1", key
        );
        System.out.println(result);

    }

}