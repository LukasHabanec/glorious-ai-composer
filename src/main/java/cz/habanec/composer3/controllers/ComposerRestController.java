package cz.habanec.composer3.controllers;

import cz.habanec.composer3.entities.dto.CompositionDto;
import cz.habanec.composer3.entities.dto.AllCompositionsDto;
import cz.habanec.composer3.midi.MidiPlaybackService;
import cz.habanec.composer3.service.CompositionService;
import cz.habanec.composer3.service.PostProductionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/composer")
@RequiredArgsConstructor
public class ComposerRestController {

    private final MidiPlaybackService midiPlaybackService;
    private final CompositionService compositionService;
    private final PostProductionService postProductionService;

    @GetMapping("/compositions")
    public ResponseEntity<AllCompositionsDto> showSavedCompositions() {
        var compositionsDto = compositionService.getAllSavedCompositionsForView();
        return ResponseEntity.ok(compositionsDto);
    }

    @GetMapping("/composition/{id}")
    public ResponseEntity<CompositionDto> showComposition(
            @PathVariable(name = "id") Long compositionId
    ) {
        var composition = compositionService.getCurrentCompositionForView(compositionId);
        return ResponseEntity.ok(composition);
    }

    @GetMapping("/composition/{id}/play")
    public ResponseEntity<String> playComposition(
            @PathVariable(name = "id") Long compositionId,
            @RequestParam(name = "measure", required = false) Integer measureIndex
    ) {
        var myComposition = compositionService.getCurrentComposition(compositionId);
        midiPlaybackService.playMyComposition(myComposition, measureIndex);
        return ResponseEntity.ok("Playing composition from measure " + (measureIndex + 1));
    }

    @GetMapping("/composition/{id}/stop")
    public ResponseEntity<String> stopPlayingComposition(
            @PathVariable(name = "id") Long compositionId
    ) {
        midiPlaybackService.stopPlayingCurrentComposition();
        return ResponseEntity.ok("Playing stopped.");
    }

    @GetMapping("/composition/{id}/shift")
    public ResponseEntity<CompositionDto> shiftSpecialShifter(
            @PathVariable(name = "id") Long compositionId,
            @RequestParam(name = "measure") Integer measureIndex,
            @RequestParam(name = "shifter") Integer shifter
    ) {
        var currentComposition = compositionService.getCurrentComposition(compositionId);
        postProductionService.shiftUserSpecialShifters(currentComposition, measureIndex, shifter);

        return ResponseEntity.ok(compositionService.getCurrentCompositionForView(compositionId));
    }

    @GetMapping("/composition/{id}/save")
    public String saveComposition(
            @PathVariable(name = "id") Long compositionId,
            RedirectAttributes att) {
        String message = compositionService.saveCurrentComposition(compositionId);
        compositionId = compositionService.getCurrentComposition().getId();
        att.addFlashAttribute("message", message);
        return "redirect:/composer/composition/" + compositionId;
    }

    @DeleteMapping("/composition/{id}/delete")
    public ResponseEntity<String> deleteComposition(
            @PathVariable(name = "id") Long compositionId
    ) {
        String message = compositionService.deleteCompositionById(compositionId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/composition/{id}/change")
    public String makeChanges(
            @PathVariable(name = "id") Long compositionId,
            @ModelAttribute(name = "changeRequest") ChangeRequestDto dto,
//            @RequestParam(name = "key") Integer quintCircleIndex,
//            @RequestParam(name = "modus") Integer modeIndex,
//            @RequestParam Integer startingGrade,
//            @RequestParam(name = "isMelodyOn", defaultValue = "false") boolean isMelodyOn,
//            @RequestParam(name = "isAccompanimentOn", defaultValue = "false") boolean isAccompanimentOn,
            RedirectAttributes att
    ) {
        System.out.println(dto);
//        postProductionService.makeUpMyComposition(tempo, quintCircleIndex, modeIndex, startingGrade, isMelodyOn, isAccompanimentOn);

        att.addFlashAttribute("message", "Changes made.");
        return "redirect:/composer/composition/" + compositionId;
    }

    @GetMapping("/composition/export-midi")
    public String exportMidi(RedirectAttributes att) {
        var composition = compositionService.getCurrentComposition();
        if (midiPlaybackService.exportMidi(composition)) {
            att.addFlashAttribute("message", "Successfully saved.");
        } else {
            att.addFlashAttribute("message", "Error, not saved.");
        }
        return "redirect:/composer/composition/" + composition.getId();
    }

    @Getter
    @Setter
    public static class ChangeRequestDto {
        int tempo;
        boolean isMelodyOn;
    }

}
