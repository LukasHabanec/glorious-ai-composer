package cz.habanec.composer3.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

}
