package cz.habanec.composer3.entities.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CompositionDto {

    Long id;
    String title;
    List<MelodyMeasureDto> melodyMeasureList;
    Integer tempo;

    @Builder
    @Value
    public static class MelodyMeasureDto {
        Integer index;
        Integer userSpecialShifter;
        String patternString;
        String currentKey;

    }
}
