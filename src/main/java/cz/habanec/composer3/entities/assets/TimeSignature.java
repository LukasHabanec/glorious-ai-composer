package cz.habanec.composer3.entities.assets;

import cz.habanec.composer3.entities.enums.NoteLength;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "label"})
@Table(name = "time_signatures")
public class TimeSignature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "num_of_beats", nullable = false)
    private Integer numOfBeats;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "beat", nullable = false)
    private NoteLength beat;

    @Column(name = "label", nullable = false, unique = true)
    private String label;

    @Transient
    private Integer midiLength;

    @Transient
    public int getMidiLength() {
        if (Objects.isNull(midiLength)) {
            midiLength = beat.getMidiValue() * numOfBeats;
        }
        return midiLength;
    }

}
