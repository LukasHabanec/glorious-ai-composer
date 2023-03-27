package cz.habanec.composer3.entities.assets;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@DiscriminatorValue("melody_tune")
public class MelodyTunePattern extends Pattern {

    public MelodyTunePattern (String body) {
        super(body);
    }

    public MelodyTunePattern (String body, Long formId) {
        super(body, formId);
    }

}
