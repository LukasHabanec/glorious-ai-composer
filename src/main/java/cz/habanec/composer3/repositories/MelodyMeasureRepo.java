package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.MelodyMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MelodyMeasureRepo extends JpaRepository<MelodyMeasure, Long> {

    List<MelodyMeasure> findAllByMelodyId(long melodyId);

}
