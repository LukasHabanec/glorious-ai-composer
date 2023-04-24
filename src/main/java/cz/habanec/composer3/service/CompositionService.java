package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.Composition;
import cz.habanec.composer3.entities.MelodyMeasure;
import cz.habanec.composer3.entities.dto.CompositionDto;
import cz.habanec.composer3.entities.dto.CompositionDto.MelodyMeasureDto;
import cz.habanec.composer3.repositories.CompositionRepo;
import cz.habanec.composer3.repositories.MelodyRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompositionService {

    private final CompositionRepo compositionRepo;
    @Setter
    @Getter
    private Composition currentComposition;


    @Transactional
    public List<String> findAllCompositionTitles() {
        return compositionRepo.findAllTitles();
    }

    public CompositionDto getCurrentCompositionForView(Long id) {

        if (Objects.isNull(currentComposition) || !id.equals(currentComposition.getId())) {
            currentComposition = loadComposition(id);
        }
        return CompositionDto.builder()
                .id(currentComposition.getId())
                .title(currentComposition.getTitle())
                .tempo(currentComposition.getTempo())
                .melodyMeasureList(currentComposition.getMelody().getMelodyMeasureList().stream()
                        .map(measure -> MelodyMeasureDto.builder()
                                .index(measure.getMeasureIndex())
                                .currentKey(measure.getCurrentKey().toString())
                                .patternString(measure.getMelodyPatternForView())
                                .userSpecialShifter(measure.getUserSpecialShifter())
                                .build())
                        .toList()
                ).build();
    }

    @Transactional
    public Composition loadComposition(String title) {
        var compositionOpt = compositionRepo.findByTitle(title);
        return resolveCompositionOptional(compositionOpt, title);
    }

    @Transactional
    public Composition loadComposition(Long id) {
        var compositionOpt = compositionRepo.findById(id);
        return resolveCompositionOptional(compositionOpt, id.toString());
    }

    private Composition resolveCompositionOptional(Optional<Composition> optional, String identifier) {
        if (optional.isPresent()) {
            var composition = optional.get();
            System.out.printf("CompositionService::loadComposition: %s with '%d' measures.%n",
                    composition, composition.getMelody().getMelodyMeasureList().size());
            return composition;
        } else {
            System.out.printf("CompositionService::loadComposition: %s not found.%n", identifier);
            // todo exception
//            throw new RuntimeException();
            return null;
        }
    }

    //todo get current composition pomoci kontroly id je blbost!!!
    public Composition getCurrentComposition(Long id) {
        if (Objects.isNull(currentComposition) || !id.equals(currentComposition.getId())) {
            currentComposition = loadComposition(id);
        }
        return currentComposition;
    }

    public String saveCurrentComposition(Long id) {
        if (Objects.isNull(currentComposition) || !id.equals(currentComposition.getId())) {
            return "Inconsistency, not saving.";
        }
        currentComposition = compositionRepo.save(currentComposition);
        System.out.println("CompositionService::saveCurrentComposition: " + currentComposition.getTitle());
        return "Successfully saved " + currentComposition.getTitle();
    }



//    public void shiftMeasureFirstTone(Integer measureIndex, int shifter) {
//        myComposition.getMelody().shiftUserSpecialTone(measureIndex, shifter);
//        myComposition.getAccompaniment().resetHarmonyFields(measureIndex);
//    }


}
