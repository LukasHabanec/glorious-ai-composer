package cz.habanec.composer3.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "melodies")
@EqualsAndHashCode(of = "id")
@ToString(of = {"melodyMeasureList"})
public class Melody {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "composition_id", nullable = false)
    private Composition composition;

    @OneToMany(mappedBy = "melody", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("measureIndex asc")
    private List<MelodyMeasure> melodyMeasureList;

}
