package cz.habanec.composer3.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@Entity
@Table(name = "composition_forms", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "title", "measure_count" }) })
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"title"})
public class CompositionForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "measure_count", nullable = false)
    private Integer measureCount;

    @Column(name = "melody_rhythm_scheme", nullable = false)
    private String melodyRhythmScheme;

    @Column(name = "melody_tune_scheme", nullable = false)
    private String melodyTuneScheme;

    @Column(name = "key_scheme", nullable = false)
    private String keyScheme;

    @Column(name = "harmony_fragmentation")
    private String harmonyFragmentationScheme;

    @Column(name = "harmony_figuration")
    private String harmonyFigurationScheme;

    @Column(name = "harmony_rhythm_scheme")
    private String harmonyRhythmScheme;

}
