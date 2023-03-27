package cz.habanec.composer3.entities.assets;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("melody_rhythm")
public class MelodyRhythmPattern extends Pattern {

    public MelodyRhythmPattern(String body) {
        super(body);
    }

    public MelodyRhythmPattern(String body, Long formId) {
        super(body, formId);
    }

}

