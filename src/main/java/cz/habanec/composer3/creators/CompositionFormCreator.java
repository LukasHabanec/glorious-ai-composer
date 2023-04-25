package cz.habanec.composer3.creators;

import cz.habanec.composer3.entities.enums.Eccentricity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompositionFormCreator {

    private static final String SCHEME_LETTER_STRING = "YWUSQOMKIGECABDFHJLNPRTVXZ";
    private static final int SCHEME_LETTER_STRING_MIDDLE_INDEX = 12;

    public String createLetterScheme(int letterCount, Eccentricity eccentricity) {

    }
}
