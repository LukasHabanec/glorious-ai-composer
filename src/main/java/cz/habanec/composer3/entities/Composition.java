package cz.habanec.composer3.entities;

import cz.habanec.composer3.entities.assets.TimeSignature;
import cz.habanec.composer3.entities.dto.AssetsAvailable;
import cz.habanec.composer3.entities.enums.ModusLabel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compositions")
@EqualsAndHashCode(of = "id")
@ToString(of = {"title"})
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tonic_key_id", nullable = false)
    private TonalKey tonicKey;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time_signature_id", nullable = false)
    private TimeSignature timeSignature;

    @Column
    private Integer tempo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_id", nullable = false)
    private CompositionForm form;

    @OneToOne(mappedBy = "composition", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Melody melody;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "accompaniment_id", nullable = false)
//    private Accompaniment accompaniment;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @OneToOne(mappedBy = "composition", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private MidiSettings midiSettings;

    @Transient
    private AssetsAvailable assetsAvailable;

    @Transient
    public AssetsAvailable getAssetsAvailable() {
        if (Objects.isNull(assetsAvailable)) {
            assetsAvailable = new AssetsAvailable();
        }
        return assetsAvailable;
    }
}
