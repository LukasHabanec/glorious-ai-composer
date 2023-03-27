package cz.habanec.composer3.entities.assets;

import cz.habanec.composer3.utils.PatternStringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assets_modi")
@EqualsAndHashCode(of = {"id", "label"})
@ToString(of = {"label"})
public class Modus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "label", unique = true)
    private String label;

    @Column(name = "intervals", unique = true)
    private String intervalsString;

    @Column(name = "enabled")
    private boolean enabled;

    @Transient
    public List<Integer> getIntervals() {
        return PatternStringUtils.extractIntegerListFrom(intervalsString, PatternStringUtils.COMMA_REGEX_DELIMITER);
    }

    @Transient
    public boolean isDiatonical() {
        return getIntervals().size() == 7;
    }
}
