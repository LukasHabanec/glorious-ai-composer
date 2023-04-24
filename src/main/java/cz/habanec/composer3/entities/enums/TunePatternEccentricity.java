package cz.habanec.composer3.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TunePatternEccentricity {
    ZERO(0),
    LOW(1),
    MIDDLE(2),
    HIGH(3);
    private final int value;
}
