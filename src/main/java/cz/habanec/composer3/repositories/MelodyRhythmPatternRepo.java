package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.assets.MelodyRhythmPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MelodyRhythmPatternRepo extends JpaRepository<MelodyRhythmPattern, Long> {

    List<MelodyRhythmPattern> findAllByFormAssociationId(Long id);

    Optional<MelodyRhythmPattern> findByBodyAndFormAssociationId(String body, Long formId);
}
