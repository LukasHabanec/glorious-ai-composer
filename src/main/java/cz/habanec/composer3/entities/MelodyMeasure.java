package cz.habanec.composer3.entities;

import cz.habanec.composer3.entities.assets.TimeSignature;
import cz.habanec.composer3.utils.MelodyMeasureUtils;
import cz.habanec.composer3.utils.PatternStringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

import static cz.habanec.composer3.utils.MelodyMeasureUtils.extractMelodyMatrix;
import static cz.habanec.composer3.utils.MelodyMeasureUtils.extractMelodyPatternForView;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "melody_measures")
@EqualsAndHashCode(of = "id")
@ToString(of = {"firstToneIndex", "rhythmPattern"})
public class MelodyMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "melody_id")
    private Melody melody;

    @Column
    private Integer measureIndex;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time_signature_id")
    private TimeSignature timeSignature;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rhythm_pattern_id")
    private MelodyRhythmPattern rhythmPattern;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tune_pattern_id")
    private MelodyTunePattern tunePattern;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "key_id")
    private TonalKey currentKey;

    @Column
    private Integer firstToneIndex;

    @Column
    private Integer userSpecialShifter;

    @Column
    private String repetitionSchema;

    @Transient
    private List<Integer> realTunePatternValues;

    @Transient
    private String melodyPatternForView;

    @Transient
    private int[] melodyMatrix;

    @Transient
    public List<Integer> getRealTunePatternValues() {
        if (Objects.isNull(realTunePatternValues)) {
            realTunePatternValues = MelodyMeasureUtils.extractRealTunePattern(
                    tunePattern.getValues(),
                    PatternStringUtils.extractSchemaMap(repetitionSchema),
                    rhythmPattern.getValuesCount()
            );
        }
        return realTunePatternValues;
    }

    @Transient
    public int[] getMelodyMatrix() {
        if (Objects.isNull(melodyMatrix)) {
            melodyMatrix = extractMelodyMatrix(
                    timeSignature.getMidiLength(),
                    firstToneIndex,
                    userSpecialShifter,
                    rhythmPattern.getValues(),
                    getRealTunePatternValues(),
                    currentKey
            );
        }
        return melodyMatrix;
    }

    @Transient
    public String getMelodyPatternForView() {
        if (Objects.isNull(melodyPatternForView)) {
            melodyPatternForView = extractMelodyPatternForView(
                    firstToneIndex,
                    userSpecialShifter,
                    rhythmPattern.getValues(),
                    getRealTunePatternValues(),
                    timeSignature
            );
        }
        return melodyPatternForView;
    }

    @Transient
    public void increaseUserSpecialShifter(int shifter) {
        userSpecialShifter += shifter;
        melodyMatrix = null;
        melodyPatternForView = null;
    }

//        resetHarmonyFields(); //todo
}
