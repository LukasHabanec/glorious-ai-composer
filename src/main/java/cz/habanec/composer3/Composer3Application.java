package cz.habanec.composer3;

import cz.habanec.composer3.entities.enums.NoteLength;
import cz.habanec.composer3.entities.enums.TunePatternEccentricity;
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
import cz.habanec.composer3.service.CompositionCreator.NewCompositionIngredients;
import cz.habanec.composer3.utils.AlphabetUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.Arrays;

import static cz.habanec.composer3.utils.ProbabilityUtils.RANDOM;

@SpringBootApplication
@RequiredArgsConstructor
public class Composer3Application implements CommandLineRunner {

	private final MigrationService migrationService;
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
	private final MelodyCreator melodyCreator;
	private final MelodyMeasureRepo melodyMeasureRepo;
	private final CompositionCreator compositionCreator;
	private final PatternService patternService;
	private final RhythmPatternCreator rhythmPatternCreator;
	private final TunePatternCreator tunePatternCreator;
	private final CompositionFormService formService;


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
//		migrateAssets();

//		var newForm = CompositionForm.builder()
//				.title("CL-runner")
//				.measureCount(8)
//				.keyScheme("0_0 5_-4 7_0")
//				.melodyRhythmScheme("AABACCBA")
//				.melodyTuneScheme("AABACCBA")
//				.build();
//		compositionFormRepo.save(newForm);

		Arrays.stream(NoteLength.values()).map(NoteLength::getMidiValue).forEach(System.out::println);

		newFromRandom("CL-runner");

	}


	public void newFromRandom(String formTitle) {
		final NoteLength[] RHYTHM_GRANULARITY_OPTIONS = {
				NoteLength.HALF_NOTE,
				NoteLength.QUARTER_NOTE,
				NoteLength.EIGHT_NOTE};
		final int[] TUNE_REPETITION_DENSITY_OPTIONS = {20, 40, 70};
		int[] repetitionDensityOptions = {
				RANDOM.nextInt(101),
				RANDOM.nextInt(101),
				RANDOM.nextInt(101),
		};
		final TunePatternEccentricity[] TUNE_ECCENTRICITY_OPTIONS = {
				TunePatternEccentricity.NO_ECCENTRICITY,
				TunePatternEccentricity.MID_ECCENTRICITY,
				TunePatternEccentricity.HIGH_ECCENTRICITY
		};
		final boolean[] rhythmEccentricOptions = {false, false, false};
		final int[] valueCountOptions = {
				RANDOM.nextInt(1, 16 + 1),
				RANDOM.nextInt(1, 16 + 1),
				RANDOM.nextInt(1, 16 + 1)
		};

		var form = formService.getFormByTitle(formTitle);
		var mainKey = tonalKeyService.getTonalKeyByLabels("A", "MAJOR");

		var rhythmPatterns = rhythmPatternCreator.createRhythmPatterns(
				RhythmPatternCreator.RhythmPatternSetIngredients.builder()
						.formId(form.getId())
						.beatCount(4)
						.eccentricOptions(rhythmEccentricOptions)
						.granularityOptions(RHYTHM_GRANULARITY_OPTIONS)
						.valueCountOptions(valueCountOptions)
						.build());

		int[] tunePatternAmbitusOptions = {
				RANDOM.nextInt(2, 9),
				RANDOM.nextInt(2, 9),
				RANDOM.nextInt(2, 9)
		};
		int[] tunePatternToneAmountOptions = {
				RANDOM.nextInt(2, 9),
				RANDOM.nextInt(2, 9),
				RANDOM.nextInt(2, 9)
		};

		var tunePatterns = tunePatternCreator.createTunePatternSet(TunePatternCreator.TunePatternSetIngredients.builder()
				.formId(form.getId())
				.eccentricityOptions(TUNE_ECCENTRICITY_OPTIONS)
				.ambitusOptions(tunePatternAmbitusOptions)
				.toneAmountOptions(tunePatternToneAmountOptions)
				.build());


		var repetitionPatterns = tunePatternCreator.createRepetitionPatternSet(rhythmPatterns, repetitionDensityOptions);

		var title = AlphabetUtils.generateRandomName();
		var tempo = RANDOM.nextInt(60, 260);

		compositionService.setCurrentComposition(compositionCreator.buildNewComposition(NewCompositionIngredients.builder()
				.rhythmPatterns(rhythmPatterns)
				.tunePatterns(tunePatterns)
				.repetitionPatterns(repetitionPatterns)
				.mainKey(mainKey)
				.form(form)
				.title(title)
				.tempo(tempo)
				.build()));
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

	private void migrateAssets() {
		migrationService.execSql("db/migration/migrate-modi.sql");
		migrationService.execSql("db/migration/migrate-quint-circle.sql");
		migrationService.execSql("db/migration/migrate-patterns-english.sql");
		migrationService.execSql("db/migration/migrate-form-english.sql");

		migrationService.removeWhitespacesFromAllHookPatterns();
	}
}
