package cz.habanec.composer3.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
class TunePatternCreatorTest {

    TunePatternCreator sut;

    @BeforeAll
    void init() {
        sut = new TunePatternCreator();
    }
    @Test
    void repetitionPatternWorking() {
//        for (int i = 0; i < 101; i++) {

            var c = sut.createRepetitionPattern(8, 50);

        System.out.println(c);
//        }
    }

    @Test
    void tunePatternWorking() {
        var c = sut.createTunePattern(0,0,0);
//        System.out.println(c);
    }
}