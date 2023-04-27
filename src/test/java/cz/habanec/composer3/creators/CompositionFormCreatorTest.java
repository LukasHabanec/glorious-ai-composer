package cz.habanec.composer3.creators;

import cz.habanec.composer3.entities.enums.Eccentricity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositionFormCreatorTest {

    @Test
    void createMatrix() {
//        var res = CompositionFormCreator.createAlphabetMatrix(8);
//        System.out.println(res);
    }

    @Test
    void createScheme() {
        CompositionFormCreator cfc = new CompositionFormCreator();
        int schemeLength = 16;
        var res = cfc.createRandomLetterScheme(schemeLength, 8, Eccentricity.ZERO);
        assertEquals(res.length(), schemeLength);
    }

}