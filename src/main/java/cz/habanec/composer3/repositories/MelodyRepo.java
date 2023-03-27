package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.Melody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MelodyRepo extends JpaRepository<Melody, Long> {

}
