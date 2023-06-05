package cz.habanec.composer3.service;

import cz.habanec.composer3.creators.RhythmPatternCreator;
import cz.habanec.composer3.entities.enums.Eccentricity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class RhythmPatternCreatorTest {


//    @BeforeAll
//    void init() {
//        sut = new RhythmPatternCreator();
//    }

    RhythmPatternCreator sut;

    @Test
    void creatingMelodyRhythmPattern1() {
        var string = sut.createOneRhythmPattern(4, 4, 1, Eccentricity.HIGH);
        System.out.println(string);
    }
    @Test
    void creatingMelodyRhythmPattern2() {
        var string = sut.createOneRhythmPattern(4, 4, 2, Eccentricity.HIGH);
        System.out.println(string);
    }
    @Test
    void creatingMelodyRhythmPattern3() {
        var string = sut.createOneRhythmPattern(4, 4, 3, Eccentricity.HIGH);
        System.out.println(string);
    }
    @Test
    void creatingMelodyRhythmPattern4() {
        var string = sut.createOneRhythmPattern(4, 0, 4, Eccentricity.HIGH);
        System.out.println(string);
    }

    @Test
    void creatingMelodyRhythmPattern5() {
        var string = sut.createOneRhythmPattern(4, 9, 4, Eccentricity.HIGH);
        System.out.println(string);
    }

    @Test
    void creatingMelodyRhythmPattern11() {
        var string1 = sut.createOneRhythmPattern(4, 5, 6, Eccentricity.LOW);
        System.out.println(string1);

    }

    @Test
    void creatingMelodyRhythmPattern12() {
        var string2 = sut.createOneRhythmPattern(4, 4, 2, Eccentricity.LOW);
        System.out.println(string2);

    }

    @Test
    void creatingMelodyRhythmPattern13() {
        var string3 = sut.createOneRhythmPattern(4, 5, 1, Eccentricity.LOW);
        System.out.println(string3);
    }
}