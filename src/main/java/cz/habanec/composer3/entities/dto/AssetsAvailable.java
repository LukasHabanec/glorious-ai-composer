package cz.habanec.composer3.entities.dto;

import cz.habanec.composer3.entities.enums.ModusLabel;
import cz.habanec.composer3.entities.enums.ToneLabel;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class AssetsAvailable {

    private List<String> modiAvailable;
    private List<String> keysAvailable;

    public AssetsAvailable() {
        modiAvailable = Arrays.stream(ModusLabel.values()).map(Enum::toString).toList();
        keysAvailable = Arrays.stream(ToneLabel.values()).map(Enum::toString).toList();
    }
}
