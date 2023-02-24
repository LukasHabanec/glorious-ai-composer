package cz.habanec.composer3.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compositions")
@EqualsAndHashCode(of = "id")
public class Composition {

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

    @Column(name = "harmony_fragmentation")
    private String harmonyFragmentationScheme;

    @Column(name = "harmony_figuration")
    private String harmonyFigurationScheme;

    @Column(name = "harmony_rhythm_scheme")
    private String harmonyRhythmScheme;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "melody_id", nullable = false)
    private Melody melody;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accompaniment_id", nullable = false)
    private Accompaniment accompaniment;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Embedded
    @AttributeOverride(name = "tempo", column = @Column(name = "midi_tempo"))
    @AttributeOverride(name = "resolution", column = @Column(name = "midi_resolution"))
    @AttributeOverride(name = "melodyOn", column = @Column(name = "midi_melody_on"))
    @AttributeOverride(name = "accompanimentOn", column = @Column(name = "midi_accompaniment_on"))
    private MidiSettings midiSettings;



    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MidiSettings {
        private int tempo;
        private int resolution;
        private boolean melodyOn;
        private boolean accompanimentOn;
    }
}
