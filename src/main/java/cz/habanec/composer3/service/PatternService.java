package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.assets.MelodyRhythmPattern;
import cz.habanec.composer3.entities.assets.MelodyTunePattern;
import cz.habanec.composer3.entities.assets.Pattern;
import cz.habanec.composer3.repositories.MelodyRhythmPatternRepo;
import cz.habanec.composer3.repositories.MelodyTunePatternRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatternService {

    private final MelodyRhythmPatternRepo rhythmPatternRepo;
    private final MelodyTunePatternRepo tunePatternRepo;



    @Transactional
    public MelodyRhythmPattern getOrCreateMelodyRhythmPattern(String body, Long formAssociationId) {
        return rhythmPatternRepo.findByBodyAndFormAssociationId(body, formAssociationId)
                .orElseGet(() -> new MelodyRhythmPattern(body, formAssociationId));

    }

    @Transactional
    public MelodyTunePattern getOrCreateMelodyTunePattern(String body, Long formAssociationId) {
        return tunePatternRepo.findByBodyAndFormAssociationId(body, formAssociationId)
                .orElseGet(() -> new MelodyTunePattern(body, formAssociationId));

    }



    public <T extends Pattern> List<T> getRequiredPatternListUsingShuffledIndexes(
            List<Integer> shuffledIndexes, List<T> patterns) {

        List<T> reconstructedPatternList = new ArrayList<>();
        for (int i = 0; i < shuffledIndexes.size(); i++) {
            var selectedIndex = shuffledIndexes.get(i);
            reconstructedPatternList.add(patterns.get(selectedIndex));
        }
        return reconstructedPatternList;
    }
}
