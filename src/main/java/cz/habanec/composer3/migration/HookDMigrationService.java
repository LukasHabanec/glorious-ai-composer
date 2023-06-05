package cz.habanec.composer3.migration;

import cz.habanec.composer3.entities.Composition;
import cz.habanec.composer3.entities.CompositionForm;
import cz.habanec.composer3.repositories.CompositionFormRepo;
import cz.habanec.composer3.repositories.MelodyRhythmPatternRepo;
import cz.habanec.composer3.repositories.MelodyTunePatternRepo;
import cz.habanec.composer3.service.CompositionBuilder;
import cz.habanec.composer3.service.CompositionBuilder.NewCompositionIngredients;
import cz.habanec.composer3.service.PatternService;
import cz.habanec.composer3.service.TimeSignatureService;
import cz.habanec.composer3.service.TonalKeyService;
import cz.habanec.composer3.utils.AlphabetUtils;
import cz.habanec.composer3.utils.PatternStringUtils;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static cz.habanec.composer3.utils.PatternStringUtils.*;

@Service
@RequiredArgsConstructor
public class HookDMigrationService {

    public static final String HOOK_FORM_KEY = "Hook D";

    private final DataSource dataSource;
    private final TonalKeyService tonalKeyService;
    private final CompositionFormRepo compositionFormRepo;
    private final MelodyRhythmPatternRepo rhythmPatternRepo;
    private final MelodyTunePatternRepo tunePatternRepo;
    private final CompositionBuilder compositionBuilder;
    private final PatternService patternService;
    private final TimeSignatureService timeSignatureService;

    @Transactional //TODO figurations, exceptions
    public Composition migrateOldHookCompositionFrom(MigratingCompositionIngredients ingredients) {
        System.out.println("MigrationService::migrateOldComposition " + ingredients.getHash());

        var mainKey = tonalKeyService.getTonalKeyByQuintCircleKeyIdAndModusId(
                ingredients.getMainKeyIndex() + 1L,
                ingredients.getModeIndex() + 1L);

        var form = compositionFormRepo.findByTitle(ingredients.getFormName()).orElseThrow();
        var formId = form.getId();
        var timeSignature = timeSignatureService.fetchOrCreateTimeSignature("4/4");

        int tunePatternsRequiredCount = getUniqueSymbolsCount(form.getMelodyTuneScheme());
        int rhythmPatternsRequiredCount = getUniqueSymbolsCount(form.getMelodyRhythmScheme());

        var shuffledRhythmIndexes = extractIntegerListFrom(ingredients.getShuffledRhythm(), WHITESPACE_REGEX_DELIMITER)
                .subList(0, rhythmPatternsRequiredCount);
        var shuffledTuneIndexes = extractIntegerListFrom(ingredients.getShuffledTune(), WHITESPACE_REGEX_DELIMITER)
                .subList(0, tunePatternsRequiredCount);

        var rhythmPatterns = rhythmPatternRepo.findAllByFormAssociationId(formId);
        var tunePatterns = tunePatternRepo.findAllByFormAssociationId(formId);

        var reconstructedRhythmPatterns = patternService.getRequiredPatternListUsingShuffledIndexes(
                shuffledRhythmIndexes, rhythmPatterns);
        var reconstructedTunePatterns = patternService.getRequiredPatternListUsingShuffledIndexes(
                shuffledTuneIndexes, tunePatterns);

        var composition = compositionBuilder.buildNewComposition(NewCompositionIngredients.builder()
                .title(ingredients.getHash() + " " + AlphabetUtils.generateRandomTwoWordsName())
                .rhythmPatterns(reconstructedRhythmPatterns)
                .tunePatterns(reconstructedTunePatterns)
                .timeSignature(timeSignature)
                .form(form)
                .mainKey(mainKey)
                .build());

        updateShifters(composition, ingredients.getShifters(), ingredients.getStartingGrade());

        return composition;
    }

    private void updateShifters(Composition composition, String shifters, int startingGrade) {
        var userSpecialShifters = extractIntegerListFrom(shifters, WHITESPACE_REGEX_DELIMITER);
        if (startingGrade == 0 && userSpecialShifters.stream().allMatch(a -> a == 0)) {
            return;
        }
        var measures = composition.getMelody().getMelodyMeasureList();
        for (int i = 0; i < measures.size(); i++) {
            measures.get(i).setUserSpecialShifter(userSpecialShifters.get(i) + startingGrade);
        }
    }

    private List<String> readFile(String filename) {
        try {
            return Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Composition loadCompositionFromTxt(String filename) {
        List<String> lines = readFile(filename);
        if (CollectionUtils.isEmpty(lines)) {
            return null;
        }
        return migrateOldHookCompositionFrom(MigratingCompositionIngredients.builder()
                .hash(lines.get(8))
                .formName(HOOK_FORM_KEY)
                .shuffledRhythm(lines.get(3))
                .shuffledTune(lines.get(4))
                .figurations(lines.get(6))
                .shifters(lines.get(5))
                .mainKeyIndex(Integer.parseInt(lines.get(1).substring(2)))
                .modeIndex(Integer.parseInt(lines.get(1).substring(0, 1)))
                .startingGrade(Integer.parseInt(lines.get(2)))
                .tempo(Integer.parseInt(lines.get(7)))
                .build());
    }

    public void migrateAssets() {
//		execSql("db/migration/migrate-modi.sql");
//		execSql("db/migration/migrate-quint-circle.sql");
        execSql("db/migration/migrate-patterns-english.sql");
//		execSql("db/migration/migrate-form-english.sql");

        var form = compositionFormRepo.findByTitle(HOOK_FORM_KEY).orElseThrow();

        makeUpHookDMelodyTunePatterns(form);
        makeUpHookDMelodyRhythmPatterns(form);
    }

    @Transactional
    private void makeUpHookDMelodyRhythmPatterns(CompositionForm form) {
        var existingPatterns = rhythmPatternRepo.findAllByFormAssociationId(form.getId());
        var hookDtimeSignature = timeSignatureService.fetchOrCreateTimeSignature("4/4");

        existingPatterns.stream()
                .forEach(pattern -> {

                    System.out.println(pattern);
                    var values = convertResolutionFourToFourtyEight(pattern.getBody());
                    System.out.println(values);
                    pattern.setGranularity(PatternStringUtils.extractShortestNoteLength(values));
                    pattern.setRndGenerated(false);
                    pattern.setValuesCount(values.size());
                    pattern.setTimeSignature(hookDtimeSignature);
                    pattern.setBody(convertRhythmValuesToLabelsAndStringify(values));
                    System.out.println(pattern + ", Granularity: " + pattern.getGranularity());
                });
    }

    private List<Integer> convertResolutionFourToFourtyEight(String body) {
        var values = PatternStringUtils.extractIntegerListFrom(body, COMMA_REGEX_DELIMITER);
        return values.stream().map(value -> value * 12).toList();
    }

    @Transactional
    private void makeUpHookDMelodyTunePatterns(CompositionForm form) {
        tunePatternRepo.findAllByFormAssociationId(form.getId())
                .forEach(pattern -> {

                    System.out.println(pattern);
                    var values = pattern.getValues();
                    pattern.setToneAmount(PatternStringUtils.getUniqueValuesCount(values));
                    pattern.setAmbitus(PatternStringUtils.getTunePatternsAmbitus(values));
                    pattern.setBody(PatternStringUtils.joinIntListWithCommas(values));
                    pattern.setRndCreated(false);
                    System.out.println(pattern + " toneAmount: " + pattern.getToneAmount()
                            + ", ambitus: " + pattern.getAmbitus());
                });
    }

    public void execSql(String... sqlFile) {
        try (Connection connection = dataSource.getConnection()) {
            Arrays.stream(sqlFile).forEach(file -> {
                Resource resource = new ClassPathResource(file);
                try {
                    ScriptUtils.executeSqlScript(connection, resource);
                } catch (ScriptException e) {
                    throw new RuntimeException("Failed to execute script '" + resource.getFilename() + "'.", e);
                }
            });

        } catch (SQLException e) {
            throw new RuntimeException("No connection acquired.");
        }
    }

    @Data
    @Builder
    public static class MigratingCompositionIngredients {
        String hash, formName, shuffledRhythm, shuffledTune, shifters, figurations;
        int mainKeyIndex, modeIndex, startingGrade, tempo;
    }

}
