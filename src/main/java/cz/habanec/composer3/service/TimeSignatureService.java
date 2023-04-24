package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.assets.TimeSignature;
import cz.habanec.composer3.entities.enums.NoteLength;
import cz.habanec.composer3.repositories.TimeSignatureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TimeSignatureService {

    private final TimeSignatureRepo timeSignatureRepo;

    @Transactional
    public TimeSignature fetchOrCreateTimeSignature(String label) {
        return timeSignatureRepo.findByLabel(label)
                .orElseGet(() -> {
                    int[] twoNums = Arrays.stream(label.split("/"))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    return timeSignatureRepo.save(TimeSignature.builder()
                            .numOfBeats(twoNums[0])
                            .beat(NoteLength.VALUES_MAP_BY_LABEL.get(String.valueOf(twoNums[1])))
                            .label(label)
                            .build()
                    );
                });
    }
}
