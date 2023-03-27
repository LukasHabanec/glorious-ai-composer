package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.Composition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompositionRepo extends JpaRepository<Composition, Long> {

    @Query("""
select title from Composition 
""")
    List<String> findAllTitles();

    Optional<Composition> findByTitle(String title);

    @Query("""
select c from Composition c
where c.title = :title
""")
    Composition blabla(String title);
}
