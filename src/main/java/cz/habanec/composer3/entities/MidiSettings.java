package cz.habanec.composer3.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MidiSettings {

    @Id
    @Column(name = "composition_id", nullable = false)
    private Long id;
    @Column
    private Integer resolution;
    @Column
    private Boolean melodyOn;
    @Column
    private Boolean accompanimentOn;
    @MapsId
    @JoinColumn(name = "composition_id")
    @OneToOne
    private Composition composition;

}
