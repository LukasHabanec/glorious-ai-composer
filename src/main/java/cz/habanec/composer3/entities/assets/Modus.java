package cz.habanec.composer3.entities.assets;

import cz.habanec.composer3.entities.enums.ModusLabel;
import cz.habanec.composer3.utils.PatternStringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

import static cz.habanec.composer3.utils.PatternStringUtils.extractIntegerListFrom;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "label", unique = true)
    private ModusLabel label;

    @Column(name = "intervals", unique = true)
    private String intervalsString;

    @Column(name = "enabled")
    private boolean enabled;

    @Transient
    private List<Integer> intervals;

    @Transient
    public List<Integer> getIntervals() {
        if (Objects.isNull(intervals)) {
            intervals = extractIntegerListFrom(intervalsString, PatternStringUtils.COMMA_REGEX_DELIMITER);
        }
        return intervals;
    }

    @Transient
    public boolean isDiatonical() {
        return getIntervals().size() == 7;
    }
}
