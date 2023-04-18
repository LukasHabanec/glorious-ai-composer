package cz.habanec.composer3.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TunePatternEccentricity {
    NO_ECCENTRICITY(0),
    LOW_ECCENTRICITY(1),
    MID_ECCENTRICITY(2),
    HIGH_ECCENTRICITY(3);
    private final int value;
}
