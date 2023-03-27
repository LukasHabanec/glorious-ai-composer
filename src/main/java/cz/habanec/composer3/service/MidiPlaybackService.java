package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.Composition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MidiPlaybackService {

    private final MidiSequencerService sequencerService;

    public void playMyComposition(Composition composition, Integer measureIndex) {
//        System.out.println("MidiPlaybackService::" + composition.getMelody().getMelodyMeasureList());
        if (measureIndex == null) { measureIndex = 0; }
        sequencerService.midiInit();
        sequencerService.feedMidiSequence(composition, measureIndex);
        sequencerService.play();
    }

    public void stopPlayingCurrentComposition() {
        sequencerService.stop();
    }
}
