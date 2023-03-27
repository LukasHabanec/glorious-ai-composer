package cz.habanec.composer3.repositories;

import cz.habanec.composer3.entities.TonalKey;
import cz.habanec.composer3.entities.assets.Modus;
import cz.habanec.composer3.entities.assets.QuintCircleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TonalKeyRepo extends JpaRepository<TonalKey, Long> {

//    @Query(nativeQuery = true, value = """
//        select * from keys k
//        left join assets_quint_circle aqc on k.quint_circle_key_id = aqc.id
//        left join assets_modi am on k.modus_id = am.id
//        where aqc.name = :quintCircleKey
//        and am.label = :modus
//""")
    @Query("""
        select k from TonalKey k
        where k.quintCircleKey.name = :quintCircleKey
        and k.modus.label = :modus
""")
    TonalKey findByQuintCircleKeyAndModus(String quintCircleKey, String modus);

    Optional<TonalKey> findByQuintCircleKeyAndModus(QuintCircleKey quintCircleKey, Modus modus);

    @Query("""
        select k from TonalKey k
        where k.quintCircleKey.id = :quintCircleKeyId
        and k.modus.id = :modusId
""")
    TonalKey findByQuintCircleKeyIdAndModusId(Long quintCircleKeyId, Long modusId);


}
