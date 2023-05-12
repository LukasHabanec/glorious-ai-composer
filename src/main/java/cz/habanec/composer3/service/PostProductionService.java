package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.Composition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostProductionService {

    private final CompositionService compositionService;

    public void shiftUserSpecialShifters(Composition composition, int measureIndex, int shifter) {
        var measureList = composition.getMelody().getMelodyMeasureList();
        for (int i = measureIndex; i < measureList.size(); i++) { // nenavysuje pouze jeden takt, ale vsechny nasledujici
            measureList.get(i).increaseUserSpecialShifter(shifter);
        }
//        composition.getAccompaniment().getAccompanimentService().runSmartAccordSuiteManager(
//                measureIndex,
//                composition.getAccompaniment().getHarmonyFieldList(),
//                composition.getAccompaniment().getMeasureList()
//        );
        System.out.printf("PostProductionService::shiftUserSpecialShifters shifted by %d from the measure %d%n",
                shifter, measureIndex +1 );
    }

}
