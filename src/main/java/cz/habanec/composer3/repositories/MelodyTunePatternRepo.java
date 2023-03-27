package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.assets.MelodyRhythmPattern;
import cz.habanec.composer3.entities.assets.MelodyTunePattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MelodyTunePatternRepo extends JpaRepository<MelodyTunePattern, Long> {

    List<MelodyTunePattern> findAllByFormAssociationId(Long id);

    Optional<MelodyTunePattern> findByBodyAndFormAssociationId(String body, Long formId);
}
