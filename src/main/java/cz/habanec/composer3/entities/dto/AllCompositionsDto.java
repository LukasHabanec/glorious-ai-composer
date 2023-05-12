package cz.habanec.composer3.entities.dto;

import cz.habanec.composer3.entities.Composition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class AllCompositionsDto {

    List<Dto> compositions;

    public static AllCompositionsDto from(List<Composition> compositions) {

        return new AllCompositionsDto(compositions.stream()
                .map(composition -> Dto.builder()
                        .compositionId(composition.getId())
                        .tempo(composition.getTempo())
                        .title(composition.getTitle())
                        .formName(composition.getForm().getTitle())
                        .createdAt(composition.getCreatedAt())
                        .updatedAt(composition.getUpdatedAt())
                        .measureCount(composition.getMelody().getMelodyMeasureList().size())
                        .timeSignature(composition.getTimeSignature().getLabel())
                        .key(composition.getTonicKey().toString())
                        .build()
                ).collect(Collectors.toList())
        );
    }

    @Value
    @Builder
    public static class Dto {
        Long compositionId;
        String title, formName, timeSignature, key;
        ZonedDateTime createdAt, updatedAt;
        Integer measureCount, tempo;

    }
}
