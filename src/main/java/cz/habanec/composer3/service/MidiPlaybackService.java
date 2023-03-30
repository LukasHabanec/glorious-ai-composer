package cz.habanec.composer3.service;

import cz.habanec.composer3.entities.Composition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MidiPlaybackService {

    private final MidiSequencerService sequencerService;

    public void playMyComposition(Composition composition, Integer measureIndex) {
        if (measureIndex == null) { measureIndex = 0; }
        sequencerService.midiInit();
        sequencerService.feedMidiSequence(composition, measureIndex);
        sequencerService.play();
    }

    public void stopPlayingCurrentComposition() {
        sequencerService.stop();
    }

    public boolean exportMidi(Composition myComposition) {
        sequencerService.midiInit();
        return sequencerService.record(myComposition,
                "skladby/" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HH:mm")),
                myComposition.getId().toString()
        );
    }
}
