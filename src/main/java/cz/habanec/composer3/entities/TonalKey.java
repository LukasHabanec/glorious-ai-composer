package cz.habanec.composer3.entities;

import cz.habanec.composer3.entities.assets.Modus;
import cz.habanec.composer3.entities.assets.QuintCircleKey;
import cz.habanec.composer3.midi.KeyMidiValues;
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
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@Table(name = "tonal_keys", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "quint_circle_key_id", "modus_id" }) })
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "label"})
public class TonalKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quint_circle_key_id")
    private QuintCircleKey quintCircleKey;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modus_id")
    private Modus modus;
    @Transient
    private KeyMidiValues keyMidiValues;

    public TonalKey(QuintCircleKey quintCircleKey, Modus modus) {
        this.quintCircleKey = quintCircleKey;
        this.modus = modus;
    }

//    public final GradeInContext[] myGradeScale;

    @Transient
    public KeyMidiValues getKeyMidiValues() {
        if (Objects.isNull(keyMidiValues)) {
            keyMidiValues = KeyMidiValues.from(quintCircleKey, modus);
        }
        return keyMidiValues;
    }

    @Override
    public String toString() {
        return String.format("%s %s", quintCircleKey.getName(), modus.getLabel());
    }

}
