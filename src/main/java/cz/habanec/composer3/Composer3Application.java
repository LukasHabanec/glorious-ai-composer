package cz.habanec.composer3;

import cz.habanec.composer3.creators.CompositionCreator;
import cz.habanec.composer3.creators.CompositionFormCreator;
import cz.habanec.composer3.creators.RhythmPatternCreator;
import cz.habanec.composer3.creators.TunePatternCreator;
import cz.habanec.composer3.entities.CompositionForm;
import cz.habanec.composer3.entities.enums.Eccentricity;
import cz.habanec.composer3.entities.enums.ModusLabel;
import cz.habanec.composer3.midi.MidiPlaybackService;
import cz.habanec.composer3.repositories.CompositionFormRepo;
import cz.habanec.composer3.repositories.CompositionRepo;
import cz.habanec.composer3.repositories.MelodyMeasureRepo;
import cz.habanec.composer3.repositories.TonalKeyRepo;
import cz.habanec.composer3.repositories.MelodyRepo;
import cz.habanec.composer3.repositories.MelodyRhythmPatternRepo;
import cz.habanec.composer3.repositories.MelodyTunePatternRepo;
import cz.habanec.composer3.repositories.ModusRepo;
import cz.habanec.composer3.repositories.QuintCircleKeyRepo;
import cz.habanec.composer3.service.*;
import cz.habanec.composer3.utils.AlphabetUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@SpringBootApplication
@RequiredArgsConstructor
public class Composer3Application implements CommandLineRunner {

    private final HookDMigrationService hookDMigrationService;
    private final MidiPlaybackService midiPlaybackService;
    private final CompositionService compositionService;
    private final TonalKeyService tonalKeyService;
    private final DataSource dataSource;
    private final ModusRepo modusRepo;
    private final QuintCircleKeyRepo quintCircleKeyRepo;
    private final CompositionRepo compositionRepo;
    private final CompositionFormRepo compositionFormRepo;
    private final MelodyRhythmPatternRepo rhythmPatternRepo;
    private final MelodyTunePatternRepo tunePatternRepo;
    private final TonalKeyRepo tonalKeyRepo;
    private final MelodyRepo melodyRepo;
    private final MelodyBuilder melodyBuilder;
    private final MelodyMeasureRepo melodyMeasureRepo;
    private final CompositionBuilder compositionBuilder;
    private final PatternService patternService;
    private final RhythmPatternCreator rhythmPatternCreator;
    private final TunePatternCreator tunePatternCreator;
    private final CompositionFormService formService;
    private final TimeSignatureService timeSignatureService;
    private final CompositionCreator compositionCreator;
    private final CompositionFormCreator formCreator;


    public static void main(String[] args) {
        SpringApplication.run(Composer3Application.class, args);
    }
// todo chci trojdoby takt a zmeny taktu vedene v patternu podobne jako keyschema
// todo chci mit moznost zobrazit, ktery pattern odpovida kteremu pismenu a rucne je zamenovat.
    // todo accomp chci přidat na požádání, až bude melodie - chci aby reagovala na hustotu melodie,
    //  spíš než na patternSchematech - ty jsou dementní, zvaž proč je nezrušit
    // todo uvaha - kazdou novou formu je treba prve ulozit, pak ji lze vybrat pro tvorbu nove skladby.
    //  - davam ji kratky nazev, mela by se pak objevit v nazvu skladby
    // todo new melody - kolik režimů? (
    //  Hook D
    //  Hybrid - pracuje s náhodnou formou ale s hook-patterny A NAOPAK
    //  Kompletně náhodně
    //  - musí být možnost psát formu ručně, vybírat z hotových, generovat i opakovaně
    //  - musí být možnost filtru povolených hodnot v rhythmPatternech
    //  - chci umožnit artikulační schéma : staccato, legato, rozličné obloučky, nátryl, trylek
    //
    // todo exception framework - rovnou s FE
    // done uklada se mi znovu a znovu Cdur, Hdur do tonal_keys
    // done nevyzkousel jsem distribuci vice klicu do taktu - je potreba vytvorit novy opus
    // done melodie kompletně chodí, je načase ustanovit frontend, dto system, controller
    // done nextMeasureShifter mozna vubec measure nepotrebuje drzet v pameti, staci si to v creatoru predat?
    // done chci umožnit repetování tónů v rámci tunePatternu - uvazuj o skupine vice tonu!!!
    // done novy koncept pro tunePattern
    // - bude vznikat bez opakovani, právě 9 hodnot (pro vice not system cykleni), ale
    // done moznost repetovat tony podle density 0 - 100 (100= vsechny stejne)
    // done vymysli, jestli radeji ukladat finalni podobu patternu do db, nebo ponechat jen raw 9-clennou verzi a k ni
    // done nove schema nesouci zaznam o repetovani, neco jako 0_2 = nulta hodnota dvakrat, 4_3 = ctvrta hodnota trikrat ???

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Running CL-runner");
//		hookDMigrationService.migrateAssets();


//		Arrays.stream(NoteLength.values()).map(NoteLength::getMidiValue).forEach(System.out::println);
//		compositionRepo.findAll().forEach(midiPlaybackService::exportMidi);
//        var formTitle = AlphabetUtils.generateRandomName(5);
//        var form = goNewForm(formTitle);
//        goNewComposition("Vuxyv");
    }

    private CompositionForm goNewForm(String title) {
        int measureCount = 12;
        var universalSchema = formCreator.createRandomLetterScheme(measureCount, 4, Eccentricity.HIGH);
        var newForm = CompositionForm.builder()
                .title(title)
                .measureCount(measureCount)
                .keyScheme("0_0 4_1 8_0")
                .melodyRhythmScheme(universalSchema)
                .melodyTuneScheme(universalSchema)
                .build();
        return compositionFormRepo.save(newForm);
    }

    void goNewComposition(String formTitle) {
        compositionCreator.createNewRandomComposition(CompositionCreator.RandomCompositionIngredients.builder()
                .formTitle(formTitle)
                .quintCircleKeyLabel("D")
                .modusLabel(ModusLabel.MAJOR)
                .timeSignatureLabel("3/4")
                .title(formTitle + " " + AlphabetUtils.generateRandomTwoWordsName())
                .build()
        );

    }

//		var composition = migrationService.migrateOldCompositionFrom(
//				MigrationService.MigratingCompositionIngredients.builder()
//						.formName(MigrationService.FOUR_MEASURE_TEST_FORM_KEY)
//						.mainKeyIndex(8)
//						.startingGrade(0)
//						.tempo(120)
//						.shuffledRhythm("19 20 22 2 12 3 11 9 15 6 21 14 23 16 17 0 13 1 7 4 24 10 8 5 18")
//						.shuffledTune("9 31 34 0 47 28 3 14 25 19 12 35 26 17 43 37 46 18 33 32 40 20 15 13 24 23 30 27 10 42 21")
//						.shifters("0 0 0 0")
//						.hash("Four")
//						.build()
//		);

//		var composition = migrationService.migrateOldCompositionFrom(
//				MigrationService.MigratingCompositionIngredients.builder()
//						.formName(MigrationService.HOOK_FORM_KEY)
//						.mainKeyIndex(8)
//						.startingGrade(0)
//						.tempo(120)
//						.shuffledRhythm("19 20 22 2 12 3 11 9 15 6 21 14 23 16 17 0 13 1 7 4 24 10 8 5 18")
//						.shuffledTune("9 31 34 0 47 28 3 14 25 19 12 35 26 17 43 37 46 18 33 32 40 20 15 13 24 23 30 27 10 42 21")
//						.shifters("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0")
//						.hash("Abcd")
//						.build()
//		);


}
