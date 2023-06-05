package cz.habanec.composer3.service;

import cz.habanec.composer3.creators.TunePatternCreator;
import cz.habanec.composer3.entities.enums.Eccentricity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(Lifecycle.PER_CLASS)
class TunePatternCreatorTest {

    @Autowired
    TunePatternCreator sut;
//
//    @BeforeAll
//    void init() {
//        sut = new TunePatternCreator();
//    }
    @Test
    void repetitionPatternWorking() {

//            var c = sut.createOneRepetitionPattern(8, 50);

//        System.out.println(c);
//        }
    }

    @Test
    void tunePatternWorking() {
        var c = sut.createOneTunePattern(0,0, Eccentricity.LOW);
        System.out.println(c);
    }
}