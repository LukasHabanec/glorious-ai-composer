package cz.habanec.composer3.entities;

import cz.habanec.composer3.entities.assets.Pattern;
import cz.habanec.composer3.entities.enums.Eccentricity;
import cz.habanec.composer3.utils.PatternStringUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = "body")
@Table(name = "melody_tune_patterns")
public class MelodyTunePattern implements Pattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "body", nullable = false, unique = true)
    private String body;

    @Column(name = "tone_amount")
    private Integer toneAmount;

    @Column(name = "ambitus")
    private Integer ambitus;

    @Enumerated(EnumType.STRING)
    private Eccentricity eccentricity;

    @Column(name = "rnd_created")
    private Boolean rndCreated;

    @Column(name = "form_association_id")
    private Long formAssociationId;

    @Transient
    private List<Integer> values;

    @Transient
    public List<Integer> getValues() {
        if (Objects.isNull(values)) {
            values = PatternStringUtils.extractIntegerListFrom(body, PatternStringUtils.COMMA_REGEX_DELIMITER);
        }
        return values;
    }

}
