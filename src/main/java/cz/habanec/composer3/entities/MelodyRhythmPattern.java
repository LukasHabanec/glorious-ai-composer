package cz.habanec.composer3.entities;

import cz.habanec.composer3.entities.assets.Pattern;
import cz.habanec.composer3.entities.assets.TimeSignature;
import cz.habanec.composer3.entities.enums.NoteLength;
import cz.habanec.composer3.utils.PatternStringUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "melody_rhythm_patterns")
@ToString(of = "body")
public class MelodyRhythmPattern implements Pattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "body", nullable = false, unique = true)
    protected String body;

    @Column(name = "form_association_id")
    private Long formAssociationId;

    @Column(name = "granularity")
    @Enumerated(EnumType.STRING)
    private NoteLength granularity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time_signature_id")
    private TimeSignature timeSignature;

    @Column(name = "rnd_generated")
    private Boolean rndGenerated;

    @Column(name = "values_count")
    private Integer valuesCount;

    @Transient
    protected List<Integer> values;

    @Transient
    public List<Integer> getValues() {
        if (Objects.isNull(values)) {
            values = PatternStringUtils.convertRhythmLabelStringToValues(body);
        }
        return values;
    }

}

