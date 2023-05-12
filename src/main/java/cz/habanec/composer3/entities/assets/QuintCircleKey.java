package cz.habanec.composer3.entities.assets;

import cz.habanec.composer3.entities.enums.ToneLabel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assets_quint_circle")
@EqualsAndHashCode(of = {"id", "label"})
@ToString(of = {"label"})
public class QuintCircleKey {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true)
    private ToneLabel name;

    @Column(name = "label", unique = true)
    private String label;

    @Column(name = "midi_ini_tone")
    private Integer midiIniTone;

}
