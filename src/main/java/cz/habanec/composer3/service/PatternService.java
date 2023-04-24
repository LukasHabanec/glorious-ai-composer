package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.MelodyRhythmPattern;
import cz.habanec.composer3.entities.MelodyTunePattern;
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
    public MelodyRhythmPattern fetchOrReturnMelodyRhythmPattern(MelodyRhythmPattern pattern) {
        return rhythmPatternRepo.findByBody(pattern.getBody())
                .orElse(pattern);

    }

    @Transactional
    public MelodyTunePattern fetchOrReturnMelodyTunePattern(MelodyTunePattern pattern) {
        return tunePatternRepo.findByBody(pattern.getBody())
                .orElse(pattern);

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
