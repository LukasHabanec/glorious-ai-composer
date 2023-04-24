package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.assets.TimeSignature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeSignatureRepo extends JpaRepository<TimeSignature, Long> {

    Optional<TimeSignature> findByLabel(String label);

}
