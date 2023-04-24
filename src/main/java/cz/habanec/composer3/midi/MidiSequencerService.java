package cz.habanec.composer3.midi;

import cz.habanec.composer3.entities.Composition;
import cz.habanec.composer3.utils.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;
import static javax.sound.midi.ShortMessage.PROGRAM_CHANGE;

@Component
public class MidiSequencerService {

    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;
    private int resolution;
    private int tickPerMeasure;
    private boolean isPlaying;
//    private FragmentScriptor midiScriptor;

    private static final int MELODY_CHANNEL = 0;
    private static final int ACCOMPANIMENT_CHANNEL = 1;


    public void midiInit() {
        if (isPlaying) {
            stop();
        }
        resolution = Properties.DEFAULT_MIDI_RESOLUTION;
//        midiScriptor = new FragmentScriptor(resolution);
        sequencer = null;
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();

            /*sequencer.addMetaEventListener(new MetaEventListener() {
                public void meta(MetaMessage m) { // A message of this type is automatically sent when we reach the end of the track
                    if (m.getType() == countOfMeasures) {
                        stop();
                        System.exit(0);
                    }
                }
            });*/

            sequence = new Sequence(Sequence.PPQ, resolution);
            track = sequence.createTrack();
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void feedMidiSequence(Composition composition, int initialMeasure) {

        sequencer.setTempoInBPM(composition.getTempo());
        if(composition.getMidiSettings().getMelodyOn()) {
            populateMelodyByAddingMeasuresOntoTrack(composition, initialMeasure);
        }
        if(composition.getMidiSettings().getAccompanimentOn()) {
//            populateAccompanimentByFields(composition, initialMeasure);
        }
    }

//    private void populateAccompanimentByFields(Composition composition, int initialMeasure) {
//        setInstrument(ACCOMPANIMENT_CHANNEL, 0, 0);
//        int countOfFields = composition.getAccompaniment().getHarmonyFieldList().size();
//        int idOfTheFirstFieldBelongingToInitialMeasure =
//                composition.getAccompaniment().getHarmonyFieldList().stream()
//                        .filter(n -> n.getBelongsToMeasure() == initialMeasure)
//                        .findFirst().get().getId();
//        int tick = 0;
//        for (int id = idOfTheFirstFieldBelongingToInitialMeasure; id < countOfFields; id++) {
//            makeOneFieldWithFigurationSelect(composition, tick, id);
//            tick += composition.getAccompaniment().getHarmonyFieldList().get(id)
//                    .getLengthInBeats() * resolution;
//        }
//    }

//    private void makeOneFieldWithFigurationSelect(Composition composition,
//                                                  int startingTick,
//                                                  int fieldId) {
//        HarmonyField thisField = composition.getAccompaniment().getHarmonyFieldList().get(fieldId);
//        List<MidiTone> toneList = midiScriptor.scriptFragment(composition.getMelody(), thisField, startingTick);
////        System.out.print("field id" + fieldId + ": ");
//        if (toneList == null || toneList.isEmpty()) { return; }
//        for (MidiTone midiTone : toneList) {
////            System.out.print(midiTone.getHigh() + ", ");
//            addMidiEventOntoTrack(ACCOMPANIMENT_CHANNEL,
//                    midiTone.getHigh(), midiTone.getVolume(), midiTone.getTick(), midiTone.getLength());
//        }
////        System.out.println(thisField.getCurrentKey() + thisField.getCurrentHarmonyGrade().getChord().getHarmonyFunction().getGrade().toString());
//    }

    private void populateMelodyByAddingMeasuresOntoTrack(Composition composition, int initialMeasure) {
        setInstrument(MELODY_CHANNEL, 0, 0);

        var measureList = composition.getMelody().getMelodyMeasureList();
        int countOfMeasures = composition.getMelody().getMelodyMeasureList().size();
        int tick = 0;

        for (int i = initialMeasure; i < countOfMeasures; i++) {

            var measure = measureList.get(i);

            makeMelodyMeasureByAddingOntoTrack(
                    measure.getCurrentKey().getKeyMidiValues().getScale(),
                    tick,
                    measure.getFirstToneIndex() + measure.getUserSpecialShifter(),
                    measure.getRhythmPattern().getValues(),
                    measure.getRealTunePatternValues()
            );
            tick += measure.getTimeSignature().getMidiLength();
        }
    }

    private void makeMelodyMeasureByAddingOntoTrack(
            List<Integer> keyValues,
            int initialTick,
            int initialToneIndex,
            List<Integer> rhythmValues,
            List<Integer> tuneValues) {

        int tick = initialTick;
        int high;
        int volume = 100;
        int valuesSize = rhythmValues.size();
        int volumeShifter = 30 / valuesSize;
        for (int i = 0; i < valuesSize; i++) {
            high = initialToneIndex + tuneValues.get(i);

            if (i > 0 && i <= valuesSize / 2) { volume -= volumeShifter; }
            else if (i > valuesSize / 2) { volume += volumeShifter; }

            addMidiEventOntoTrack(MELODY_CHANNEL, keyValues.get(high), volume, tick, rhythmValues.get(i));
            tick += rhythmValues.get(i);
        }
    }
//    public void feedTrackWithBeat() {
//        for (int i = 0; i < countOfMeasures * tickPerMeasure; i += 2) { // DRUMS PATTERN
//            if (i % 16 - 8 == 0) {
//                makeTone(9, 38, 100, i, 1);
//            }
//            if (i % countOfMeasures == 0) {
//                makeTone(9, 36, 100, i, 1);
//            }
//            makeTone(9, 44, 70, i, 1);
//        }
//    }


    public void play() {
        System.out.println("MidiSequencerService::play:");
        try {
            sequencer.setSequence(sequence);
            isPlaying = true;
            sequencer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean record(Composition composition, String filename, String hash) {
        feedMidiSequence(composition, 0);
        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
        if (allowedTypes.length == 0) {
            System.err.println("No supported MIDI file types.");
            return false;
        } else {
            try {
                MidiSystem.write(sequence, allowedTypes[0], new File(filename + "-" + hash + ".mid"));
                System.out.println("Successfully saved.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("No success saving midi file.");
                return false;
            }
//            System.exit(0);
        }
        return true;
    }


    private void setInstrument(int chan, int instrument, int tick) {
        try {
            track.add(new MidiEvent(new ShortMessage(PROGRAM_CHANGE, chan, instrument, 0), tick));
        } catch (Exception e) {
            System.out.println("Event not created.");
        }
    }

    private void addMidiEventOntoTrack(int chan, int high, int vol, int tick, int len) {
        try {
            track.add(new MidiEvent(new ShortMessage(NOTE_ON, chan, high, vol), tick));
            track.add(new MidiEvent(new ShortMessage(NOTE_OFF, chan, high, vol), tick + len));
        } catch (Exception e) {
            System.out.println("Event not created.");
        }
    }

    public void stop() {
        isPlaying = false;
        sequencer.stop();
    }
}
