package cz.habanec.composer3.entities.assets;

import cz.habanec.composer3.utils.PatternStringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DiscriminatorOptions;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "assets_patterns", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "form_association_id", "body" }) })
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "body"})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@DiscriminatorOptions(force = true)
@ToString(of = "body")
public abstract class Pattern {

    public Pattern (String body) {
        this.body = body;
    }

    public Pattern (String body, Long formId) {
        this.body = body;
        formAssociationId = formId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "body", nullable = false)
    private String body;

    @Setter
    @Column(name = "form_association_id")
    private Long formAssociationId;

    @Transient
    protected List<Integer> values;

    @Transient
    public List<Integer> getValues() {
        if (Objects.isNull(values)) {
            values = PatternStringUtils.extractIntegerListFrom(getBody(), PatternStringUtils.COMMA_REGEX_DELIMITER);
        }
        return values;
    }

}
