package cz.habanec.composer3.entities.assets;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("melody_tune")
public class MelodyTunePattern extends Pattern {

    public MelodyTunePattern(String body) {
        super(body);
    }

    public MelodyTunePattern(String body, Long formId) {
        super(body, formId);
    }

    public void setBody(String body) {
        super.body = body;
    }

}
