package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.assets.QuintCircleKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuintCircleKeyRepo extends JpaRepository<QuintCircleKey, Long> {

    QuintCircleKey findByName(String name);

    Optional<QuintCircleKey> findByLabel(String label);

}
