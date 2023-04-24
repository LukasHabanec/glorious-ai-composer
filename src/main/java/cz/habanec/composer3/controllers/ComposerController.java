package cz.habanec.composer3.controllers;

import cz.habanec.composer3.service.CompositionService;
import cz.habanec.composer3.midi.MidiPlaybackService;
import cz.habanec.composer3.service.PostProductionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/composer")
@RequiredArgsConstructor
public class ComposerController {

    private final MidiPlaybackService midiPlaybackService;
    private final CompositionService compositionService;
    private final PostProductionService postProductionService;

    @GetMapping("/composition/{id}")
    public String showComposition(Model model, @PathVariable(name = "id") Long compositionId) {
        model.addAttribute("composition", compositionService.getCurrentCompositionForView(compositionId));
        model.addAttribute("changeRequest", new ChangeRequestDto());
        return "old-view";
    }

    @GetMapping("/composition/{id}/play")
    public String playComposition(
            @PathVariable(name = "id") Long compositionId,
            @RequestParam(name = "measure", required = false) Integer measureIndex,
            RedirectAttributes att) {
        var myComposition = compositionService.getCurrentComposition(compositionId);
        midiPlaybackService.playMyComposition(myComposition, measureIndex);
        att.addFlashAttribute("message", "Playing...");

        return "redirect:/composer/composition/" + compositionId;
    }

    @GetMapping("/composition/{id}/stop")
    public String stopPlayingComposition(
            @PathVariable(name = "id") Long compositionId,
            RedirectAttributes att) {
        midiPlaybackService.stopPlayingCurrentComposition();
        att.addFlashAttribute("message", "Stopped.");
        return "redirect:/composer/composition/" + compositionId;
    }

    @GetMapping("/composition/{id}/shiftDown")
    public String shiftDown(
            @PathVariable(name = "id") Long compositionId,
            @RequestParam(name = "index") Integer measureIndex,
            RedirectAttributes att) {

        var currentComposition = compositionService.getCurrentComposition(compositionId);
        postProductionService.shiftUserSpecialShifters(currentComposition, measureIndex, -1);

        att.addFlashAttribute("message", "Measure " + (measureIndex + 1) + " shifted down.");
        return "redirect:/composer/composition/" + compositionId;
    }

    @GetMapping("/composition/{id}/shiftUp")
    public String shiftUp(
            @PathVariable(name = "id") Long compositionId,
            @RequestParam(name = "index") Integer measureIndex,
            RedirectAttributes att) {

        var currentComposition = compositionService.getCurrentComposition(compositionId);
        postProductionService.shiftUserSpecialShifters(currentComposition, measureIndex, 1);

        att.addFlashAttribute("message", "Measure " + (measureIndex + 1) + " shifted up.");
        return "redirect:/composer/composition/" + compositionId;
    }

    @GetMapping("/composition/{id}/save")
    public String saveComposition(
            @PathVariable(name = "id") Long compositionId,
            RedirectAttributes att) {
        String message = compositionService.saveCurrentComposition(compositionId);
        att.addFlashAttribute("message", message);
        return "redirect:/composer/composition/" + compositionId;
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
