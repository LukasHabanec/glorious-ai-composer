package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.assets.Modus;
import cz.habanec.composer3.entities.enums.ModusLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModusRepo extends JpaRepository<Modus, Long> {

    Optional<Modus> findByLabel(ModusLabel label);

}
