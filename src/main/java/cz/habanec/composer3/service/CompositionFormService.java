package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.CompositionForm;
import cz.habanec.composer3.entities.TonalKey;
import cz.habanec.composer3.repositories.CompositionFormRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompositionFormService {

    private final CompositionFormRepo compositionFormRepo;

    public CompositionForm getFormByTitle(String title){
        return compositionFormRepo.findByTitle(title).orElseThrow();
    }

}
