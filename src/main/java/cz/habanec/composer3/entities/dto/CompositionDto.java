package cz.habanec.composer3.entities.dto;

import lombok.Builder;
import lombok.Setter;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Value
public class CompositionDto {

    Long id;
    String title;
    Integer tempo;
    String key;
    String modus;
    String timeSignature;
    List<MelodyMeasureDto> melodyMeasureList;
    Assets assets;
    ZonedDateTime createdAt;
    ZonedDateTime updatedAt;

    @Builder
    @Value
    public static class MelodyMeasureDto {
        Integer index;
        Integer userSpecialShifter;
        String patternString;
        String currentKey;

    }

    @Builder
    @Value
    public static class Assets {
        List<String> modi;
        List<String> quintCircleKeys;

    }
}
