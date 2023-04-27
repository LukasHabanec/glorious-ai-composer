package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.TonalKey;
import cz.habanec.composer3.entities.assets.Modus;
import cz.habanec.composer3.entities.assets.QuintCircleKey;
import cz.habanec.composer3.entities.enums.ModusLabel;
import cz.habanec.composer3.repositories.ModusRepo;
import cz.habanec.composer3.repositories.QuintCircleKeyRepo;
import cz.habanec.composer3.repositories.TonalKeyRepo;
import cz.habanec.composer3.utils.PatternStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TonalKeyService {

    private final TonalKeyRepo tonalKeyRepo;
    private final QuintCircleKeyRepo quintCircleKeyRepo;
    private final ModusRepo modusRepo;

    private TonalKey getTonalKey(Optional<QuintCircleKey> quintCircleKeyOpt, Optional<Modus> modusOpt) {
        if (quintCircleKeyOpt.isEmpty() || modusOpt.isEmpty()) {
            // todo fatal exception
            return null;
        }
        return tonalKeyRepo.findByQuintCircleKeyAndModus(quintCircleKeyOpt.get(), modusOpt.get())
                .orElseGet(() -> tonalKeyRepo.save(new TonalKey(quintCircleKeyOpt.get(), modusOpt.get())));
    }

    @Transactional
    public TonalKey getTonalKeyByQuintCircleKeyIdAndModusId(Long quintCircleKeyId, Long modusId) {
        var quintCircleKeyOpt = quintCircleKeyRepo.findById(quintCircleKeyId);
        var modusOpt = modusRepo.findById(modusId);
        return getTonalKey(quintCircleKeyOpt, modusOpt);
    }

    @Transactional
    public TonalKey getTonalKeyByLabels(String quintCircleKeyLabel, ModusLabel modusLabel) {
        var quintCircleKeyOpt = quintCircleKeyRepo.findByLabel(quintCircleKeyLabel);
        var modusOpt = modusRepo.findByLabel(modusLabel);
        return getTonalKey(quintCircleKeyOpt, modusOpt);
    }

    @Transactional
    public Map<Integer, TonalKey> extractHarmonySchemaMap(String keyScheme, TonalKey mainKey) {
        System.out.printf("TonalKeyService::extractHarmonySchemaMap from '%s'%n", keyScheme);
        // todo az budu pripraven na dur/moll zmeny, tady se budou dit veci!!!
        var mainKeyId = mainKey.getQuintCircleKey().getId();
        var modusId = mainKey.getModus().getId();

        var rawMap = PatternStringUtils.extractSchemaMap(keyScheme);
        Map<Integer, TonalKey> map = new HashMap<>();

        int quintCircleShifter;
        for (Integer measureIndex : rawMap.keySet()) {
            quintCircleShifter = rawMap.get(measureIndex);
            map.put(measureIndex,
                    getTonalKeyByQuintCircleKeyIdAndModusId(mainKeyId + quintCircleShifter, modusId)
            );
        }
        return map;
    }

}
