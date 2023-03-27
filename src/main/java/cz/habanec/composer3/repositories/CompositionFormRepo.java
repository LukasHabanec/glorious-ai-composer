package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.CompositionForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompositionFormRepo extends JpaRepository<CompositionForm, Long> {

    Optional<CompositionForm> findByTitle(String title);

}
