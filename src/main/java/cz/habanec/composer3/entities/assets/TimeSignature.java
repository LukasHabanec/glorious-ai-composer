package cz.habanec.composer3.entities.assets;

import cz.habanec.composer3.entities.enums.NoteLength;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "time_signatures", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "num_of_beats", "note_length" }) })
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "label"})
public class TimeSignature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "num_of_beats", nullable = false)
    private Integer numOfBeats;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "note_length", nullable = false)
    private NoteLength noteLength;

}
