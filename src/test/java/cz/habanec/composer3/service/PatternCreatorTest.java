package cz.habanec.composer3.service;

import cz.habanec.composer3.creators.RhythmPatternCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class PatternCreatorTest {


//    @BeforeAll
//    void init() {
//        sut = new RhythmPatternCreator();
//    }

    RhythmPatternCreator sut;

    @Test
    void creatingMelodyRhythmPattern1() {
        var string = sut.createRandomRhythmPatternBodyBySquashing(4, 4, 1);
        System.out.println(string);
    }
    @Test
    void creatingMelodyRhythmPattern2() {
        var string = sut.createRandomRhythmPatternBodyBySquashing(4, 4, 2);
        System.out.println(string);
    }
    @Test
    void creatingMelodyRhythmPattern3() {
        var string = sut.createRandomRhythmPatternBodyBySquashing(4, 4, 3);
        System.out.println(string);
    }
    @Test
    void creatingMelodyRhythmPattern4() {
        var string = sut.createRandomRhythmPatternBodyBySquashing(4, 0, 4);
        System.out.println(string);
    }

    @Test
    void creatingMelodyRhythmPattern5() {
        var string = sut.createRandomRhythmPatternBodyBySquashing(4, 9, 4);
        System.out.println(string);
    }

    @Test
    void creatingMelodyRhythmPattern11() {
        var string1 = sut.createRandomRhythmPatternBodyByCutting(4, 5, 6);
        System.out.println(string1);

    }

    @Test
    void creatingMelodyRhythmPattern12() {
        var string2 = sut.createRandomRhythmPatternBodyByCutting(4, 4, 2);
        System.out.println(string2);

    }

    @Test
    void creatingMelodyRhythmPattern13() {
        var string3 = sut.createRandomRhythmPatternBodyByCutting(4, 5, 1);
        System.out.println(string3);
    }
}